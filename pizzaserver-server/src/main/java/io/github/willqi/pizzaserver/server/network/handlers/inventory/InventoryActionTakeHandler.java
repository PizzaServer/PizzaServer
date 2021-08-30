package io.github.willqi.pizzaserver.server.network.handlers.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions.InventoryActionTake;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackResponsePacket;

public class InventoryActionTakeHandler extends InventoryActionHandler<InventoryActionTake> {

    public static final InventoryActionHandler<InventoryActionTake> INSTANCE = new InventoryActionTakeHandler();


    @Override
    public boolean isValid(Player player, InventoryActionTake action) {
        return stackExists(player, action.getSource()) &&
                stackExists(player, action.getDestination()) &&
                (action.getDestination().getInventorySlotType() == InventorySlotType.CURSOR) &&
                player.getInventory().getCursor().getItemType().getItemId().equals(BlockTypeID.AIR) &&
                action.getCount() > 0;
    }

    @Override
    public boolean handle(ItemStackResponsePacket.Response response, Player player, InventoryActionTake action) {
        SlotLocation source = new SlotLocation(response, player, action.getSource());
        SlotLocation destination = new SlotLocation(response, player, action.getDestination());

        int preSourceStackCount = source.getItem().getCount();
        int pickedUpStackCount = Math.min(preSourceStackCount, action.getCount());

        // Get a new item stack for the amount they requested to pick up
        ItemStack pickedUpStack = source.getItem().newNetworkStack();
        pickedUpStack.setCount(pickedUpStackCount);

        // Change the existing item stack to get rid of the picked up item count
        source.getItem().setCount(preSourceStackCount - pickedUpStackCount);

        destination.setItem(pickedUpStack);
        source.setItem(source.getItem());
        return true;
    }

}
