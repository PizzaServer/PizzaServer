package io.github.pizzaserver.api.event.type.world;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.utils.Location;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;

import java.util.Optional;

public class WorldSoundEvent extends BaseWorldEvent.Cancellable {

    private SoundEvent sound;
    private final Location location;
    private boolean relativeVolumeDisabled;
    private boolean baby;
    private String entityIdentifier;
    private Block block;

    public WorldSoundEvent(Location location, SoundEvent sound, boolean relativeVolumeDisabled, boolean isBaby, String entityIdentifier, Block block) {
        super(location.getWorld());
        this.sound = sound;
        this.location = location;
        this.relativeVolumeDisabled = relativeVolumeDisabled;
        this.baby = isBaby;
        this.entityIdentifier = entityIdentifier;
        this.block = block;
    }

    public SoundEvent getSound() {
        return this.sound;
    }

    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean isRelativeVolumeDisabled() {
        return this.relativeVolumeDisabled;
    }

    public void setRelativeVolumeDisabled(boolean disabled) {
        this.relativeVolumeDisabled = disabled;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public void setBaby(boolean baby) {
        this.baby = baby;
    }

    public String getIdentifier() {
        return this.entityIdentifier;
    }

    public void setIdentifier(String entityIdentifier) {
        this.entityIdentifier = entityIdentifier;
    }

    public Optional<Block> getBlock() {
        return Optional.ofNullable(this.block);
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
