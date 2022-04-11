package io.github.pizzaserver.api.utils;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.commons.utils.NumberUtils;

public class Location {

    private final World world;
    private final float x;
    private final float y;
    private final float z;

    private final float pitch;
    private final float yaw;
    private final float headYaw;


    public Location(World world, Vector3i position) {
        this(world, position.toFloat());
    }

    public Location(World world, Vector3f position) {
        this(world, position, Vector3f.ZERO);
    }

    public Location(World world, Vector3i position, Vector3f rotation) {
        this(world, position.toFloat(), rotation);
    }

    public Location(World world, Vector3f position, Vector3f rotation) {
        this(world, position.getX(), position.getY(), position.getZ(), rotation.getX(), rotation.getY(), rotation.getZ());
    }

    public Location(World world, float x, float y, float z, float pitch, float yaw, float headYaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        this.pitch = pitch;
        this.yaw = yaw;
        this.headYaw = headYaw;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getHeadYaw() {
        return this.headYaw;
    }

    public Chunk getChunk() {
        if (this.getWorld() == null) {
            return  null;
        }
        return this.getWorld().getChunk(this.getChunkX(), this.getChunkZ());
    }

    public World getWorld() {
        return this.world;
    }

    public Level getLevel() {
        if (this.getWorld() == null) {
            return null;
        }
        return this.getWorld().getLevel();
    }

    public int getChunkX() {
        return (int) Math.floor(this.getX() / 16d);
    }

    public int getChunkZ() {
        return (int) Math.floor(this.getZ() / 16d);
    }

    public Vector3f toVector3f() {
        return Vector3f.from(this.getX(), this.getY(), this.getZ());
    }

    public Vector3i toVector3i() {
        return Vector3i.from(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public String toString() {
        return "Location(level=" + this.getWorld().getLevel().getName()
                + ", world=" + this.getWorld().getDimension()
                + ", x=" + this.getX()
                + ", y=" + this.getY()
                + ", z=" + this.getZ() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location otherLocation) {
            return NumberUtils.isNearlyEqual(this.getX(), otherLocation.getX())
                    && NumberUtils.isNearlyEqual(this.getY(), otherLocation.getY())
                    && NumberUtils.isNearlyEqual(this.getZ(), otherLocation.getZ())
                    && NumberUtils.isNearlyEqual(this.getPitch(), otherLocation.getPitch())
                    && NumberUtils.isNearlyEqual(this.getYaw(), otherLocation.getYaw())
                    && NumberUtils.isNearlyEqual(this.getHeadYaw(), otherLocation.getHeadYaw())
                    && this.getWorld().equals(otherLocation.getWorld());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) ((91 * this.getX())
                + (91 * this.getY())
                + (91 * this.getZ())
                + (91 * this.getWorld().hashCode())
                + (91 * this.getPitch())
                + (91 * this.getYaw())
                + (91 * this.getHeadYaw()));
    }

}
