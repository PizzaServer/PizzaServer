package io.github.pizzaserver.api.block.descriptors;

/**
 * Represents a block that is to be treated like a liquid.
 */
public interface Liquid {

    default boolean isSwimmable() {
        return true;
    }

}
