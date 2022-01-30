package io.github.pizzaserver.api.item.descriptors;

import io.github.pizzaserver.api.item.Item;

/**
 * Represents an item that can be used as fuel.
 */
public interface FuelItem extends Item {

    /**
     * Retrieve the amount of ticks this fuel lasts for.
     * @return amount of ticks
     */
    int getFuelTicks();

}
