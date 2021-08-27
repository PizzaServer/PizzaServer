package io.github.willqi.pizzaserver.server.entity.inventory;

import io.github.willqi.pizzaserver.api.entity.LivingEntity;
import io.github.willqi.pizzaserver.api.entity.inventory.LivingEntityInventory;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.packets.MobArmourEquipmentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.MobEquipmentPacket;

import java.util.Optional;

public class ImplLivingEntityInventory extends BaseEntityInventory implements LivingEntityInventory {

    private ItemStack helmet = null;
    private ItemStack chestplate = null;
    private ItemStack leggings = null;
    private ItemStack boots = null;

    private ItemStack mainHand = null;
    private ItemStack offHand = null;


    public ImplLivingEntityInventory(LivingEntity entity, int size) {
        super(entity, size);
    }

    public ImplLivingEntityInventory(LivingEntity entity, int size, int id) {
        super(entity, size, id);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity)super.getEntity();
    }

    @Override
    public ItemStack getHelmet() {
        return Optional.ofNullable(this.helmet).orElse(ItemRegistry.getItem(BlockTypeID.AIR)).clone();
    }

    @Override
    public boolean setHelmet(ItemStack helmet) {
        if (isDifferentItems(this.helmet, helmet)) {
            this.helmet = helmet.clone();
            this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getChestplate() {
        return Optional.ofNullable(this.chestplate).orElse(ItemRegistry.getItem(BlockTypeID.AIR)).clone();
    }

    @Override
    public boolean setChestplate(ItemStack chestplate) {
        if (isDifferentItems(this.chestplate, chestplate)) {
            this.chestplate = chestplate.clone();
            this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getLeggings() {
        return Optional.ofNullable(this.leggings).orElse(ItemRegistry.getItem(BlockTypeID.AIR)).clone();
    }

    @Override
    public boolean setLeggings(ItemStack leggings) {
        if (isDifferentItems(this.leggings, leggings)) {
            this.leggings = leggings.clone();
            this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getBoots() {
        return Optional.ofNullable(this.boots).orElse(ItemRegistry.getItem(BlockTypeID.AIR)).clone();
    }

    @Override
    public boolean setBoots(ItemStack boots) {
        if (isDifferentItems(this.boots, boots)) {
            this.boots = boots.clone();
            this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
            return true;
        } else {
            return false;
        }
    }

    protected void broadcastMobArmourEquipmentPacket() {
        for (Player player : this.getEntity().getViewers()) {
            MobArmourEquipmentPacket mobArmourEquipmentPacket = new MobArmourEquipmentPacket();
            mobArmourEquipmentPacket.setEntityRuntimeId(this.getEntity().getId());
            mobArmourEquipmentPacket.setHelmet(this.helmet);
            mobArmourEquipmentPacket.setChestplate(this.chestplate);
            mobArmourEquipmentPacket.setLeggings(this.leggings);
            mobArmourEquipmentPacket.setBoots(this.boots);
            player.sendPacket(mobArmourEquipmentPacket);
        }
    }

    @Override
    public ItemStack getHeldItem() {
        return Optional.ofNullable(this.mainHand).orElse(ItemRegistry.getItem(BlockTypeID.AIR)).clone();
    }

    @Override
    public boolean setHeldItem(ItemStack mainHand) {
        if (isDifferentItems(this.mainHand, mainHand)) {
            this.mainHand = mainHand.clone();
            this.broadcastMobEquipmentPacket(this.mainHand, 0, true); // TODO when entity support is implemented: check if entity supports armor before sending
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getOffhandItem() {
        return Optional.ofNullable(this.offHand).orElse(ItemRegistry.getItem(BlockTypeID.AIR)).clone();
    }

    @Override
    public boolean setOffhandItem(ItemStack offHand) {
        if (isDifferentItems(this.offHand, offHand)) {
            this.offHand = offHand.clone();
            this.broadcastMobEquipmentPacket(this.offHand, 1, false); // TODO when entity support is implemented: check if entity supports armor before sending
            return true;
        } else {
            return false;
        }
    }

    protected void broadcastMobEquipmentPacket(ItemStack itemStack, int slot, boolean mainHand) {
        for (Player player : this.getEntity().getViewers()) {
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.setEntityRuntimeId(this.getEntity().getId());
            mobEquipmentPacket.setInventoryId(mainHand ? InventoryID.MAIN_INVENTORY : InventoryID.OFF_HAND_INVENTORY);
            mobEquipmentPacket.setSlot(slot);
            mobEquipmentPacket.setHotbarSlot(slot);
            mobEquipmentPacket.setEquipment(itemStack);
            player.sendPacket(mobEquipmentPacket);
        }
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        // TODO: open inventory depending on inventory type
    }

}
