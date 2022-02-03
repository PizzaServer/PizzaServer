package io.github.pizzaserver.api;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.entity.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.pizzaserver.api.event.EventManager;
import io.github.pizzaserver.api.item.CreativeRegistry;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.level.LevelManager;
import io.github.pizzaserver.api.packs.ResourcePackManager;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.plugin.PluginManager;
import io.github.pizzaserver.api.scheduler.Scheduler;
import io.github.pizzaserver.api.scoreboard.Scoreboard;
import io.github.pizzaserver.api.utils.Logger;

import java.util.Optional;
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
     * Retrieve a player by their username.
     * If no player can be found by the username given, it will look for any player
     * that has the username as a prefix.
     * @param username username of the player
     * @return player if any exists
     */
    public abstract Optional<Player> getPlayerByUsername(String username);

    public abstract Optional<Player> getPlayerByExactUsername(String username);

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

    public abstract Scoreboard createScoreboard();

    public abstract BossBar createBossBar();

    public abstract EntityInventory createInventory(Entity entity, ContainerType containerType);

    /**
     * Create an inventory for an entity.
     * @param entity entity associated with this inventory
     * @param containerType container type
     * @param size size of the inventory. MUST be less than or equal to the regular inventory size of the container
     * @return inventory
     */
    public abstract EntityInventory createInventory(Entity entity, ContainerType containerType, int size);

    public abstract BlockEntityInventory createInventory(BlockEntity blockEntity, ContainerType containerType);

    /**
     * Create an inventory for a block entity.
     * @param blockEntity block entity associated with this inventory
     * @param containerType container type
     * @param size size of the inventory. MUST be less than or equal to the regular inventory size of the container
     * @return inventory
     */
    public abstract BlockEntityInventory createInventory(BlockEntity blockEntity, ContainerType containerType, int size);

    public abstract BlockRegistry getBlockRegistry();

    public abstract BlockEntityRegistry getBlockEntityRegistry();

    public abstract ItemRegistry getItemRegistry();

    public abstract CreativeRegistry getCreativeRegistry();

    public abstract EntityRegistry getEntityRegistry();

    public static Server getInstance() {
        return instance;
    }

    public static void setInstance(Server server) {
        instance = server;
    }

}
