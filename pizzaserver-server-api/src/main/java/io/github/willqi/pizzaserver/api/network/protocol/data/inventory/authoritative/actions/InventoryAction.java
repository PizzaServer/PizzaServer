package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative.actions;

/**
 * Used in the ItemStackRequest and ItemStackResponse packets for server authoritative inventories.
 * Describes a slot action
 */
public interface InventoryAction {

    InventoryActionType getType();

}
