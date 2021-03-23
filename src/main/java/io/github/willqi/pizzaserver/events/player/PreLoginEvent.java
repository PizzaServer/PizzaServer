package io.github.willqi.pizzaserver.events.player;

import io.github.willqi.pizzaserver.player.Player;
import io.github.willqi.pizzaserver.events.generic.PlayerEvent;

public class PreLoginEvent extends PlayerEvent {

    public PreLoginEvent(Player player) {
        super(player);
    }

}
