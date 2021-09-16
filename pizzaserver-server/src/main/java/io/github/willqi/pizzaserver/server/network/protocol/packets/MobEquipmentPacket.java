package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent by the client when it changes the item being held
 * Sent by the server when it wants to change the item being held by an entity
 */
public class MobEquipmentPacket extends BaseBedrockPacket {

    public static final int ID = 0x1f;

    private long entityRuntimeId;

    private int inventoryId;
    private ItemStack equipment;
    private int slot;
    private int hotbarSlot;


    public MobEquipmentPacket() {
        super(ID);
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public void setEntityRuntimeId(long id) {
        this.entityRuntimeId = id;
    }

    public int getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(int id) {
        this.inventoryId = id;
    }

    public ItemStack getEquipment() {
        return this.equipment;
    }

    public void setEquipment(ItemStack equipment) {
        this.equipment = equipment;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    public void setHotbarSlot(int hotbarSlot) {
        this.hotbarSlot = hotbarSlot;
    }

}
