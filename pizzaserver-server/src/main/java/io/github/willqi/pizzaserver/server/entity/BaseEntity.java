package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.NumberUtils;
import io.github.willqi.pizzaserver.server.entity.meta.ImplEntityMetaData;
import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseEntity implements Entity {

    public static long ID;


    private final long id;
    private final Set<Player> spawnedTo = new HashSet<>();
    private boolean spawned;

    private Location location = null;

    private EntityMetaData metaData = new ImplEntityMetaData();


    public BaseEntity() {
        this.id = ID++;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Chunk getChunk() {
        return this.location.getChunk();
    }

    @Override
    public void setLocation(Location newLocation) {
        if (this.location != null) {
            Chunk oldChunk = this.getChunk();
            Chunk newChunk = newLocation.getChunk();
            if (!oldChunk.equals(newChunk)) {
                oldChunk.removeEntity(this);
                for (Player viewer : this.getViewers()) {
                    if (!newChunk.canBeVisibleTo(viewer)) {
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

    @Override
    public EntityMetaData getMetaData() {
        return this.metaData;
    }

    @Override
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
     * Called when the entity is initially spawned into a world.
     * This is useful for entity initialization.
     */
    public abstract void onSpawned();

    /**
     * Used internally to when spawning this entity in a world
     * @param spawned if the entity has been spawned
     */
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    @Override
    public boolean hasSpawned() {
        return this.spawned;
    }

    @Override
    public void spawnTo(Player player) {
        this.spawnedTo.add(player);
    }

    @Override
    public void despawnFrom(Player player) {
        this.spawnedTo.remove(player);
    }

    @Override
    public Set<Player> getViewers() {
        return this.spawnedTo;
    }

    @Override
    public int hashCode() {
        return 43 * (int)this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseEntity) {
            return NumberUtils.isNearlyEqual(((BaseEntity) obj).getId(), this.getId());
        }
        return false;
    }
}
