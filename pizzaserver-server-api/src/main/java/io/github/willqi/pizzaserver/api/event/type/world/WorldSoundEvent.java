package io.github.willqi.pizzaserver.api.event.type.world;

import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.api.world.data.WorldSound;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class WorldSoundEvent extends BaseWorldEvent.Cancellable {

    private WorldSound sound;
    private Vector3 location;
    private boolean global;
    private boolean baby;
    private String entityIdentifier;
    private int blockID;

    public WorldSoundEvent(World world, WorldSound sound, Vector3 location, boolean isGlobal, boolean isBaby, String entityIdentifier, int blockId) {
        super(world);
        this.sound = sound;
        this.location = location;
        this.global = isGlobal;
        this.baby = isBaby;
        this.entityIdentifier = entityIdentifier;
        this.blockID = blockId;
    }

    public WorldSound getSound() {
        return sound;
    }

    public void setSound(WorldSound sound) {
        this.sound = sound;
    }

    public Vector3 getLocation() {
        return location;
    }

    public void setLocation(Vector3 location) {
        this.location = location;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isBaby() {
        return baby;
    }

    public void setBaby(boolean baby) {
        this.baby = baby;
    }

    public String getEntityIdentifier() {
        return entityIdentifier;
    }

    public void setEntityIdentifier(String entityIdentifier) {
        this.entityIdentifier = entityIdentifier;
    }

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }
}
