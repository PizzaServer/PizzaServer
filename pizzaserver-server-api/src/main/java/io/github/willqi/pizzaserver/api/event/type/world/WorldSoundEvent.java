package io.github.willqi.pizzaserver.api.event.type.world;

import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.data.WorldSound;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

import java.util.Optional;

public class WorldSoundEvent extends BaseWorldEvent.Cancellable {

    private WorldSound sound;
    private Vector3 location;
    private boolean global;
    private boolean baby;
    private String entityIdentifier;
    private Block block;

    public WorldSoundEvent(World world, WorldSound sound, Vector3 location, boolean isGlobal, boolean isBaby, String entityIdentifier, Block block) {
        super(world);
        this.sound = sound;
        this.location = location;
        this.global = isGlobal;
        this.baby = isBaby;
        this.entityIdentifier = entityIdentifier;
        this.block = block;
    }

    public WorldSound getSound() {
        return this.sound;
    }

    public void setSound(WorldSound sound) {
        this.sound = sound;
    }

    public Vector3 getLocation() {
        return this.location;
    }

    public void setLocation(Vector3 location) {
        this.location = location;
    }

    public boolean isGlobal() {
        return this.global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public void setBaby(boolean baby) {
        this.baby = baby;
    }

    public String getEntityIdentifier() {
        return this.entityIdentifier;
    }

    public void setEntityIdentifier(String entityIdentifier) {
        this.entityIdentifier = entityIdentifier;
    }

    public Optional<Block> getBlock() {
        return Optional.ofNullable(this.block);
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
