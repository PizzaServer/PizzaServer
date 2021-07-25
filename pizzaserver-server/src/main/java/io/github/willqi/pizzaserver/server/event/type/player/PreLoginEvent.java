package io.github.willqi.pizzaserver.server.event.type.player;

import io.github.willqi.pizzaserver.server.player.Player;

public class PreLoginEvent extends PlayerEvent.Cancellable {

    public PreLoginEvent(Player player) {
        super(player);
    }

}
