package io.github.pizzaserver.api.block.descriptors;

import io.github.pizzaserver.api.block.Block;

/**
 * Represents a block that is to be treated like a liquid.
 */
public interface Liquid extends Block {

    default boolean isSwimmable() {
        return true;
    }
}
