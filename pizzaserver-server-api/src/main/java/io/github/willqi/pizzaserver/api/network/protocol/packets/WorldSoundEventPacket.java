package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.data.WorldSound;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * Sent by the client and server to send a sound to each other.
 */
public class WorldSoundEventPacket extends BaseBedrockPacket {

    public static final int ID = 0x7b;

    private WorldSound sound;
    private Vector3 vector3;
    private Block block;
    private String entityType; //Entity type (identifier:type, i.e. minecraft:player)
    private boolean isBaby;
    private boolean isGlobal;

    public WorldSoundEventPacket() {
        super(ID);
    }

    public static int getID() {
        return ID;
    }

    public WorldSound getSound() {
        return this.sound;
    }

    public void setSound(WorldSound sound) {
        this.sound = sound;
    }

    public Vector3 getVector3() {
        return this.vector3;
    }

    public void setVector3(Vector3 vector3) {
        this.vector3 = vector3;
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public void setEntityType(String entityID) {
        this.entityType = entityID;
    }

    public boolean isBaby() {
        return this.isBaby;
    }

    public void setBaby(boolean baby) {
        this.isBaby = baby;
    }

    public boolean isGlobal() {
        return this.isGlobal;
    }

    public void setGlobal(boolean global) {
        this.isGlobal = global;
    }

}
