package io.github.willqi.pizzaserver.api;

import io.github.willqi.pizzaserver.api.event.EventManager;
import io.github.willqi.pizzaserver.api.packs.ResourcePackManager;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.plugin.PluginManager;
import io.github.willqi.pizzaserver.api.scheduler.Scheduler;
import io.github.willqi.pizzaserver.api.utils.Logger;
import io.github.willqi.pizzaserver.api.level.LevelManager;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;

import java.util.Set;

/**
 * Represents a Minecraft Server.
 */
public interface Server {

    /**
     * Return all {@link Player}s who been spawned into the server.
     * @return a set of all players online
     */
    Set<Player> getPlayers();

    /**
     * Retrieve the amount of players currently online.
     * @return online player count
     */
    int getPlayerCount();

    /**
     * Retrieve the motto of the day message displayed in the server list menu.
     * @return motto of the day
     */
    String getMotd();

    /**
     * Change the motto of the day message displayed in the server list menu.
     */
    void setMotd(String motd);

    /**
     * Get the maximum amount of {@link Player}s allowed on the server.
     * @return max player count
     */
    int getMaximumPlayerCount();

    /**
     * Set the maximum allowed {@link Player}s allowed on the server.
     * @param players max player count
     */
    void setMaximumPlayerCount(int players);

    /**
     * Retrieve the target ticks per second for the server.
     * @return target tps
     */
    int getTargetTps();

    /**
     * Change the target ticks per second for the server.
     * @param newTps new ticks per second
     */
    void setTargetTps(int newTps);

    /**
     * Retrieve the last recorded ticks per second.
     * @return current tps
     */
    int getCurrentTps();

    PluginManager getPluginManager();

    ResourcePackManager getResourcePackManager();

    LevelManager getLevelManager();

    EventManager getEventManager();

    Scheduler getScheduler();

    Logger getLogger();

    /**
     * Get the path to the root server directory.
     * @return path to the root server directory
     */
    String getRootDirectory();



}
