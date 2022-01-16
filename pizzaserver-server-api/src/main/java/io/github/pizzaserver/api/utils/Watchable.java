package io.github.pizzaserver.api.utils;

import io.github.pizzaserver.api.player.Player;

import java.util.Set;

public interface Watchable {

    /**
     * Retrieve all of the {@link Player}s who can see this.
     *
     * @return a set of all players containing those who can see this.
     */
    Set<Player> getViewers();
}
