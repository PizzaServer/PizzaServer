package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative.actions;

import io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative.AuthoritativeInventorySlot;

/**
 * Used for server authoritative inventories
 * Created for the items used up when a player crafts something (e.g. the wood from crafting planks)
 */
public class InventoryActionConsume implements InventoryAction {

    private final AuthoritativeInventorySlot source;
    private final int amount;


    public InventoryActionConsume(AuthoritativeInventorySlot source, int amount) {
        this.source = source;
        this.amount = amount;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.CONSUME;
    }

    public AuthoritativeInventorySlot getSlot() {
        return this.source;
    }

    public int getAmount() {
        return this.amount;
    }

}
