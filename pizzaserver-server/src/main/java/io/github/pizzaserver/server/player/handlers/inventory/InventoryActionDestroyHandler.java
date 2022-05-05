package io.github.pizzaserver.server.player.handlers.inventory;

import io.github.pizzaserver.api.event.type.inventory.InventoryCreativeDestructionEvent;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.network.data.inventory.actions.DestroyStackRequestActionDataWrapper;

public class InventoryActionDestroyHandler extends InventoryActionHandler<DestroyStackRequestActionDataWrapper> {

    public static final InventoryActionHandler<DestroyStackRequestActionDataWrapper> INSTANCE = new InventoryActionDestroyHandler();

    @Override
    protected boolean isValid(Player player, DestroyStackRequestActionDataWrapper action) {
        InventorySlotContainer sourceSlot = action.getSource();
        return sourceSlot.exists() && action.getCountRequested() > 0 && player.isCreativeMode();
    }

    @Override
    protected boolean runAction(Player player, DestroyStackRequestActionDataWrapper action) {
        InventorySlotContainer sourceSlot = action.getSource();

        Item droppingItem = sourceSlot.getItemStack();
        int amountDropped = Math.min(droppingItem.getCount(), action.getCountRequested());

        Item droppedItem = droppingItem.clone();
        droppedItem.setCount(amountDropped);

        InventoryCreativeDestructionEvent destroyItemEvent = new InventoryCreativeDestructionEvent(player,
                sourceSlot.getInventory(),
                action.getSource().getSlotType(),
                action.getSource().getNetworkSlot(),
                droppingItem,
                droppedItem);
        player.getServer().getEventManager().call(destroyItemEvent);
        if (destroyItemEvent.isCancelled()) {
            return false;
        }

        droppingItem.setCount(droppingItem.getCount() - amountDropped);
        action.getSource().setItemStack(droppingItem);
        return true;
    }

}
