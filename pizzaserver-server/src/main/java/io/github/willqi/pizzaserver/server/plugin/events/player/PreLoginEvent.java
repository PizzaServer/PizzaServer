package io.github.willqi.pizzaserver.server.plugin.events.player;

import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.plugin.events.generic.PlayerEvent;

public class PreLoginEvent extends PlayerEvent {

    public PreLoginEvent(Player player) {
        super(player);
    }

}
