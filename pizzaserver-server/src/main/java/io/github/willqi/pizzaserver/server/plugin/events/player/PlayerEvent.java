package io.github.willqi.pizzaserver.server.plugin.events.player;

import io.github.willqi.pizzaserver.server.plugin.events.Event;
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
