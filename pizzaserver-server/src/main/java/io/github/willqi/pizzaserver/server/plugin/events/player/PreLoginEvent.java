package io.github.willqi.pizzaserver.server.plugin.events.player;

import io.github.willqi.pizzaserver.server.player.Player;

public class PreLoginEvent extends PlayerEvent {

    public PreLoginEvent(Player player) {
        super(player);
    }

}
