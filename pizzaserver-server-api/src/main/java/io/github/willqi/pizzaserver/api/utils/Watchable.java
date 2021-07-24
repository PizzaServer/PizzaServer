package io.github.willqi.pizzaserver.api.utils;

import io.github.willqi.pizzaserver.api.player.Player;

import java.util.Set;

public interface Watchable {

    /**
     * Retrieve all of the {@link Player}s who can see this
     * @return a {@link Set< Player >} containing those who can see this
      */
    Set<Player> getViewers();

}
