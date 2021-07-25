package io.github.willqi.pizzaserver.server.event.type.player;

import io.github.willqi.pizzaserver.api.player.Player;

public class PlayerPreLoginEvent extends BasePlayerEvent.Cancellable {

    public PlayerPreLoginEvent(Player player) {
        super(player);
    }

}
