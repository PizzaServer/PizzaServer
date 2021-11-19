package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;

/**
 * Called when a player starts sneaking.
 */
public class PlayerStartSneakingEvent extends BasePlayerEvent {

    public PlayerStartSneakingEvent(Player player) {
        super(player);
    }

}
