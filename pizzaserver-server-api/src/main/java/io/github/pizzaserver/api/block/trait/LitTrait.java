package io.github.pizzaserver.api.block.trait;

import io.github.pizzaserver.api.block.Block;

/**
 * Blocks that can be lit or unlit.
 */
public interface LitTrait extends Block {

    boolean isLit();

    void setLit(boolean status);

}
