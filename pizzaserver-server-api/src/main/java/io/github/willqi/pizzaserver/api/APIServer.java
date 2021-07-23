package io.github.willqi.pizzaserver.api;

import io.github.willqi.pizzaserver.api.player.APIPlayer;

import java.util.Set;

/**
 * Represents a Minecraft Server
 */
public interface APIServer {

    /**
     * Start the server's internal logic
     */
    void boot();

    /**
     * Return all {@link APIPlayer}s who been spawned into the server
     * @return {@link Set<APIPlayer>}
     */
    Set<APIPlayer> getPlayers();

    /**
     * Retrieve the amount of players currently online
     * @return online player count
     */
    int getPlayerCount();

    /**
     * Retrieve the motto of the day message displayed in the server list menu
     * @return motto of the day
     */
    String getMotd();

    /**
     * Change the motto of the day message displayed in the server list menu
     */
    void setMotd(String motd);

    /**
     * Get the maximum amount of {@link APIPlayer}s allowed on the server
     * @return max player count
     */
    int getMaximumPlayerCount();

    /**
     * Set the maximum allowed {@link APIPlayer}s allowed on the server
     * @param players max player count
     */
    void setMaximumPlayerCount(int players);

    /**
     * Retrieve the target ticks per second for the server
     * @return target tps
     */
    int getTargetTps();

    /**
     * Change the target ticks per second for the server
     * @param newTps new ticks per second
     */
    void setTargetTps(int newTps);

    /**
     * Retrieve the last recorded ticks per second
     * @return current tps
     */
    int getCurrentTps();

}
