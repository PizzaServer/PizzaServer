package io.github.willqi.pizzaserver.server.entity.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.item.types.components.DurableItemComponent;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventoryType;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ContainerOpenPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventoryContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventorySlotPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.MobEquipmentPacket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ImplPlayerInventory extends ImplLivingEntityInventory implements PlayerInventory {

    private static final Set<InventorySlotType> PLAYER_SLOT_TYPES = new HashSet<InventorySlotType>(){
        {
            this.add(InventorySlotType.ARMOR);
            this.add(InventorySlotType.INVENTORY);
            this.add(InventorySlotType.HOTBAR);
            this.add(InventorySlotType.PLAYER_INVENTORY);
            this.add(InventorySlotType.CURSOR);
            this.add(InventorySlotType.OFFHAND);

            // TODO: uncomment when crafting inventory related slots are implemented
//            this.add(InventorySlotType.CRAFTING_ITEM);
//            this.add(InventorySlotType.CRAFTING_RESULT);
        }
    };

    private int selectedSlot;
    private ItemStack cursor = ItemRegistry.getItem(BlockTypeID.AIR);


    public ImplPlayerInventory(Player player) {
        super(player, PLAYER_SLOT_TYPES, 36, InventoryID.MAIN_INVENTORY);
    }

    @Override
    public Player getEntity() {
        return (Player)super.getEntity();
    }

    @Override
    public void setSlot(Player player, int slot, ItemStack itemStack, boolean keepNetworkId) {
        ItemStack oldItemStack = this.getSlot(slot);
        super.setSlot(player, slot, itemStack, keepNetworkId);

        // Only broadcast this slot change to other entities if the slot is in the main hand and the change is a visible change.
        boolean isSelectedSlot = slot == this.getSelectedSlot();
        boolean isSameItemVisually = itemStack.getCompoundTag().equals(oldItemStack.getCompoundTag()) &&
                (itemStack.getDamage() == oldItemStack.getDamage() || (itemStack.getItemType() instanceof DurableItemComponent)) &&
                itemStack.getItemType().equals(oldItemStack.getItemType());

        if (isSelectedSlot && !isSameItemVisually) {
            this.broadcastMobEquipmentPacket(player, itemStack, slot, true);
        }
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        super.setHelmet(helmet);
        sendInventorySlot(this.getEntity(), this.getHelmet(), 0, InventoryID.ARMOR_INVENTORY);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        super.setChestplate(chestplate);
        sendInventorySlot(this.getEntity(), this.getChestplate(), 1, InventoryID.ARMOR_INVENTORY);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        super.setLeggings(leggings);
        sendInventorySlot(this.getEntity(), this.getLeggings(), 2, InventoryID.ARMOR_INVENTORY);
    }

    @Override
    public void setBoots(ItemStack boots) {
        super.setBoots(boots);
        sendInventorySlot(this.getEntity(), this.getBoots(), 3, InventoryID.ARMOR_INVENTORY);
    }

    @Override
    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    @Override
    public void setSelectedSlot(int slot) {
        this.setSelectedSlot(slot, true);
    }

    public void setSelectedSlot(int slot, boolean sendPackets) {
        if (slot < 0 || slot >= 9) {
            throw new IllegalArgumentException("The selected slot cannot be a number outside of slots 0-8");
        }
        this.selectedSlot = slot;

        if (sendPackets) {
            // To select a slot, we need to send a mob equipment packet and then resend the slot we are selecting
            // However, this appears to only work for non-empty slots. Sending this from an empty to another empty slot will not change the slot
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.setEntityRuntimeId(this.getEntity().getId());
            mobEquipmentPacket.setHotbarSlot(slot);
            mobEquipmentPacket.setSlot(slot);
            mobEquipmentPacket.setEquipment(this.getSlot(slot));
            this.getEntity().sendPacket(mobEquipmentPacket);

            sendInventorySlot(this.getEntity(), this.getSlot(slot), slot, this.getId());
        }
    }

    @Override
    public ItemStack getHeldItem() {
        return this.getSlot(this.getSelectedSlot());
    }

    @Override
    public void setHeldItem(ItemStack mainHand) {
        this.setSlot(this.getSelectedSlot(), mainHand);
    }

    @Override
    public void setOffhandItem(ItemStack offHand) {
        super.setOffhandItem(offHand);

        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setInventoryId(InventoryID.OFF_HAND_INVENTORY);
        inventoryContentPacket.setContents(new ItemStack[]{ this.getOffhandItem() });
        this.getEntity().sendPacket(inventoryContentPacket);
    }

    @Override
    public ItemStack getCursor() {
        return Optional.ofNullable(this.cursor).orElse(ItemRegistry.getItem(BlockTypeID.AIR)).clone();
    }

    @Override
    public void setCursor(ItemStack cursor) {
        this.setCursor(cursor, false);

        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setInventoryId(InventoryID.PLAYER_UI);
        inventorySlotPacket.setItem(this.cursor);
        this.getEntity().sendPacket(inventorySlotPacket);
    }

    public void setCursor(ItemStack cursor, boolean keepNetworkId) {
        this.cursor = keepNetworkId ? ItemStack.ensureItemStackExists(cursor) : ItemStack.ensureItemStackExists(cursor).newNetworkStack();
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setEntityRuntimeId(this.getEntity().getId());
        containerOpenPacket.setInventoryType(InventoryType.INVENTORY);
        containerOpenPacket.setCoordinates(this.getEntity().getLocation().toVector3i());
        player.sendPacket(containerOpenPacket);
    }

    @Override
    public boolean openFor(Player player) {
        if (!player.equals(this.getEntity())) {
            return false;
        }
        return super.openFor(player);
    }

    @Override
    public Set<Player> getViewers() {   // The player is ALWAYS a viewer. However, it is possible to open and close the inventory
        return Collections.singleton(this.getEntity());
    }

}
