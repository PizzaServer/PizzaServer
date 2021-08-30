package io.github.willqi.pizzaserver.server.network.protocol.data.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;

/**
 * Used for server authoritative inventories
 * Represents a slot
 */
public class InventorySlot {

    private final InventorySlotType inventorySlotType;
    private final int slot;
    private final int networkStackId;


    public InventorySlot(InventorySlotType slotType, int slot, int networkStackId) {
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
