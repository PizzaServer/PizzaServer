package io.github.willqi.pizzaserver.server.network.handlers.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.event.type.inventory.InventoryMoveItemEvent;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryActionTake;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackResponsePacket;

import java.util.Optional;

public class InventoryActionTakeHandler extends InventoryActionHandler<InventoryActionTake> {

    public static final InventoryActionHandler<InventoryActionTake> INSTANCE = new InventoryActionTakeHandler();


    @Override
    public boolean isValid(Player player, InventoryActionTake action) {
        Optional<ItemStack> sourceStack = getItemStack(player, action.getSource());
        Optional<ItemStack> destinationStack = getItemStack(player, action.getDestination());

        // Validate the slots exist and that the player is requesting a valid slot/amount
        boolean valid = sourceStack.isPresent() &&
                destinationStack.isPresent() &&
                action.getDestination().getInventorySlotType() == InventorySlotType.CURSOR &&
                action.getCount() > 0;
        if (valid) {
            // (Verify that either the destination is air or the source can be merged with the destination)
            boolean underStackLimit = destinationStack.get().getCount() + Math.min(action.getCount(), sourceStack.get().getCount()) <= sourceStack.get().getItemType().getMaxStackSize();
            boolean destinationIsAir = destinationStack.get().getItemType().getItemId().equals(BlockTypeID.AIR);
            boolean canAddSourceToStack = destinationStack.get().hasSameDataAs(sourceStack.get()) || destinationIsAir;

            return underStackLimit && canAddSourceToStack;
        } else {
            return false;
        }
    }

    @Override
    public boolean runAction(ItemStackResponsePacket.Response response, Player player, InventoryActionTake action) {
        SlotLocation source = new SlotLocation(response, player, action.getSource());
        SlotLocation destination = new SlotLocation(response, player, action.getDestination());

        int sourceStackCount = source.getItem().getCount();
        int pickedUpStackCount = Math.min(sourceStackCount, action.getCount());

        // Get a new item stack with the amount they picked up
        ItemStack pickedUpStack;
        if (destination.getItem().hasSameDataAs(source.getItem())) {
            // matching item in cursor: add to existing cursor (double-clicking an item stack to combine all common item stacks to one)
            pickedUpStack = destination.getItem();
            pickedUpStack.setCount(pickedUpStackCount + destination.getItem().getCount());
        } else {
            // air in cursor atm: new itemstack
            pickedUpStack = source.getItem().newNetworkStack();
            pickedUpStack.setCount(pickedUpStackCount);
        }

        InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(player,
                InventoryMoveItemEvent.Action.TAKE,
                source.getInventory(),
                action.getSource().getInventorySlotType(),
                action.getSource().getSlot(),
                source.getItem(),
                pickedUpStackCount,
                destination.getInventory(),
                action.getDestination().getInventorySlotType(),
                action.getDestination().getSlot(),
                destination.getItem());
        player.getServer().getEventManager().call(inventoryMoveItemEvent);
        if (inventoryMoveItemEvent.isCancelled()) {
            return false;
        }

        // Change the existing item stack to get rid of the picked up item count
        source.getItem().setCount(sourceStackCount - pickedUpStackCount);

        destination.setItem(pickedUpStack);
        source.setItem(source.getItem());
        return true;
    }

}
