package io.github.pizzaserver.server.network.handlers.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionType;
import io.github.pizzaserver.api.event.type.inventory.InventoryMoveItemEvent;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.types.ItemType;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.network.data.inventory.actions.SwapStackRequestActionDataWrapper;

public class InventoryActionSwapHandler extends InventoryActionHandler<SwapStackRequestActionDataWrapper> {

    public static final InventoryActionHandler<SwapStackRequestActionDataWrapper> INSTANCE = new InventoryActionSwapHandler();


    @Override
    public boolean isValid(Player player, SwapStackRequestActionDataWrapper action) {
        if (action.getSource().exists() && action.getDestination().exists()) {
            // Ensure that the source and destination items are allowed in their new slots
            return ItemType.canBePlacedInSlot(action.getSource().getItemStack().getItemType(), action.getDestination().getSlotType(), action.getDestination().getSlot())
                    && ItemType.canBePlacedInSlot(action.getDestination().getItemStack().getItemType(), action.getSource().getSlotType(), action.getDestination().getSlot());
        } else {
            return false;
        }
    }

    @Override
    public boolean runAction(Player player, SwapStackRequestActionDataWrapper action) {
        InventorySlotContainer source = action.getSource();
        InventorySlotContainer destination = action.getDestination();

        // Call the event
        InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(player,
                StackRequestActionType.SWAP,
                source.getInventory(),
                action.getSource().getSlotType(),
                action.getSource().getSlot(),
                source.getItemStack(),
                source.getItemStack().getCount(),
                destination.getInventory(),
                action.getDestination().getSlotType(),
                action.getDestination().getSlot(),
                destination.getItemStack());
        player.getServer().getEventManager().call(inventoryMoveItemEvent);
        if (inventoryMoveItemEvent.isCancelled()) {
            return false;
        }

        // Swap item stacks
        ItemStack originalSourceItemStack = source.getItemStack();
        source.setItemStack(destination.getItemStack());
        destination.setItemStack(originalSourceItemStack);

        return true;
    }

}
