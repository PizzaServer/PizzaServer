package io.github.willqi.pizzaserver.api.utils;

import io.github.willqi.pizzaserver.api.player.APIPlayer;

import java.util.Set;

public interface Watchable {

    /**
     * Retrieve all of the {@link APIPlayer}s who can see this
     * @return a {@link Set<APIPlayer>} containing those who can see this
      */
    Set<APIPlayer> getViewers();

}
