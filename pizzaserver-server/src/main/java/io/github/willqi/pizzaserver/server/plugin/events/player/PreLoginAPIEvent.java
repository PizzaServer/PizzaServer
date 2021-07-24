package io.github.willqi.pizzaserver.server.plugin.events.player;

import io.github.willqi.pizzaserver.server.player.Player;

public class PreLoginAPIEvent extends PlayerAPIEvent {

    public PreLoginAPIEvent(Player player) {
        super(player);
    }

}
