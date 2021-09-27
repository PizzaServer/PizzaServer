package io.github.willqi.pizzaserver.api.event.type.player;

import io.github.willqi.pizzaserver.api.player.Player;

/**
 * Called when a player starts sneaking.
 */
public class PlayerStartSneakingEvent extends BasePlayerEvent {

    public PlayerStartSneakingEvent(Player player) {
        super(player);
    }

}
