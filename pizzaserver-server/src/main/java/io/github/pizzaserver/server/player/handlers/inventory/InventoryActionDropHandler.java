package io.github.pizzaserver.server.player.handlers.inventory;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.network.data.inventory.actions.DropStackRequestActionDataWrapper;
import io.github.pizzaserver.server.player.ImplPlayer;

public class InventoryActionDropHandler extends InventoryActionHandler<DropStackRequestActionDataWrapper> {

    public static final InventoryActionHandler<DropStackRequestActionDataWrapper> INSTANCE = new InventoryActionDropHandler();


    @Override
    protected boolean isValid(Player player, DropStackRequestActionDataWrapper action) {
        InventorySlotContainer sourceSlot = action.getSource();
        return sourceSlot.exists() && action.getCountRequested() > 0;
    }

    @Override
    protected boolean runAction(Player player, DropStackRequestActionDataWrapper action) {
        InventorySlotContainer sourceSlot = action.getSource();

        Item droppingItem = sourceSlot.getItemStack();
        int amountDropped = Math.min(droppingItem.getCount(), action.getCountRequested());

        Item droppedItem = droppingItem.newNetworkCopy();
        droppedItem.setCount(amountDropped);

        if (((ImplPlayer) player).tryDroppingItem(sourceSlot.getInventory(), droppedItem)) {
            droppingItem.setCount(droppingItem.getCount() - amountDropped);
            action.getSource().setItemStack(droppingItem);
        }
        return true;
    }

}
