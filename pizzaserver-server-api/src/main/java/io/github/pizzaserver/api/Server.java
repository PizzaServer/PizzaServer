package io.github.pizzaserver.api;

import io.github.pizzaserver.api.level.LevelManager;
import io.github.pizzaserver.api.event.EventManager;
import io.github.pizzaserver.api.packs.ResourcePackManager;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.plugin.PluginManager;
import io.github.pizzaserver.api.scheduler.Scheduler;
import io.github.pizzaserver.api.utils.Logger;

import java.util.Set;

/**
 * Represents a Minecraft Server.
 */
public abstract class Server {

    private static Server instance;


    /**
     * Return all {@link Player}s who been spawned into the server.
     * @return a set of all players online
     */
    public abstract Set<Player> getPlayers();

    /**
     * Retrieve the amount of players currently online.
     * @return online player count
     */
    public abstract int getPlayerCount();

    /**
     * Retrieve the motto of the day message displayed in the server list menu.
     * @return motto of the day
     */
    public abstract String getMotd();

    /**
     * Change the motto of the day message displayed in the server list menu.
     */
    public abstract void setMotd(String motd);

    /**
     * Get the maximum amount of {@link Player}s allowed on the server.
     * @return max player count
     */
    public abstract int getMaximumPlayerCount();

    /**
     * Set the maximum allowed {@link Player}s allowed on the server.
     * @param players max player count
     */
    public abstract void setMaximumPlayerCount(int players);

    /**
     * Retrieve the target ticks per second for the server.
     * @return target tps
     */
    public abstract int getTargetTps();

    /**
     * Change the target ticks per second for the server.
     * @param newTps new ticks per second
     */
    public abstract void setTargetTps(int newTps);

    /**
     * Retrieve the last recorded ticks per second.
     * @return current tps
     */
    public abstract int getCurrentTps();

    /**
     * Get the current server tick.
     * @return server tick
     */
    public abstract long getTick();

    public abstract PluginManager getPluginManager();

    public abstract ResourcePackManager getResourcePackManager();

    public abstract LevelManager getLevelManager();

    public abstract EventManager getEventManager();

    public abstract Scheduler getScheduler();

    public abstract Set<Scheduler> getSyncedSchedulers();

    /**
     * Sync a {@link Scheduler} to the server tick.
     * @param scheduler scheduler to sync
     */
    public abstract void syncScheduler(Scheduler scheduler);

    /**
     * Desync a {@link Scheduler} from the server tick.
     * @param scheduler scheduler to desync
     * @return if the scheduler was desynced
     */
    public abstract boolean desyncScheduler(Scheduler scheduler);

    public abstract Logger getLogger();

    /**
     * Get the path to the root server directory.
     * @return path to the root server directory
     */
    public abstract String getRootDirectory();

    public abstract ServerConfig getConfig();

    public static Server getInstance() {
        return instance;
    }

    public static void setInstance(Server server) {
        instance = server;
    }

}
