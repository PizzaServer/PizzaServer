package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

/**
 * Used in the ItemStackRequest and ItemStackResponse packets for server authoritative inventories.
 * Describes a slot action
 */
public interface InventoryAction {

    InventoryActionType getType();

}
