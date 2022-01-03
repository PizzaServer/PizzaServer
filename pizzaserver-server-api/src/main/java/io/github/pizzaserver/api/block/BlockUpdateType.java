package io.github.pizzaserver.api.block;

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
    BLOCK
}
