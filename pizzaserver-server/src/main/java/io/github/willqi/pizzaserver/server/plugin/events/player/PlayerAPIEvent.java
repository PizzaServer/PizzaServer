package io.github.willqi.pizzaserver.server.plugin.events.player;

import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.api.plugin.events.APIEvent;

public abstract class PlayerAPIEvent extends APIEvent {

    protected APIPlayer player;

    public PlayerAPIEvent(APIPlayer player) {
        this.player = player;
    }

    public APIPlayer getPlayer() {
        return this.player;
    }

}
