package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.types.behaviour.EntityBehaviour;
import io.github.willqi.pizzaserver.api.level.Level;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.utils.Watchable;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;

/**
 * Represents a entity on Minecraft.
 */
public interface Entity extends Watchable {

    long getId();

    float getX();

    float getY();

    float getZ();

    int getFloorX();

    int getFloorY();

    int getFloorZ();

    /**
     * Retrieve the {@link Server} this entity is in.
     * @return {@link Server}
     */
    Server getServer();

    /**
     * Retrieve the {@link Level} this entity is in.
     * @return {@link Level}
     */
    Level getLevel();

    /**
     * Retrieve the {@link World} this entity is in.
     * @return {@link World}
     */
    World getWorld();

    /**
     * Retrieve the {@link Chunk} the entity is in.
     * @return the {@link Chunk}
     */
    Chunk getChunk();

    /**
     * Retrieve the {@link Location} of the entity.
     * @return the {@link Location}
     */
    Location getLocation();

    float getHeight();

    float getWidth();

    float getEyeHeight();

    EntityMetaData getMetaData();

    void setMetaData(EntityMetaData metaData);

    Inventory getInventory();

    EntityBehaviour getEntityBehaviour();

    void setEntityBehaviour(EntityBehaviour behaviour);

    /**
     * Called every server tick.
     */
    void tick();

    /**
     * Check if the entity has been spawned into a world yet.
     * @return if the entity has been spawned into a world
     */
    boolean hasSpawned();

    boolean hasSpawnedTo(Player player);

    /**
     * Spawns an entity to a player.
     * @param player the player the entity is being spawned to
     * @return if the entity was spawned
     */
    boolean spawnTo(Player player);

    /**
     * Despawns an entity from a player.
     * @param player the player the entity is being despawned from
     * @return if the entity was despawned
     */
    boolean despawnFrom(Player player);

    void despawn();

}
