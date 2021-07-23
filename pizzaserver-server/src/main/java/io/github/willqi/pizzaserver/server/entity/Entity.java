package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.APIEntity;
import io.github.willqi.pizzaserver.api.entity.meta.APIEntityMetaData;
import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.api.utils.APILocation;
import io.github.willqi.pizzaserver.api.world.chunks.APIChunk;
import io.github.willqi.pizzaserver.commons.utils.NumberUtils;
import io.github.willqi.pizzaserver.server.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;

import java.util.HashSet;
import java.util.Set;

public abstract class Entity implements APIEntity {

    public static long ID;


    private final long id;
    private final Set<APIPlayer> spawnedTo = new HashSet<>();
    private boolean spawned;

    private APILocation location = null;

    private APIEntityMetaData metaData = new EntityMetaData();


    public Entity() {
        this.id = ID++;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public APILocation getLocation() {
        return this.location;
    }

    @Override
    public APIChunk getChunk() {
        return this.location.getChunk();
    }

    @Override
    public void setLocation(APILocation newLocation) {
        if (this.location != null) {
            APIChunk oldChunk = this.getChunk();
            APIChunk newChunk = newLocation.getChunk();
            if (!oldChunk.equals(newChunk)) {
                oldChunk.removeEntity(this);
                for (APIPlayer viewer : this.getViewers()) {
                    if (!newChunk.canBeVisibleTo(viewer)) {
                        this.despawnFrom(viewer);
                    }
                }

                newChunk.addEntity(this);
                for (APIPlayer player : newChunk.getViewers()) {
                    this.spawnTo(player);
                }
            }
        } else {
            newLocation.getChunk().addEntity(this);
        }
        this.location = newLocation;
    }

    @Override
    public APIEntityMetaData getMetaData() {
        return this.metaData;
    }

    @Override
    public void setMetaData(APIEntityMetaData metaData) {
        this.metaData = metaData;

        SetEntityDataPacket entityDataPacket = new SetEntityDataPacket();
        entityDataPacket.setRuntimeId(this.getId());
        entityDataPacket.setData(this.getMetaData());
        for (APIPlayer player : this.getViewers()) {
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
    public void spawnTo(APIPlayer player) {
        this.spawnedTo.add(player);
    }

    @Override
    public void despawnFrom(APIPlayer player) {
        this.spawnedTo.remove(player);
    }

    @Override
    public Set<APIPlayer> getViewers() {
        return this.spawnedTo;
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
