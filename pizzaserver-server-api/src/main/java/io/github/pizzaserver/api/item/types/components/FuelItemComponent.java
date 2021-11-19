package io.github.pizzaserver.api.item.types.components;

/**
 * Represents an item that can be used as fuel.
 */
public interface FuelItemComponent {

    /**
     * Retrieve the amount of ticks this fuel lasts for.
     * @return amount of ticks
     */
    int getFuelTicks();

}
