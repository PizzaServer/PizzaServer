package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent to update an inventory for the player
 */
public class InventoryContentPacket extends BaseBedrockPacket {

    public static final int ID = 0x31;

    private int inventoryId;
    private ItemStack[] contents;


    public InventoryContentPacket() {
        super(ID);
    }

    public int getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    /**
     * The contents of this list are equal to the size of the container
     * @return contents
     */
    public ItemStack[] getContents() {
        return this.contents;
    }

    /**
     * Set the contents of the inventory
     * The size of this list is equal to the size of the container
     * @param contents contents
     */
    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

}
