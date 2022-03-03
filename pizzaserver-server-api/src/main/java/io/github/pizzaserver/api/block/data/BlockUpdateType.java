package io.github.pizzaserver.api.block.data;

public enum BlockUpdateType {
    /**
     * Used when a neighbouring block is modified.
     * e.g. a block is mined so neighbouring blocks need an update
     */
    NEIGHBOUR,

    /**
     * Used when the block itself requires an update.
     * e.g. a button is pressed and needs to be reset to its original state later.
     */
    BLOCK,

    /**
     * Used for when the chunk randomly requests an update for blocks.
     */
    RANDOM
}
