package io.github.willqi.pizzaserver.server.network.handlers.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions.InventoryActionPlace;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackResponsePacket;

import java.util.Optional;

public class InventoryActionPlaceHandler extends InventoryActionHandler<InventoryActionPlace> {

    public static final InventoryActionHandler<InventoryActionPlace> INSTANCE = new InventoryActionPlaceHandler();


    @Override
    public boolean isValid(Player player, InventoryActionPlace action) {
        Optional<ItemStack> source = getItemStack(player, action.getSource());
        Optional<ItemStack> destination = getItemStack(player, action.getDestination());

        if (source.isPresent() && destination.isPresent()) {
            ItemStack sourceItemStack = source.get();
            ItemStack destinationItemStack = destination.get();

            boolean canMergeItemData = (sourceItemStack.getItemType().equals(destinationItemStack.getItemType()) &&
                                            sourceItemStack.getCompoundTag().equals(destinationItemStack.getCompoundTag()) &&
                                            sourceItemStack.getDamage() == destinationItemStack.getDamage()) || destinationItemStack.getItemType().getItemId().equals(BlockTypeID.AIR);

            boolean doesNotExceedMaxCount = action.getCount() > 0 &&
                                                destinationItemStack.getCount() + action.getCount() <= sourceItemStack.getItemType().getMaxStackSize();

            boolean canPutItemTypeInSlot = canPutItemTypeInSlot(sourceItemStack.getItemType(), action.getDestination().getInventorySlotType()) &&
                                                action.getDestination().getInventorySlotType() != InventorySlotType.CURSOR;

            return canMergeItemData && doesNotExceedMaxCount && canPutItemTypeInSlot;
        } else {
            return false;
        }
    }

    @Override
    public boolean handle(ItemStackResponsePacket.Response response, Player player, InventoryActionPlace action) {
        SlotLocation source = new SlotLocation(response, player, action.getSource());
        SlotLocation destination = new SlotLocation(response, player, action.getDestination());

        int sourceStackCount = source.getItem().getCount();
        int playerRequestedAmount = Math.min(action.getCount(), sourceStackCount);

        int placedStackAmount = destination.getItem().getCount() + playerRequestedAmount;
        ItemStack placedStack = new ItemStack(source.getItem().getItemType(), placedStackAmount, source.getItem().getDamage()).newNetworkStack();
        destination.setItem(placedStack);

        source.getItem().setCount(sourceStackCount - playerRequestedAmount);
        source.setItem(source.getItem());
        return true;
    }

}
