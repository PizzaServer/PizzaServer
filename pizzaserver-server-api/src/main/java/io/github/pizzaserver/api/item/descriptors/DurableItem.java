package io.github.pizzaserver.api.item.descriptors;

import io.github.pizzaserver.api.item.Item;

/**
 * Represents an item that has durability.
 */
public interface DurableItem extends Item {

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

    default void useDurability() {
        if (this.getMaxDurability() != -1) {
            int damage = this.getNBT().getInt("Damage");
            this.setNBT(this.getNBT()
                    .toBuilder()
                    .putInt("Damage", damage + 1)
                    .build());

            if (damage > this.getMaxDurability()) {
                this.setCount(0);
            }
        }
    }

}
