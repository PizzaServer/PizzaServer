package io.github.pizzaserver.api.item.types.components;

import io.github.pizzaserver.api.item.ItemStack;

/**
 * Represents an item that can be eaten.
 */
public interface FoodItemComponent {

    /**
     * Retrieve the amount of nutrition this item provides the player when eaten.
     * @return nutrition points
     */
    int getNutrition();

    /**
     * Retrieve the amount of saturation to give to the player when eaten.
     * @return saturation points
     */
    default int getSaturation() {
        return 0;
    }

    // TODO: effects should be implemented here when they're implemented in general

    /**
     * Retrieve the item this item is converted into when eaten.
     * If this method return null then no result item is given when eaten
     * @return item to give to the player when eaten
     */
    default ItemStack getResultItem() {
        return null;
    }

    /**
     * Check if this food item can always be eaten regardless of hunger.
     * @return if the item can eaten regardless of hunger
     */
    default boolean canAlwaysBeEaten() {
        return false;
    }

}
