package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;

public class PlayerLocallyInitializedEvent extends BasePlayerEvent {

    public PlayerLocallyInitializedEvent(Player player) {
        super(player);
    }
}
