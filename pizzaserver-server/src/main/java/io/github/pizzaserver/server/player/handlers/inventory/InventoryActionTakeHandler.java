package io.github.pizzaserver.server.player.handlers.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionType;
import io.github.pizzaserver.api.event.type.inventory.InventoryMoveItemEvent;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.network.data.inventory.actions.TakeStackRequestActionDataWrapper;

public class InventoryActionTakeHandler extends InventoryActionHandler<TakeStackRequestActionDataWrapper> {

    public static final InventoryActionHandler<TakeStackRequestActionDataWrapper> INSTANCE = new InventoryActionTakeHandler();


    @Override
    protected boolean isValid(Player player, TakeStackRequestActionDataWrapper action) {
        InventorySlotContainer sourceSlot = action.getSource();
        InventorySlotContainer destinationSlot = action.getDestination();

        // Validate the slots exist and that the player is requesting a valid slot/amount
        boolean slotsValid = sourceSlot.exists()
                && destinationSlot.exists()
                && destinationSlot.getSlotType() == ContainerSlotType.CURSOR
                && action.getCountRequested() > 0;
        if (slotsValid) {
            // (Verify that either the destination is air or the source can be merged with the destination)
            int destinationSlotCount = destinationSlot.getItemStack().getCount();
            int sourceSlotCount = sourceSlot.getItemStack().getCount();
            int maxStackCountAllowed = sourceSlot.getItemStack().getMaxStackSize();

            boolean underStackLimit = destinationSlotCount + Math.min(action.getCountRequested(), sourceSlotCount) <= maxStackCountAllowed;
            boolean destinationIsAir = destinationSlot.getItemStack().isEmpty();
            boolean canAddSourceToStack = destinationSlot.getItemStack().hasSameDataAs(sourceSlot.getItemStack()) || destinationIsAir;

            return underStackLimit && canAddSourceToStack;
        } else {
            return false;
        }
    }

    @Override
    protected boolean runAction(Player player, TakeStackRequestActionDataWrapper action) {
        InventorySlotContainer source = action.getSource();
        InventorySlotContainer destination = action.getDestination();

        // Retrieve actual amount picked up
        int sourceStackCount = source.getItemStack().getCount();
        int pickedUpStackCount = Math.min(sourceStackCount, action.getCountRequested());


        // Get a new item stack with the amount they picked up
        Item pickedUpStack;
        if (destination.getItemStack().hasSameDataAs(source.getItemStack())) {
            // matching item in cursor: add to existing cursor (double-clicking an item stack to combine all common item stacks to one)
            pickedUpStack = destination.getItemStack();
            pickedUpStack.setCount(pickedUpStackCount + destination.getItemStack().getCount());
        } else {
            // air in cursor atm: new itemstack
            pickedUpStack = source.getItemStack().newNetworkCopy();
            pickedUpStack.setCount(pickedUpStackCount);
        }

        InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(player,
                StackRequestActionType.TAKE,
                source.getInventory(),
                action.getSource().getSlotType(),
                action.getSource().getNetworkSlot(),
                source.getItemStack(),
                pickedUpStackCount,
                destination.getInventory(),
                action.getDestination().getSlotType(),
                action.getDestination().getNetworkSlot(),
                destination.getItemStack());
        player.getServer().getEventManager().call(inventoryMoveItemEvent);
        if (inventoryMoveItemEvent.isCancelled()) {
            return false;
        }

        // Change the existing item stack to get rid of the picked up item count
        Item currentStack = source.getItemStack();
        currentStack.setCount(sourceStackCount - pickedUpStackCount);

        destination.setItemStack(pickedUpStack);
        source.setItemStack(currentStack);
        return true;
    }

}
