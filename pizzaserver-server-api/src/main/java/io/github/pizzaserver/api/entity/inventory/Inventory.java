package io.github.pizzaserver.api.entity.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.Watchable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface Inventory extends Watchable {

    int getId();

    ContainerType getContainerType();

    int getSize();

    /**
     * Clear the inventory.
     */
    void clear();

    /**
     * Returns a copy of the slots of this inventory.
     * @return clone of the slots
     */
    Item[] getSlots();

    /**
     * Returns a copy of the slot in this inventory.
     * @param slot the slot
     * @return clone of the slot
     */
    Item getSlot(int slot);

    /**
     * Change all the slots in this inventory.
     * The slots provided must be the same size as the existing slots
     * items will be cloned when setting
     * @param slots new slots
     */
    void setSlots(Item[] slots);

    /**
     * Change a single slot.
     * @param slot the slot to change
     * @param item the item to place in it (this will be cloned)
     */
    void setSlot(int slot, Item item);

    /**
     * Check if the inventory contains the item id provided.
     * @param itemId item id
     * @return if the inventory contains the item id.
     */
    boolean contains(String itemId);

    /**
     * Check if the inventory contains the item provided.
     * This will take into consideration the size of the stack.
     * @param item item
     * @return if the inventory contains the item
     */
    boolean contains(Item item);

    /**
     * Try to add an item to the inventory.
     * It will attempt to merge itself with any existing items that can merge
     * with this item. If it cannot fullly merge, it will return whatever could not be merged.
     * @param item item to add
     * @return remainder of the item if any exists
     */
    Optional<Item> addItem(Item item);

    /**
     * Try to add items to this inventory.
     * It will attempt to merge itself with any existing items that can merge
     * with this items. If it cannot fully merge, it will return whatever could not be merged.
     * @param items items to add
     * @return remainder of the items that could not be added
     */
    Set<Item> addItems(Item... items);

    /**
     * Try to add items to this inventory.
     * It will attempt to merge itself with any existing items that can merge
     * with this item. If it cannot fully merge, it will return whatever could not be merged.
     * @param items items to add
     * @return remainder of the items that could not be added
     */
    Set<Item> addItems(Collection<Item> items);

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
     * @param item item to find the excess of
     * @return excess amount
     */
    int getExcessIfAdded(Item item);
}
