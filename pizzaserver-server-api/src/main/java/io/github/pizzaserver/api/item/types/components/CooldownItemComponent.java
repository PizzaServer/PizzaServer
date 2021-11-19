package io.github.pizzaserver.api.item.types.components;

/**
 * Represents an item that has a cooldown when used.
 */
public interface CooldownItemComponent {

    /**
     * Retrieve the cooldown category this item falls under.
     * @return cooldown item category
     */
    String getCooldownCategory();

    /**
     * Retrieve the amount of ticks this item should be on cooldown for after usage.
     * @return amount of ticks
     */
    int getCooldownTicks();

}
