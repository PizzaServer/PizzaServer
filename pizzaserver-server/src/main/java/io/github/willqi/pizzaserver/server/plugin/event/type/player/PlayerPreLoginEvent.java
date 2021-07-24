package io.github.willqi.pizzaserver.server.plugin.event.type.player;

import io.github.willqi.pizzaserver.api.player.Player;

public class PlayerPreLoginEvent extends PlayerEvent.Cancellable {

    public PlayerPreLoginEvent(Player player) {
        super(player);
    }

}
