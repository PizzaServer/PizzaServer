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

public class ImplLivingEntityInventory extends BaseInventory implements LivingEntityInventory {

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
    public void clear() {
        super.clear();
        this.setHeldItem(null);
        this.setOffhandItem(null);
        this.setArmour(null, null, null, null);
    }

    @Override
    public void setArmour(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.setArmour(helmet, chestplate, leggings, boots, false);
    }

    public void setArmour(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, boolean keepNetworkId) {
        this.helmet = keepNetworkId ? helmet : ItemStack.ensureItemStackExists(helmet).newNetworkStack();
        this.chestplate = keepNetworkId ? chestplate : ItemStack.ensureItemStackExists(chestplate).newNetworkStack();
        this.leggings = keepNetworkId ? leggings : ItemStack.ensureItemStackExists(leggings).newNetworkStack();
        this.boots = keepNetworkId ? boots : ItemStack.ensureItemStackExists(boots).newNetworkStack();
        this.broadcastMobArmourEquipmentPacket();
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
    public void setHelmet(ItemStack helmet) {
        this.setHelmet(helmet, false);
    }

    public void setHelmet(ItemStack helmet, boolean keepNetworkId) {
        this.helmet = keepNetworkId ? helmet : ItemStack.ensureItemStackExists(helmet).newNetworkStack();
        this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
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
    public void setChestplate(ItemStack chestplate) {
        this.setChestplate(chestplate, false);
    }

    public void setChestplate(ItemStack chestplate, boolean keepNetworkId) {
        this.chestplate = keepNetworkId ? chestplate : ItemStack.ensureItemStackExists(chestplate).newNetworkStack();
        this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
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
    public void setLeggings(ItemStack leggings) {
        this.setLeggings(leggings, false);
    }

    public void setLeggings(ItemStack leggings, boolean keepNetworkId) {
        this.leggings = keepNetworkId ? leggings : ItemStack.ensureItemStackExists(leggings).newNetworkStack();
        this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
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
    public void setBoots(ItemStack boots) {
        this.setBoots(boots, false);
    }

    public void setBoots(ItemStack boots, boolean keepNetworkId) {
        this.boots = keepNetworkId ? boots : ItemStack.ensureItemStackExists(boots).newNetworkStack();
        this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
    }

    protected void broadcastMobArmourEquipmentPacket() {
        for (Player player : this.getEntity().getViewers()) {
            MobArmourEquipmentPacket mobArmourEquipmentPacket = new MobArmourEquipmentPacket();
            mobArmourEquipmentPacket.setEntityRuntimeId(this.getEntity().getId());
            mobArmourEquipmentPacket.setHelmet(this.getHelmet());
            mobArmourEquipmentPacket.setChestplate(this.getChestplate());
            mobArmourEquipmentPacket.setLeggings(this.getLeggings());
            mobArmourEquipmentPacket.setBoots(this.getBoots());
            player.sendPacket(mobArmourEquipmentPacket);
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
    public void setHeldItem(ItemStack mainHand) {
        this.setHeldItem(mainHand, false);
    }

    public void setHeldItem(ItemStack mainHand, boolean keepNetworkId) {
        this.mainHand = keepNetworkId ? mainHand : ItemStack.ensureItemStackExists(mainHand).newNetworkStack();
        this.broadcastMobEquipmentPacket(this.getHeldItem(), 0, true); // TODO when entity support is implemented: check if entity supports armor before sending
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
    public void setOffhandItem(ItemStack offHand) {
        this.setOffhandItem(offHand, false);
    }

    public void setOffhandItem(ItemStack offHand, boolean keepNetworkId) {
        this.offHand = keepNetworkId ? offHand : ItemStack.ensureItemStackExists(offHand).newNetworkStack();
        this.broadcastMobEquipmentPacket(this.getHeldItem(), 1, false); // TODO when entity support is implemented: check if entity supports armor before sending
    }

    /**
     * Broadcasts mob equipment packet to all viewers of this entity
     * @param itemStack the item stack being sent
     * @param slot the slot to send it as
     * @param mainHand if the item is in the main hand
     */
    protected void broadcastMobEquipmentPacket(ItemStack itemStack, int slot, boolean mainHand) {
        for (Player player : this.getEntity().getViewers()) {
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.setEntityRuntimeId(this.getEntity().getId());
            mobEquipmentPacket.setInventoryId(mainHand ? InventoryID.MAIN_INVENTORY : InventoryID.OFF_HAND_INVENTORY);
            mobEquipmentPacket.setSlot(slot);
            mobEquipmentPacket.setHotbarSlot(slot);
            mobEquipmentPacket.setEquipment(ItemStack.ensureItemStackExists(itemStack));
            player.sendPacket(mobEquipmentPacket);
        }
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        // TODO: open inventory depending on inventory type
    }

}
