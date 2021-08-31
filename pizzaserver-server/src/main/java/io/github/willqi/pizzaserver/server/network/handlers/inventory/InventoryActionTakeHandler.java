package io.github.willqi.pizzaserver.server.network.handlers.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions.InventoryActionTake;
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
                (action.getDestination().getInventorySlotType() == InventorySlotType.CURSOR) &&
                action.getCount() > 0;
        if (valid) {
            // Verify that the source can be added to the destination.
            // (either the destination is air or the source can be merged with the destination)
            boolean underStackLimit = destinationStack.get().getCount() + sourceStack.get().getCount() < destinationStack.get().getItemType().getMaxStackSize();
            boolean canAddSourceToStack = destinationStack.get().hasSameDataAs(sourceStack.get()) && underStackLimit;

            return destinationStack.get().getItemType().getItemId().equals(BlockTypeID.AIR) || canAddSourceToStack;
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
        ItemStack pickedUpStack = source.getItem().newNetworkStack();
        if (destination.getItem().hasSameDataAs(pickedUpStack)) {
            pickedUpStack.setCount(pickedUpStackCount + destination.getItem().getCount());
        } else {
            pickedUpStack.setCount(pickedUpStackCount);
        }

        // Change the existing item stack to get rid of the picked up item count
        source.getItem().setCount(sourceStackCount - pickedUpStackCount);

        destination.setItem(pickedUpStack);
        source.setItem(source.getItem());
        return true;
    }

}
