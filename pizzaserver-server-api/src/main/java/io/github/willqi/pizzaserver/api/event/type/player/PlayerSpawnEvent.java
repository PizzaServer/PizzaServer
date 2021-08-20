package io.github.willqi.pizzaserver.api.event.type.player;

import io.github.willqi.pizzaserver.api.player.Player;

/**
 * Called after the client sends the SetLocalPlayerAsInitializedPacket and the player entity is spawned
 */
public class PlayerSpawnEvent extends BasePlayerEvent.Cancellable {

    public PlayerSpawnEvent(Player player) {
        super(player);
    }

}
