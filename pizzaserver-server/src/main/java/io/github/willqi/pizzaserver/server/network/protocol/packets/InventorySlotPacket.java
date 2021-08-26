package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.data.NetworkItemStackData;

/**
 * Sent by the server to update a single slot in a inventory the player has open
 */
public class InventorySlotPacket extends BaseBedrockPacket {

    public static final int ID = 0x32;

    private int inventoryId;
    private int slot;
    private NetworkItemStackData itemStackData;


    public InventorySlotPacket() {
        super(ID);
    }

    public int getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(int id) {
        this.inventoryId = id;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public NetworkItemStackData getItemStackData() {
        return this.itemStackData;
    }

    public void setItemStackData(NetworkItemStackData data) {
        this.itemStackData = data;
    }

}
