package io.github.pizzaserver.api.item.types.components;

import io.github.pizzaserver.api.item.ItemStack;

/**
 * Represents an item that is considered armor.
 */
public interface ArmorItemComponent extends DurableItemComponent {

    /**
     * Amount of armor points this item type provides when worn.
     * @return armor points this item type provides
     */
    default int getProtection() {
        return 0;
    }

    /**
     * Percentage of knockback resistance to apply.
     * This ranges from 0-1, 1 being maximum knockback resistance
     * @return percentage of knockback to ignore if this item is worn
     */
    default float getKnockbackResistance() {
        return 0;
    }

    /**
     * The possible items that can repair this item in an anvil.
     * @return items that can repair this item
     */
    default ItemStack[] getRepairItems() {
        return new ItemStack[0];
    }

}
