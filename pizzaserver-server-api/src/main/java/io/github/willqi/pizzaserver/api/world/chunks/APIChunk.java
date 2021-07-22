package io.github.willqi.pizzaserver.api.world.chunks;

import io.github.willqi.pizzaserver.api.player.APIPlayer;

/**
 * Represents a 16x16 chunk of land in the world
 */
public interface APIChunk {

    // TODO: remove canSeeChunk from Player.java
    boolean canBeVisibleTo(APIPlayer player);

}
