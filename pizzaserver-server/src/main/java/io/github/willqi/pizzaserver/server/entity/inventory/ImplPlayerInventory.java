package io.github.willqi.pizzaserver.server.entity.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ContainerOpenPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventoryContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.MobEquipmentPacket;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class ImplPlayerInventory extends ImplLivingEntityInventory implements PlayerInventory {

    private int selectedSlot;
    private ItemStack cursor = ItemRegistry.getItem(BlockTypeID.AIR);


    public ImplPlayerInventory(Player player) {
        super(player, 36, InventoryID.MAIN_INVENTORY);
    }

    @Override
    public Player getEntity() {
        return (Player)super.getEntity();
    }

    @Override
    public boolean setHelmet(ItemStack helmet) {
        if (super.setHelmet(helmet)) {
            sendSlot(this.getEntity(), this.getHelmet(), 0, InventoryID.ARMOR_INVENTORY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setChestplate(ItemStack chestplate) {
        if (super.setChestplate(chestplate)) {
            sendSlot(this.getEntity(), this.getChestplate(), 1, InventoryID.ARMOR_INVENTORY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setLeggings(ItemStack leggings) {
        if (super.setLeggings(leggings)) {
            sendSlot(this.getEntity(), this.getLeggings(), 2, InventoryID.ARMOR_INVENTORY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setBoots(ItemStack boots) {
        if (super.setBoots(boots)) {
            sendSlot(this.getEntity(), this.getBoots(), 3, InventoryID.ARMOR_INVENTORY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    @Override
    public boolean setSelectedSlot(int slot) {
        if (slot < 0 || slot >= 9) {
            throw new IllegalArgumentException("The selected slot cannot be a number outside of slots 0-8");
        }

        if (this.selectedSlot != slot && !this.getSlot(slot).getItemType().getItemId().equals(BlockTypeID.AIR)) {
            this.selectedSlot = slot;

            // To select a slot, we need to send a mob equipment packet and then resend the slot we are selecting
            // However, this appears to only work for non-empty slots. Sending this from an empty to another empty slot will not change the slot
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.setEntityRuntimeId(this.getEntity().getId());
            mobEquipmentPacket.setHotbarSlot(slot);
            mobEquipmentPacket.setSlot(slot);
            mobEquipmentPacket.setEquipment(this.getSlot(slot));
            this.getEntity().sendPacket(mobEquipmentPacket);

            sendSlot(this.getEntity(), this.getSlot(slot), slot, this.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getHeldItem() {
        return this.getSlot(this.getSelectedSlot());
    }

    @Override
    public boolean setHeldItem(ItemStack mainHand) {
        return this.setSlot(this.getSelectedSlot(), mainHand);
    }

    @Override
    public boolean setOffhandItem(ItemStack offHand) {
        if (super.setOffhandItem(offHand)) {
            InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
            inventoryContentPacket.setInventoryId(InventoryID.OFF_HAND_INVENTORY);
            inventoryContentPacket.setContents(new ItemStack[]{ this.getOffhandItem() });
            this.getEntity().sendPacket(inventoryContentPacket);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getCursor() {
        return Optional.ofNullable(this.cursor).orElse(ItemRegistry.getItem(BlockTypeID.AIR)).clone();
    }

    @Override
    public boolean setCursor(ItemStack cursor) {
        return this.setCursor(cursor, false);
    }

    public boolean setCursor(ItemStack cursor, boolean keepNetworkId) {
        // TODO: events
        this.cursor = keepNetworkId ? cursor : cursor.newNetworkStack();
        // TODO: send cursor packet
        return true;
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setEntityRuntimeId(this.getEntity().getId());
        containerOpenPacket.setInventoryType(-1);   // TODO: get rid of magic number and replace with enum
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
