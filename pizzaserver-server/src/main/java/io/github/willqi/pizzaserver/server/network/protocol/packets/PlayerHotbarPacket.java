package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent by the server to change the hotbar position of the player
 */
public class PlayerHotbarPacket extends BaseBedrockPacket {

    public static final int ID = 0x30;

    private int inventoryId;
    private int hotbarSlot;
    private boolean selectSlot;


    public PlayerHotbarPacket() {
        super(ID);
    }

    public int getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    public void setHotbarSlot(int slot) {
        this.hotbarSlot = slot;
    }

    public boolean shouldSelectSlot() {
        return this.selectSlot;
    }

    public void setShouldSelectSlot(boolean selectSlot) {
        this.selectSlot = selectSlot;
    }

}
