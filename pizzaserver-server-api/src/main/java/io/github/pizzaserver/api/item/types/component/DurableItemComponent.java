package io.github.pizzaserver.api.item.types.component;

/**
 * Represents an item that has durability.
 */
public interface DurableItemComponent {

    /**
     * Max durability of this item type.
     * Returning -1 means this item has infinite durability.
     * @return max durability
     */
    default int getMaxDurability() {
        return -1;
    }

    default boolean isRepairable() {
        return false;
    }

}
