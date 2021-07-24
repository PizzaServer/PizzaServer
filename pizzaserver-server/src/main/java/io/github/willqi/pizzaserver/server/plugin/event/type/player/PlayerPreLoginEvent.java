package io.github.willqi.pizzaserver.server.plugin.event.type.player;

import io.github.willqi.pizzaserver.api.player.APIPlayer;

public class PlayerPreLoginEvent extends PlayerEvent.Cancellable {

    public PlayerPreLoginEvent(APIPlayer player) {
        super(player);
    }

}
