package io.github.pizzaserver.server.network.handlers.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionType;
import io.github.pizzaserver.api.event.type.inventory.InventoryMoveItemEvent;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.types.ItemType;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.network.data.inventory.actions.PlaceStackRequestActionDataWrapper;

public class InventoryActionPlaceHandler extends InventoryActionHandler<PlaceStackRequestActionDataWrapper> {

    public static final InventoryActionHandler<PlaceStackRequestActionDataWrapper> INSTANCE = new InventoryActionPlaceHandler();


    @Override
    public boolean isValid(Player player, PlaceStackRequestActionDataWrapper action) {

        if (action.getSource().exists() && action.getDestination().exists()) {
            ItemStack sourceItemStack = action.getSource().getItemStack();
            ItemStack destinationItemStack = action.getDestination().getItemStack();

            // the slots must be either of the same type or the destination has to be air
            boolean canMergeItemData = (sourceItemStack.getItemType().equals(destinationItemStack.getItemType())
                    && sourceItemStack.getCompoundTag().equals(destinationItemStack.getCompoundTag())
                    && sourceItemStack.getDamage() == destinationItemStack.getDamage()) || destinationItemStack.isEmpty();

            // final destination slot cannot exceed max count
            boolean doesNotExceedMaxCount = action.getCountRequested() > 0
                    && destinationItemStack.getCount() + action.getCountRequested() <= sourceItemStack.getItemType().getMaxStackSize();

            boolean canPutItemTypeInSlot = ItemType.canBePlacedInSlot(sourceItemStack.getItemType(), action.getDestination().getSlotType())
                    && action.getDestination().getSlotType() != ContainerSlotType.CURSOR;

            return canMergeItemData && doesNotExceedMaxCount && canPutItemTypeInSlot;
        } else {
            return false;
        }
    }

    @Override
    public boolean runAction(Player player, PlaceStackRequestActionDataWrapper action) {
        InventorySlotContainer source = action.getSource();
        InventorySlotContainer destination = action.getDestination();

        int sourceStackCount = source.getItemStack().getCount();
        int playerRequestedAmount = Math.min(action.getCountRequested(), sourceStackCount);

        // Create new stack with the amount that will be placed down + existing destination count
        int placedStackAmount = playerRequestedAmount + destination.getItemStack().getCount();
        ItemStack placedStack = new ItemStack(source.getItemStack().getItemType(), placedStackAmount, source.getItemStack().getDamage()).newNetworkStack();
        placedStack.setCompoundTag(source.getItemStack().getCompoundTag());

        // Remove the amount placed from our source
        ItemStack newSourceStack = source.getItemStack().clone();
        newSourceStack.setCount(sourceStackCount - playerRequestedAmount);

        // Call the event
        InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(player,
                StackRequestActionType.PLACE,
                source.getInventory(),
                source.getSlotType(),
                source.getSlot(),
                source.getItemStack(),
                playerRequestedAmount,
                destination.getInventory(),
                destination.getSlotType(),
                destination.getSlot(),
                destination.getItemStack());
        player.getServer().getEventManager().call(inventoryMoveItemEvent);
        if (inventoryMoveItemEvent.isCancelled()) {
            return false;
        }

        destination.setItemStack(placedStack);
        source.setItemStack(newSourceStack);
        return true;
    }

}
