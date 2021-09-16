package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

import io.github.willqi.pizzaserver.api.item.ItemStack;

import java.util.List;

/**
 * Used for server authoritative inventories
 * Should not be used.
 * Created after a player attempted to craft an item.
 */
public class InventoryActionCraftResultsDeprecated implements InventoryAction {

    private final List<ItemStack> createdItems;
    private final int timesCreated;


    public InventoryActionCraftResultsDeprecated(List<ItemStack> createdItems, int timesCreated) {
        this.createdItems = createdItems;
        this.timesCreated = timesCreated;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.CRAFT_RESULTS_DEPRECATED;
    }

    public List<ItemStack> getCreatedItems() {
        return this.createdItems;
    }

    public int getTimesCreated() {
        return this.timesCreated;
    }

}
