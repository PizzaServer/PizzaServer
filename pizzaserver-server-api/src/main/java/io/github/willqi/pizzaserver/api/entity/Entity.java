package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.utils.Watchable;
import io.github.willqi.pizzaserver.api.world.chunks.Chunk;

/**
 * Represents a entity on Minecraft
 */
public interface Entity extends Watchable {

    long getId();

    /**
     * Retrieve the {@link Chunk} the entity is in
     * @return the {@link Chunk}
     */
    Chunk getChunk();

    /**
     * Retrieve the {@link Location} of the entity
     * @return the {@link Location}
     */
    Location getLocation();

    /**
     * Change the {@link Location} of the entity
     * @param location the new {@link Location}
     */
    void setLocation(Location location);

    EntityMetaData getMetaData();

    void setMetaData(EntityMetaData metaData);

    /**
     * Called every server tick
     */
    void tick();

    /**
     * Check if the entity has been spawned into a world yet
     * @return if the entity has been spawned into a world
     */
    boolean hasSpawned();

    void spawnTo(Player player);

    void despawnFrom(Player player);

}
