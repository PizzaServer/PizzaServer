package io.github.pizzaserver.api.block.traits;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.utils.HorizontalDirection;

/**
 * Blocks that only have horizontal directions.
 */
public interface HorizontalDirectionalTrait extends Block {

    HorizontalDirection getDirection();

    void setDirection(HorizontalDirection direction);

}
