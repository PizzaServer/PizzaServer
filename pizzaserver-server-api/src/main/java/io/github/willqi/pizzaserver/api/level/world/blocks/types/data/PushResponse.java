package io.github.willqi.pizzaserver.api.level.world.blocks.types.data;

/**
 * What the block type should do when it is pushed by a piston.
 */
public enum PushResponse {

    DENY,

    ALLOW,

    /**
     * Prevents this block from sticking to a sticky piston.
     */
    ALLOW_NO_STICKY,

    /**
     * Break this block when pushed by a piston.
     */
    BREAK

}
