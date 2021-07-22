package io.github.willqi.pizzaserver.server.plugin.events.player;

import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.server.plugin.events.Event;

public abstract class PlayerEvent extends Event {

    protected APIPlayer player;

    public PlayerEvent(APIPlayer player) {
        this.player = player;
    }

    public APIPlayer getPlayer() {
        return this.player;
    }

}
