package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative;

import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;

/**
 * Used for server authoritative inventories.
 * Represents a slot
 */
public class AuthoritativeInventorySlot {

    private final InventorySlotType inventorySlotType;
    private final int slot;
    private final int networkStackId;


    public AuthoritativeInventorySlot(InventorySlotType slotType, int slot, int networkStackId) {
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
