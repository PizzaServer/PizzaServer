package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.entity.meta.APIEntityMetaData;
import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.api.utils.APILocation;
import io.github.willqi.pizzaserver.api.utils.Watchable;
import io.github.willqi.pizzaserver.api.world.chunks.APIChunk;

/**
 * Represents a entity on Minecraft
 */
public interface APIEntity extends Watchable {

    long getId();

    /**
     * Retrieve the {@link APIChunk} the entity is in
     * @return the {@link APIChunk}
     */
    APIChunk getChunk();

    /**
     * Retrieve the {@link APILocation} of the entity
     * @return the {@link APILocation}
     */
    APILocation getLocation();

    /**
     * Change the {@link APILocation} of the entity
     * @param location the new {@link APILocation}
     */
    void setLocation(APILocation location);

    APIEntityMetaData getMetaData();

    void setMetaData(APIEntityMetaData metaData);

    /**
     * Check if the entity has been spawned into a world yet
     * @return if the entity has been spawned into a world
     */
    boolean hasSpawned();

    void spawnTo(APIPlayer player);

    void despawnFrom(APIPlayer player);

}
