package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.data.NetworkItemStackData;

/**
 * Sent by the client when it changes the item being held
 * Sent by the server when it wants to change the item being held by an entity
 */
public class MobEquipmentPacket extends BaseBedrockPacket {

    public static final int ID = 0x1f;

    private long entityRuntimeId;

    private int inventoryId;
    private NetworkItemStackData networkItemStackData;
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

    public NetworkItemStackData getNetworkItemStackData() {
        return this.networkItemStackData;
    }

    public void setNetworkItemStackData(NetworkItemStackData networkItemStackData) {
        this.networkItemStackData = networkItemStackData;
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
