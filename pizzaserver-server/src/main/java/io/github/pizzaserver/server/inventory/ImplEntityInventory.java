package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import com.nukkitx.protocol.bedrock.packet.MobArmorEquipmentPacket;
import com.nukkitx.protocol.bedrock.packet.MobEquipmentPacket;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.inventory.EntityInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.item.ItemUtils;

import java.util.Optional;

public class ImplEntityInventory extends BaseInventory implements EntityInventory {

    protected final Entity entity;

    protected Item helmet = null;
    protected Item chestplate = null;
    protected Item leggings = null;
    protected Item boots = null;

    protected Item mainHand = null;
    protected Item offHand = null;


    public ImplEntityInventory(Entity entity, ContainerType containerType, int size) {
        super(containerType, size);
        this.entity = entity;
    }

    public ImplEntityInventory(Entity entity, ContainerType containerType, int size, int id) {
        super(containerType, size, id);
        this.entity = entity;
    }

    @Override
    public void clear() {
        super.clear();
        this.setHeldItem(null);
        this.setOffhandItem(null);
        this.setArmour(null, null, null, null);
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public void setArmour(Item helmet, Item chestplate, Item leggings, Item boots) {
        this.setArmour(helmet, chestplate, leggings, boots, false);
    }

    public void setArmour(Item helmet, Item chestplate, Item leggings, Item boots, boolean keepNetworkId) {
        this.setHelmet(helmet, keepNetworkId);
        this.setChestplate(chestplate, keepNetworkId);
        this.setLeggings(leggings, keepNetworkId);
        this.setBoots(boots, keepNetworkId);
    }

    @Override
    public Item getHelmet() {
        return this.getHelmet(true);
    }

    public Item getHelmet(boolean clone) {
        Item helmet = Optional.ofNullable(this.helmet).orElse(ItemRegistry.getInstance().getItem(BlockID.AIR));
        if (clone) {
            return helmet.clone();
        } else {
            return helmet;
        }
    }

    @Override
    public void setHelmet(Item helmet) {
        this.setHelmet(helmet, false);
    }

    public void setHelmet(Item helmet, boolean keepNetworkId) {
        if (helmet == null || helmet.isEmpty()) {
            this.helmet = null;
        } else {
            this.helmet = keepNetworkId ? Item.getAirIfNull(helmet).clone() : Item.getAirIfNull(helmet).newNetworkCopy();
        }
        this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
    }

    @Override
    public Item getChestplate() {
        return this.getChestplate(true);
    }

    public Item getChestplate(boolean clone) {
        Item chestplate = Optional.ofNullable(this.chestplate).orElse(ItemRegistry.getInstance().getItem(BlockID.AIR));
        if (clone) {
            return chestplate.clone();
        } else {
            return chestplate;
        }
    }

    @Override
    public void setChestplate(Item chestplate) {
        this.setChestplate(chestplate, false);
    }

    public void setChestplate(Item chestplate, boolean keepNetworkId) {
        if (chestplate == null || chestplate.isEmpty()) {
            this.chestplate = null;
        } else {
            this.chestplate = keepNetworkId ? Item.getAirIfNull(chestplate).clone() : Item.getAirIfNull(chestplate).newNetworkCopy();
        }
        this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
    }

    @Override
    public Item getLeggings() {
        return this.getLeggings(true);
    }

    public Item getLeggings(boolean clone) {
        Item leggings = Optional.ofNullable(this.leggings).orElse(ItemRegistry.getInstance().getItem(BlockID.AIR));
        if (clone) {
            return leggings.clone();
        } else {
            return leggings;
        }
    }

    @Override
    public void setLeggings(Item leggings) {
        this.setLeggings(leggings, false);
    }

    public void setLeggings(Item leggings, boolean keepNetworkId) {
        if (leggings == null || leggings.isEmpty()) {
            this.leggings = null;
        } else {
            this.leggings = keepNetworkId ? Item.getAirIfNull(leggings).clone() : Item.getAirIfNull(leggings).newNetworkCopy();
        }
        this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
    }

    @Override
    public Item getBoots() {
        return this.getBoots(true);
    }

    public Item getBoots(boolean clone) {
        Item boots = Optional.ofNullable(this.boots).orElse(ItemRegistry.getInstance().getItem(BlockID.AIR));
        if (clone) {
            return boots.clone();
        } else {
            return boots;
        }
    }

    @Override
    public void setBoots(Item boots) {
        this.setBoots(boots, false);
    }

    public void setBoots(Item boots, boolean keepNetworkId) {
        if (boots == null || boots.isEmpty()) {
            this.boots = null;
        } else {
            this.boots = keepNetworkId ? Item.getAirIfNull(boots).clone() : Item.getAirIfNull(boots).newNetworkCopy();
        }
        this.broadcastMobArmourEquipmentPacket(); // TODO when entity support is implemented: check if entity supports armor before sending
    }

    protected void broadcastMobArmourEquipmentPacket() {
        for (Player player : this.getEntity().getViewers()) {
            MobArmorEquipmentPacket mobArmourEquipmentPacket = new MobArmorEquipmentPacket();
            mobArmourEquipmentPacket.setRuntimeEntityId(this.getEntity().getId());
            mobArmourEquipmentPacket.setHelmet(ItemUtils.serializeForNetwork(this.getHelmet(), player.getVersion()));
            mobArmourEquipmentPacket.setChestplate(ItemUtils.serializeForNetwork(this.getChestplate(), player.getVersion()));
            mobArmourEquipmentPacket.setLeggings(ItemUtils.serializeForNetwork(this.getLeggings(), player.getVersion()));
            mobArmourEquipmentPacket.setBoots(ItemUtils.serializeForNetwork(this.getBoots(), player.getVersion()));
            player.sendPacket(mobArmourEquipmentPacket);
        }
    }

    @Override
    public Item getHeldItem() {
        return this.getHeldItem(true);
    }

    public Item getHeldItem(boolean clone) {
        Item mainHand = Optional.ofNullable(this.mainHand).orElse(ItemRegistry.getInstance().getItem(BlockID.AIR));
        if (clone) {
            return mainHand.clone();
        } else {
            return mainHand;
        }
    }

    @Override
    public void setHeldItem(Item mainHand) {
        this.setHeldItem(mainHand, false);
    }

    public void setHeldItem(Item mainHand, boolean keepNetworkId) {
        if (mainHand == null || mainHand.isEmpty()) {
            this.mainHand = null;
        } else {
            this.mainHand = keepNetworkId ? Item.getAirIfNull(mainHand).clone() : Item.getAirIfNull(mainHand).newNetworkCopy();
        }
        this.broadcastMobEquipmentPacket(this.getHeldItem(), 0, true); // TODO when entity support is implemented: check if entity supports armor before sending
    }

    @Override
    public Item getOffhandItem() {
        return this.getOffhandItem(true);
    }

    public Item getOffhandItem(boolean clone) {
        Item offhand = Optional.ofNullable(this.offHand).orElse(ItemRegistry.getInstance().getItem(BlockID.AIR));
        if (clone) {
            return offhand.clone();
        } else {
            return offhand;
        }
    }

    @Override
    public void setOffhandItem(Item offHand) {
        this.setOffhandItem(offHand, false);
    }

    public void setOffhandItem(Item offHand, boolean keepNetworkId) {
        if (offHand == null || offHand.isEmpty()) {
            this.offHand = null;
        } else {
            this.offHand = keepNetworkId ? Item.getAirIfNull(offHand).clone() : Item.getAirIfNull(offHand).newNetworkCopy();
        }
        this.broadcastMobEquipmentPacket(this.getHeldItem(), 1, false); // TODO when entity support is implemented: check if entity supports armor before sending
    }

    /**
     * Broadcasts mob equipment packet to all viewers of this entity.
     * @param item the item stack being sent
     * @param slot the slot to send it as
     * @param mainHand if the item is in the main hand
     */
    protected void broadcastMobEquipmentPacket(Item item, int slot, boolean mainHand) {
        for (Player player : this.getEntity().getViewers()) {
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.setRuntimeEntityId(this.getEntity().getId());
            mobEquipmentPacket.setContainerId(mainHand ? ContainerId.INVENTORY : ContainerId.OFFHAND);
            mobEquipmentPacket.setInventorySlot(slot);
            mobEquipmentPacket.setHotbarSlot(slot);
            mobEquipmentPacket.setItem(ItemUtils.serializeForNetwork(item, player.getVersion()));
            player.sendPacket(mobEquipmentPacket);
        }
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return this.getEntity().getViewers().contains(player) && super.canBeOpenedBy(player);
    }

    @Override
    public boolean shouldBeClosedFor(Player player) {
        return !player.canReach(this.getEntity()) && super.shouldBeClosedFor(player);
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setId((byte) this.getId());
        containerOpenPacket.setType(this.getContainerType());
        containerOpenPacket.setUniqueEntityId(this.getEntity().getId());
        player.sendPacket(containerOpenPacket);

        this.sendSlots(player);
    }

}
