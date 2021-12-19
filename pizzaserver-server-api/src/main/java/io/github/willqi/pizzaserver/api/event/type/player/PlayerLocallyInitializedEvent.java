package io.github.willqi.pizzaserver.api.event.type.player;

import io.github.willqi.pizzaserver.api.player.Player;

public class PlayerLocallyInitializedEvent extends BasePlayerEvent {

    public PlayerLocallyInitializedEvent(Player player) {
        super(player);
    }

}
