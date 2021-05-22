package io.github.willqi.pizzaserver.server.events.generic;

import io.github.willqi.pizzaserver.server.events.Event;
import io.github.willqi.pizzaserver.server.player.Player;

public abstract class PlayerEvent extends Event {

    protected Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

}
