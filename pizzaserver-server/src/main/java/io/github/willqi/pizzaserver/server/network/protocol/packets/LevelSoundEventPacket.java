package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.network.protocol.data.LevelSound;

public class LevelSoundEventPacket extends BedrockPacket {

    public static final int ID = 0x7b;

    private LevelSound sound;
    private Vector3 vector3;
    private int blockID;
    private String entityType; //Entity type (identifier:type, i.e. minecraft:player)
    private boolean isBaby;
    private boolean isGlobal;

    public LevelSoundEventPacket() {
        super(ID);
    }

    public static int getID() {
        return ID;
    }

    public LevelSound getSound() {
        return sound;
    }

    public void setSound(LevelSound sound) {
        this.sound = sound;
    }

    public Vector3 getVector3() {
        return vector3;
    }

    public void setVector3(Vector3 vector3) {
        this.vector3 = vector3;
    }

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityID) {
        this.entityType = entityID;
    }

    public boolean isBaby() {
        return isBaby;
    }

    public void setBaby(boolean baby) {
        isBaby = baby;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }
}
