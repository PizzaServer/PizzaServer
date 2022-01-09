package io.github.pizzaserver.api.block.descriptors;

import io.github.pizzaserver.api.block.Block;

/**
 * Blocks that implement this interface are flammable.
 */
public interface Flammable extends Block {

    /**
     * How likely the block will be destroyed by flames when on fire.
     * @return chance of being destroyed
     */
    int getBurnOdds();

    /**
     * How likely the block will catch flame when next to a fire.
     * @return Chance of catching fire
     */
    int getFlameOdds();

}
