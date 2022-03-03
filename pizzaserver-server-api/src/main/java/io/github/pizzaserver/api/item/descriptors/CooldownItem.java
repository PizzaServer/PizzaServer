package io.github.pizzaserver.api.item.descriptors;

import io.github.pizzaserver.api.item.Item;

/**
 * Represents an item that has a cooldown when used.
 */
public interface CooldownItem extends Item {

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
