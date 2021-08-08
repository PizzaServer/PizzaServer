package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.api.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.NumberUtils;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.entity.meta.ImplEntityMetaData;
import io.github.willqi.pizzaserver.server.network.protocol.packets.RemoveEntityPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.SetEntityDataPacket;
import io.github.willqi.pizzaserver.server.utils.ImplLocation;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseEntity implements Entity {

    public static long ID;

    protected final long id;
    protected float x;
    protected float y;
    protected float z;
    protected World world;

    protected boolean spawned;
    private final Set<Player> spawnedTo = new HashSet<>();
    private EntityMetaData metaData = new ImplEntityMetaData();


    public BaseEntity() {
        this.id = ID++;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public float getZ() {
        return this.z;
    }

    @Override
    public int getFloorX() {
        return (int)Math.floor(this.x);
    }

    @Override
    public int getFloorY() {
        return (int)Math.floor(this.y);
    }

    @Override
    public int getFloorZ() {
        return (int)Math.floor(this.z);
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public Location getLocation() {
        return new ImplLocation(this.world, new Vector3(this.getX(), this.getY(), this.getZ()));
    }

    /**
     * Set the location of the entity
     * Used internally to setup and to clean up the entity
     * @param location entity location
     */
    public void setLocation(Location location) {
        if (location != null) {
            this.x = location.getX();
            this.y = location.getY();
            this.z = location.getZ();
            this.world = location.getWorld();
        } else {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.world = null;
        }
    }

    @Override
    public Chunk getChunk() {
        return this.getLocation().getChunk();
    }

    @Override
    public float getEyeHeight() {
        return this.getHeight() / 2 + 0.1f;
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
    public void onSpawned() {
        this.spawned = true;
    }

    /**
     * Called when the entity is completely despawned
     */
    public void onDespawned() {
        this.spawned = false;
    }

    @Override
    public boolean hasSpawned() {
        return this.spawned;
    }

    @Override
    public boolean hasSpawnedTo(Player player) {
        return this.spawnedTo.contains(player);
    }

    @Override
    public void spawnTo(Player player) {
        this.spawnedTo.add(player);
    }

    @Override
    public void despawnFrom(Player player) {
        if (this.spawnedTo.remove(player)) {
            RemoveEntityPacket entityPacket = new RemoveEntityPacket();
            entityPacket.setUniqueEntityId(this.getId());
            player.sendPacket(entityPacket);
        }
    }

    @Override
    public void despawn() {
        this.getWorld().removeEntity(this);
    }

    @Override
    public Set<Player> getViewers() {
        return new HashSet<>(this.spawnedTo);
    }

    @Override
    public int hashCode() {
        return 43 * (int)this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseEntity) {
            return NumberUtils.isNearlyEqual(((BaseEntity)obj).getId(), this.getId());
        }
        return false;
    }
}
