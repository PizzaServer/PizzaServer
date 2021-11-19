package io.github.pizzaserver.api.entity.inventory;

import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.utils.Watchable;
import io.github.pizzaserver.api.player.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface Inventory extends Watchable {

    int getId();

    /**
     * Get all slot types this inventory has.
     * @return all possible slot types this inventory has
     */
    Set<InventorySlotType> getSlotTypes();

    int getSize();

    /**
     * Clear the inventory.
     */
    void clear();

    /**
     * Returns a copy of the slots of this inventory.
     * @return clone of the slots
     */
    ItemStack[] getSlots();

    /**
     * Returns a copy of the slot in this inventory.
     * @param slot the slot
     * @return clone of the slot
     */
    ItemStack getSlot(int slot);

    /**
     * Change all the slots in this inventory.
     * The slots provided must be the same size as the existing slots
     * ItemStacks will be cloned when setting
     * @param slots new slots
     */
    void setSlots(ItemStack[] slots);

    /**
     * Change a single slot.
     * @param slot the slot to change
     * @param itemStack the item to place in it (this will be cloned)
     */
    void setSlot(int slot, ItemStack itemStack);

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
     * Send this inventory to a player.
     * @param player the player to send this inventory to
     */
    void sendSlots(Player player);

    /**
     * Send a specific slot of this inventory to a player.
     * @param player the player to send the slot to
     * @param slot the slot
     */
    void sendSlot(Player player, int slot);

    /**
     * Check if this player has permission to open this inventory by themselves.
     * @param player if the player has permission to open this inventory
     * @return if the player has permission to open the inventory by themselves
     */
    boolean canBeOpenedBy(Player player);

    /**
     * Check how many items in the stack provided will be left over if this item were to be added to the entity inventory.
     * @param itemStack item stack to add
     * @return excess amount
     */
    int getExcessIfAdded(ItemStack itemStack);

}
