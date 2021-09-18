package io.github.willqi.pizzaserver.api.event.type.player;

import io.github.willqi.pizzaserver.api.player.Player;

/**
 * Called when a player stops sneaking.
 */
public class PlayerStopSneakingEvent extends BasePlayerEvent {

    public PlayerStopSneakingEvent(Player player) {
        super(player);
    }

}
