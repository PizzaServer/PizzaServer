package io.github.willqi.pizzaserver.api.entity.inventory;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Watchable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface EntityInventory extends Watchable {

    int getId();

    /**
     * Get the entity who owns this inventory
     * @return the entity who owns this inventory
     */
    Entity getEntity();

    int getSize();

    ItemStack[] getSlots();

    ItemStack getSlot(int slot);

    /**
     * Change all the slots in this inventory
     * The slots provided must be the same size as the existing slots
     * @param slots new slots
     * @returns if the slots were changed
     */
    boolean setSlots(ItemStack[] slots);

    /**
     * Change a single slot
     * @param slot the slot to change
     * @param itemStack the item to place in it
     * @return if the slot was changed
     */
    boolean setSlot(int slot, ItemStack itemStack);

    /**
     * Try to add an item to the inventory.
     * It will attempt to merge itself with any existing item stacks that can merge
     * with this item stack. If it cannot fullly merge, it will return whatever could not be merged.
     * @param itemStack ItemStack to add
     * @return remainder of the item stack if any exists
     */
    Optional<ItemStack> addItem(ItemStack itemStack);

    /**
     * Try to add items to this inventory.
     * It will attempt to merge itself with any existing item stacks that can merge
     * with this item stack. If it cannot fully merge, it will return whatever could not be merged.
     * @param itemStacks ItemStacks to add
     * @return remainder of the item stacks that could not be added
     */
    Set<ItemStack> addItems(ItemStack ...itemStacks);

    /**
     * Try to add items to this inventory.
     * It will attempt to merge itself with any existing item stacks that can merge
     * with this item stack. If it cannot fully merge, it will return whatever could not be merged.
     * @param itemStacks ItemStacks to add
     * @return remainder of the item stacks that could not be added
     */
    Set<ItemStack> addItems(Collection<ItemStack> itemStacks);

    /**
     * Send this inventory to a player
     * @param player the player to send this inventory to
     */
    void sendSlots(Player player);

    /**
     * Show this inventory to a player
     * @param player the player
     * @return if the inventory was opened
     */
    boolean openFor(Player player);

    /**
     * Close this inventory for a player
     * @param player the player
     * @return if the inventory was closed
     */
    boolean closeFor(Player player);

}
