package io.github.pizzaserver.server.player.handlers.inventory;

import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.event.type.inventory.InventoryDropItemEvent;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.network.data.inventory.actions.DropStackRequestActionDataWrapper;

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

        InventoryDropItemEvent dropItemEvent = new InventoryDropItemEvent(sourceSlot.getInventory(), player, droppedItem);
        player.getServer().getEventManager().call(dropItemEvent);
        if (dropItemEvent.isCancelled()) {
            return false;
        }

        droppingItem.setCount(droppingItem.getCount() - amountDropped);
        action.getSource().setItemStack(droppingItem);

        // Drop item
        EntityItem itemEntity = EntityRegistry.getInstance().getItemEntity(droppedItem);
        itemEntity.setPickupDelay(40);
        player.getWorld().addItemEntity(itemEntity, player.getLocation().toVector3f().add(0, 1.3f, 0), player.getDirectionVector().mul(0.25f, 0.6f, 0.25f));
        return true;
    }

}
