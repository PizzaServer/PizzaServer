package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions;

/**
 * Used for server authoritative inventories
 * Created for inventory actions that are not yet supported by the server authoritative inventory system
 */
public class InventoryActionNotImplemented implements InventoryAction {

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.CRAFT_NOT_IMPLEMENTED;
    }

}
