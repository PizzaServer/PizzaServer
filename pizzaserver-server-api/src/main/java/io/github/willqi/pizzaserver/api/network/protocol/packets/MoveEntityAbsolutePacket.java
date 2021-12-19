package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.commons.utils.Vector3;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MoveEntityAbsolutePacket extends BaseBedrockPacket {

    public static final int ID = 0x12;

    private long entityRuntimeId;
    private Vector3 position;
    private float pitch;
    private float yaw;
    private float headYaw;
    private final Set<Flag> flags = new HashSet<>();

    public MoveEntityAbsolutePacket() {
        super(ID);
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getHeadYaw() {
        return this.headYaw;
    }

    public void setHeadYaw(float headYaw) {
        this.headYaw = headYaw;
    }

    public Set<Flag> getFlags() {
        return Collections.unmodifiableSet(this.flags);
    }

    public void addFlag(Flag flag) {
        this.flags.add(flag);
    }

    public void removeFlag(Flag flag) {
        this.flags.remove(flag);
    }

    public enum Flag {
        ON_GROUND,
        TELEPORT
    }


}
