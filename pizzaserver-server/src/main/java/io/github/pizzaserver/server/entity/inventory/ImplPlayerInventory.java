package io.github.pizzaserver.server.entity.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import com.nukkitx.protocol.bedrock.packet.MobEquipmentPacket;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.item.ItemUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class ImplPlayerInventory extends ImplEntityInventory implements PlayerInventory {

    private int selectedSlot;
    private Item cursor = ItemRegistry.getInstance().getItem(BlockID.AIR);


    public ImplPlayerInventory(Player player) {
        super(player, ContainerType.INVENTORY, InventoryUtils.getSlotCount(ContainerType.INVENTORY), ContainerId.INVENTORY);
    }

    @Override
    public Player getEntity() {
        return (Player) super.getEntity();
    }

    @Override
    public void clear() {
        super.clear();
        this.setCursor(null);
    }

    @Override
    public void setSlot(Player player, int slot, Item item, boolean keepNetworkId) {
        Item oldItemStack = this.getSlot(slot);
        super.setSlot(player, slot, item, keepNetworkId);

        // Only broadcast this slot change to other entities if the slot is in the main hand and the change is a visible change.
        boolean isSelectedSlot = slot == this.getSelectedSlot();
        if (isSelectedSlot && !oldItemStack.visuallySameAs(Item.getAirIfNull(item))) {
            // Only broadcast the packet if the item has visually changed for other players.
            this.broadcastMobEquipmentPacket(item, slot, true);
        }
    }

    @Override
    public void setArmour(Item helmet, Item chestplate, Item leggings, Item boots) {
        super.setArmour(helmet, chestplate, leggings, boots);
        sendInventorySlots(this.getEntity(), new Item[]{ this.getHelmet(), this.getChestplate(), this.getLeggings(), this.getBoots() }, ContainerId.ARMOR);
    }

    @Override
    public void setHelmet(Item helmet) {
        super.setHelmet(helmet);
        sendInventorySlot(this.getEntity(), this.getHelmet(), 0, ContainerId.ARMOR);
    }

    @Override
    public void setChestplate(Item chestplate) {
        super.setChestplate(chestplate);
        sendInventorySlot(this.getEntity(), this.getChestplate(), 1, ContainerId.ARMOR);
    }

    @Override
    public void setLeggings(Item leggings) {
        super.setLeggings(leggings);
        sendInventorySlot(this.getEntity(), this.getLeggings(), 2, ContainerId.ARMOR);
    }

    @Override
    public void setBoots(Item boots) {
        super.setBoots(boots);
        sendInventorySlot(this.getEntity(), this.getBoots(), 3, ContainerId.ARMOR);
    }

    @Override
    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    @Override
    public void setSelectedSlot(int slot) {
        this.setSelectedSlot(slot, false);
    }

    /**
     * Change the selected slot of the player.
     * @param slot slot
     * @param calledByPlayer if this action was done by the player rather than manually through the server
     */
    public void setSelectedSlot(int slot, boolean calledByPlayer) {
        if (slot < 0 || slot >= 9) {
            throw new IllegalArgumentException("The selected slot cannot be a number outside of slots 0-8");
        }

        Item oldItem = this.getHeldItem();
        this.selectedSlot = slot;

        // Only broadcast the item to others if the item has changed visually for other players.
        if (!oldItem.visuallySameAs(this.getHeldItem())) {
            this.broadcastMobEquipmentPacket(this.getHeldItem(), 0, true);
        }

        // if this not a player action, we need to change the client's slot
        if (!calledByPlayer) {
            // To select a slot, we need to send a mob equipment packet and then resend the slot we are selecting
            // However, this appears to only work for non-empty slots. Sending this from an empty to another empty slot will not change the slot
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.setRuntimeEntityId(this.getEntity().getId());
            mobEquipmentPacket.setHotbarSlot(slot);
            mobEquipmentPacket.setInventorySlot(slot);
            mobEquipmentPacket.setItem(ItemUtils.serializeForNetwork(this.getSlot(slot), this.getEntity().getVersion()));
            this.getEntity().sendPacket(mobEquipmentPacket);

            sendInventorySlot(this.getEntity(), this.getSlot(slot), slot, this.getId());
        }
    }

    @Override
    public Item getHeldItem() {
        return this.getSlot(this.getSelectedSlot());
    }

    @Override
    public void setHeldItem(Item mainHand) {
        this.setSlot(this.getSelectedSlot(), mainHand);
    }

    @Override
    public void setOffhandItem(Item offHand) {
        super.setOffhandItem(offHand);
        sendInventorySlots(this.getEntity(), new Item[]{ this.getOffhandItem() }, ContainerId.OFFHAND);
    }

    @Override
    public Item getCursor() {
        return Optional.ofNullable(this.cursor).orElse(ItemRegistry.getInstance().getItem(BlockID.AIR)).clone();
    }

    @Override
    public void setCursor(Item cursor) {
        this.setCursor(cursor, false);
        sendInventorySlot(this.getEntity(), this.cursor, 0, ContainerId.UI);
    }

    public void setCursor(Item cursor, boolean keepNetworkId) {
        if (cursor == null || cursor.isEmpty()) {
            this.cursor = null;
        } else {
            this.cursor = keepNetworkId ? Item.getAirIfNull(cursor).clone() : Item.getAirIfNull(cursor).newNetworkCopy();
        }
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setUniqueEntityId(this.getEntity().getId());
        containerOpenPacket.setType(ContainerType.INVENTORY);
        containerOpenPacket.setBlockPosition(this.getEntity().getLocation().toVector3i());
        player.sendPacket(containerOpenPacket);
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return this.getEntity().equals(player);
    }

    @Override
    public boolean shouldBeClosedFor(Player player) {
        return !player.equals(this.getEntity());
    }

    @Override
    public Set<Player> getViewers() {   // The player is ALWAYS a viewer. However, it is possible to open and close the inventory
        return Collections.singleton(this.getEntity());
    }

}
