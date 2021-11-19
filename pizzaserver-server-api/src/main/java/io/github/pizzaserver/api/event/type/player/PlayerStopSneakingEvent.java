package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;

/**
 * Called when a player stops sneaking.
 */
public class PlayerStopSneakingEvent extends BasePlayerEvent {

    public PlayerStopSneakingEvent(Player player) {
        super(player);
    }

}
