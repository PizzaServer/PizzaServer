package io.github.pizzaserver.server.network.handlers.inventory;

import io.github.pizzaserver.api.event.type.inventory.InventoryMoveItemEvent;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.player.Player;
import java.util.Optional;

public class InventoryActionSwapHandler extends InventoryActionHandler<InventoryActionSwap> {

    public static final InventoryActionHandler<InventoryActionSwap> INSTANCE = new InventoryActionSwapHandler();


    @Override
    public boolean isValid(Player player, InventoryActionSwap action) {
        Optional<ItemStack> sourceItemStack = getItemStack(player, action.getSource());
        Optional<ItemStack> destinationItemStack = getItemStack(player, action.getDestination());
        if (sourceItemStack.isPresent() && destinationItemStack.isPresent()) {
            // Ensure that the source and destination items are allowed in their new slots
            return canPutItemTypeInSlot(sourceItemStack.get().getItemType(), action.getDestination().getInventorySlotType())
                    && canPutItemTypeInSlot(destinationItemStack.get().getItemType(), action.getSource().getInventorySlotType());
        } else {
            return false;
        }
    }

    @Override
    public boolean runAction(ItemStackResponsePacket.Response response, Player player, InventoryActionSwap action) {
        SlotLocation source = new SlotLocation(response, player, action.getSource());
        SlotLocation destination = new SlotLocation(response, player, action.getDestination());

        // Call the event
        InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(player,
                InventoryMoveItemEvent.Action.SWAP,
                source.getInventory(),
                action.getSource().getInventorySlotType(),
                action.getSource().getSlot(),
                source.getItem(),
                source.getItem().getCount(),
                destination.getInventory(),
                action.getDestination().getInventorySlotType(),
                action.getDestination().getSlot(),
                destination.getItem());
        player.getServer().getEventManager().call(inventoryMoveItemEvent);
        if (inventoryMoveItemEvent.isCancelled()) {
            return false;
        }

        // Swap item stacks
        ItemStack originalSourceItemStack = source.getItem();
        source.setItem(destination.getItem());
        destination.setItem(originalSourceItemStack);

        return true;
    }

}
