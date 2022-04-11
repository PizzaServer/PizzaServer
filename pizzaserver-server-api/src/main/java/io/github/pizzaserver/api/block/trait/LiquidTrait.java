package io.github.pizzaserver.api.block.trait;

import io.github.pizzaserver.api.block.Block;

/**
 * Represents a block that is to be treated like a liquid.
 */
public interface LiquidTrait extends Block {

    default boolean isSwimmable() {
        return true;
    }

}
