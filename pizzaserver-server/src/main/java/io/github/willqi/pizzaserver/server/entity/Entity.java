package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.commons.utils.NumberUtils;
import io.github.willqi.pizzaserver.server.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;
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

    private EntityMetaData metaData = new EntityMetaData();

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

    /**
     * Change the location of the entity and spawn itself for viewers of the new chunk
     * If a player cannot see the new location, it will despawn itself from that player
     * @param newLocation
     */
    public void setLocation(Location newLocation) {
        if (this.location != null) {
            Chunk oldChunk = this.getChunk();
            Chunk newChunk = newLocation.getChunk();
            if (!oldChunk.equals(newChunk)) {
                oldChunk.removeEntity(this);
                for (Player viewer : this.getViewers()) {
                    if (!viewer.canSeeChunk(newChunk)) {
                        this.despawnFrom(viewer);
                    }
                }

                newChunk.addEntity(this);
                for (Player player : newChunk.getViewers()) {
                    this.spawnTo(player);
                }
            }
        } else {
            newLocation.getChunk().addEntity(this);
        }
        this.location = newLocation;
    }

    public EntityMetaData getMetaData() {
        return this.metaData;
    }

    public void setMetaData(EntityMetaData metaData) {
        this.metaData = metaData;

        SetEntityDataPacket entityDataPacket = new SetEntityDataPacket();
        entityDataPacket.setRuntimeId(this.getId());
        entityDataPacket.setData(this.getMetaData());
        for (Player player : this.getViewers()) {
            player.sendPacket(entityDataPacket);
        }
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
