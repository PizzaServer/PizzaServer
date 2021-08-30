package io.github.willqi.pizzaserver.server.entity.inventory;

import io.github.willqi.pizzaserver.api.entity.LivingEntity;
import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.entity.inventory.LivingEntityInventory;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.packets.MobArmourEquipmentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.MobEquipmentPacket;

import java.util.Optional;
import java.util.Set;

public class ImplLivingEntityInventory extends BaseEntityInventory implements LivingEntityInventory {

    protected ItemStack helmet = null;
    protected ItemStack chestplate = null;
    protected ItemStack leggings = null;
    protected ItemStack boots = null;

    protected ItemStack mainHand = null;
    protected ItemStack offHand = null;


    public ImplLivingEntityInventory(LivingEntity entity, Set<InventorySlotType> slotTypes, int size) {
        super(entity, slotTypes, size);
    }

    public ImplLivingEntityInventory(LivingEntity entity, Set<InventorySlotType> slotTypes, int size, int id) {
        super(entity, slotTypes, size, id);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity)super.getEntity();
    }

    @Override
    public ItemStack getHelmet() {
        return this.getHelmet(true);
    }

    public ItemStack getHelmet(boolean clone) {
        ItemStack helmet = Optional.ofNullable(this.helmet).orElse(ItemRegistry.getItem(BlockTypeID.AIR));
        if (clone) {
            return helmet.clone();
        } else {
            return helmet;
        }
    }

    @Override
    public boolean setHelmet(ItemStack helmet) {
        return this.setHelmet(null, helmet, false);
    }

    public boolean setHelmet(Player player, ItemStack helmet, boolean keepNetworkId) {
        // TODO: events
        this.helmet = keepNetworkId ? ensureItemStackExists(helmet) : ensureItemStackExists(helmet).newNetworkStack();
        this.broadcastMobArmourEquipmentPacket(player); // TODO when entity support is implemented: check if entity supports armor before sending
        return true;
    }

    @Override
    public ItemStack getChestplate() {
        return this.getChestplate(true);
    }

    public ItemStack getChestplate(boolean clone) {
        ItemStack chestplate = Optional.ofNullable(this.chestplate).orElse(ItemRegistry.getItem(BlockTypeID.AIR));
        if (clone) {
            return chestplate.clone();
        } else {
            return chestplate;
        }
    }

    @Override
    public boolean setChestplate(ItemStack chestplate) {
        return this.setChestplate(null, chestplate, false);
    }

    public boolean setChestplate(Player player, ItemStack chestplate, boolean keepNetworkId) {
        // TODO: events
        this.chestplate = keepNetworkId ? ensureItemStackExists(chestplate) : ensureItemStackExists(chestplate).newNetworkStack();
        this.broadcastMobArmourEquipmentPacket(player); // TODO when entity support is implemented: check if entity supports armor before sending
        return true;
    }

    @Override
    public ItemStack getLeggings() {
        return this.getLeggings(true);
    }

    public ItemStack getLeggings(boolean clone) {
        ItemStack leggings = Optional.ofNullable(this.leggings).orElse(ItemRegistry.getItem(BlockTypeID.AIR));
        if (clone) {
            return leggings.clone();
        } else {
            return leggings;
        }
    }

    @Override
    public boolean setLeggings(ItemStack leggings) {
        return this.setLeggings(null, leggings, false);
    }

    public boolean setLeggings(Player player, ItemStack leggings, boolean keepNetworkId) {
        // TODO: events
        this.leggings = keepNetworkId ? ensureItemStackExists(leggings) : ensureItemStackExists(leggings).newNetworkStack();
        this.broadcastMobArmourEquipmentPacket(player);
        return true;
    }

    @Override
    public ItemStack getBoots() {
        return this.getBoots(true);
    }

    public ItemStack getBoots(boolean clone) {
        ItemStack boots = Optional.ofNullable(this.boots).orElse(ItemRegistry.getItem(BlockTypeID.AIR));
        if (clone) {
            return boots.clone();
        } else {
            return boots;
        }
    }

    @Override
    public boolean setBoots(ItemStack boots) {
        return this.setBoots(null, boots, false);
    }

    public boolean setBoots(Player player, ItemStack boots, boolean keepNetworkId) {
        // TODO: events
        this.boots = keepNetworkId ? ensureItemStackExists(boots) : ensureItemStackExists(boots).newNetworkStack();
        this.broadcastMobArmourEquipmentPacket(player);
        return true;
    }

    protected void broadcastMobArmourEquipmentPacket(Player activatingPlayer) {
        for (Player player : this.getEntity().getViewers()) {
            if (!player.equals(activatingPlayer)) {
                MobArmourEquipmentPacket mobArmourEquipmentPacket = new MobArmourEquipmentPacket();
                mobArmourEquipmentPacket.setEntityRuntimeId(this.getEntity().getId());
                mobArmourEquipmentPacket.setHelmet(this.getHelmet());
                mobArmourEquipmentPacket.setChestplate(this.getChestplate());
                mobArmourEquipmentPacket.setLeggings(this.getLeggings());
                mobArmourEquipmentPacket.setBoots(this.getBoots());
                player.sendPacket(mobArmourEquipmentPacket);
            }
        }
    }

    @Override
    public ItemStack getHeldItem() {
        return this.getHeldItem(true);
    }

    public ItemStack getHeldItem(boolean clone) {
        ItemStack mainHand = Optional.ofNullable(this.mainHand).orElse(ItemRegistry.getItem(BlockTypeID.AIR));
        if (clone) {
            return mainHand.clone();
        } else {
            return mainHand;
        }
    }

    @Override
    public boolean setHeldItem(ItemStack mainHand) {
        return this.setHeldItem(null, mainHand, false);
    }

    public boolean setHeldItem(Player player, ItemStack mainHand, boolean keepNetworkId) {
        // TODO: events
        this.mainHand = keepNetworkId ? ensureItemStackExists(mainHand) : ensureItemStackExists(mainHand).newNetworkStack();
        this.broadcastMobEquipmentPacket(player, this.getHeldItem(), 0, true);
        return true;
    }

    @Override
    public ItemStack getOffhandItem() {
        return this.getOffhandItem(true);
    }

    public ItemStack getOffhandItem(boolean clone) {
        ItemStack offhand = Optional.ofNullable(this.offHand).orElse(ItemRegistry.getItem(BlockTypeID.AIR));
        if (clone) {
            return offhand.clone();
        } else {
            return offhand;
        }
    }

    @Override
    public boolean setOffhandItem(ItemStack offHand) {
        return this.setOffhandItem(null, offHand, false);
    }

    public boolean setOffhandItem(Player player, ItemStack offHand, boolean keepNetworkId) {
        // TODO: events
        this.offHand = keepNetworkId ? ensureItemStackExists(offHand) : ensureItemStackExists(offHand).newNetworkStack();
        this.broadcastMobEquipmentPacket(player, this.getHeldItem(), 1, false);
        return true;
    }

    protected void broadcastMobEquipmentPacket(Player activatingPlayer, ItemStack itemStack, int slot, boolean mainHand) {
        for (Player player : this.getEntity().getViewers()) {
            if (!player.equals(activatingPlayer)) {
                MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
                mobEquipmentPacket.setEntityRuntimeId(this.getEntity().getId());
                mobEquipmentPacket.setInventoryId(mainHand ? InventoryID.MAIN_INVENTORY : InventoryID.OFF_HAND_INVENTORY);
                mobEquipmentPacket.setSlot(slot);
                mobEquipmentPacket.setHotbarSlot(slot);
                mobEquipmentPacket.setEquipment(itemStack);
                player.sendPacket(mobEquipmentPacket);
            }
        }
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        // TODO: open inventory depending on inventory type
    }

}
