package io.github.pizzaserver.server.player.handlers.inventory;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.network.data.inventory.actions.CraftCreativeRequestActionDataWrapper;

public class InventoryActionCraftCreativeHandler extends InventoryActionHandler<CraftCreativeRequestActionDataWrapper> {

    public static final InventoryActionHandler<CraftCreativeRequestActionDataWrapper> INSTANCE = new InventoryActionCraftCreativeHandler();

    @Override
    protected boolean isValid(Player player, CraftCreativeRequestActionDataWrapper action) {
        return !action.getItem().isEmpty() && player.isCreativeMode();
    }

    @Override
    protected boolean runAction(Player player, CraftCreativeRequestActionDataWrapper action) {
        // Items picked out of the creative menu are always set to the max count
        Item creativeSlotItem = action.getItem();
        creativeSlotItem.setCount(creativeSlotItem.getMaxStackSize());

        action.getDestination().setItemStack(creativeSlotItem);
        return true;
    }

}
