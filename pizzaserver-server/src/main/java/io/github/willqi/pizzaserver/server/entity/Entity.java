package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.commons.utils.NumberUtils;
import io.github.willqi.pizzaserver.server.entity.meta.EntityMeta;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.utils.Location;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;

import java.util.HashSet;
import java.util.Set;

public abstract class Entity {

    public static long ID;

    private final long id;
    private final Set<Player> spawnedTo = new HashSet<>();
    private boolean spawned;

    private Location location = null;

    private EntityMeta metaData = new EntityMeta();

    public Entity() {
        this.id = ID++;
    }

    public long getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public Chunk getChunk() {
        return this.location.getChunk();
    }

    public void setLocation(Location newLocation) {
        if (this.location != null) {
            Chunk oldChunk = this.getChunk();
            Chunk newChunk = newLocation.getChunk();
            if (!oldChunk.equals(newChunk)) {
                oldChunk.removeEntity(this);
                newChunk.addEntity(this);
            }
        } else {
            newLocation.getChunk().addEntity(this);
        }
        this.location = newLocation;
    }

    public EntityMeta getMetaData() {
        return this.metaData;
    }

    public void setMetaData(EntityMeta metaData) {
        this.metaData = metaData;
        // TODO: Send meta data to viewers
    }

    /**
     * Used internally to determine whether or not a entity has spawned in a world
     * @param spawned
     */
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    /**
     * Whether or not this entity was spawned in a world yet
     * @return
     */
    public boolean hasSpawned() {
        return this.spawned;
    }

    /**
     * Called when this entity is spawned
     * Used for setup on spawn
     */
    public void onSpawned() {}

    public Set<Player> getViewers() {
        return this.spawnedTo;
    }

    public void spawnTo(Player player) {
        this.spawnedTo.add(player);
    }

    public void despawnFrom(Player player) {
        this.spawnedTo.remove(player);
    }

    @Override
    public int hashCode() {
        return 43 * (int)this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity) {
            return NumberUtils.isNearlyEqual(((Entity) obj).getId(), this.getId());
        }
        return false;
    }
}
