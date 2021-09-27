package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent by the server to update a single slot in a inventory the player has open.
 */
public class InventorySlotPacket extends BaseBedrockPacket {

    public static final int ID = 0x32;

    private int inventoryId;
    private int slot;
    private ItemStack item;


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

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack data) {
        this.item = data;
    }

}
