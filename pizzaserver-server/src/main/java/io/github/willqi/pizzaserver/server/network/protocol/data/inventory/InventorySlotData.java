package io.github.willqi.pizzaserver.server.network.protocol.data.inventory;

/**
 * Used for server authoritative inventories
 * Represents a slot
 */
public class InventorySlotData {

    private final InventorySlotType inventorySlotType;
    private final int slot;
    private final int networkStackId;


    public InventorySlotData(InventorySlotType slotType, int slot, int networkStackId) {
        this.inventorySlotType = slotType;
        this.slot = slot;
        this.networkStackId = networkStackId;
    }

    public InventorySlotType getInventorySlotType() {
        return this.inventorySlotType;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getNetworkStackId() {
        return this.networkStackId;
    }

}
