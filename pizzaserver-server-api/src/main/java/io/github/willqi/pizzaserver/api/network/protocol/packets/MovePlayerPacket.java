package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.data.MovementMode;
import io.github.willqi.pizzaserver.api.network.protocol.data.TeleportationCause;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * Sent by the server and client to move a player entity.
 */
public class MovePlayerPacket extends BaseBedrockPacket {

    public static final int ID = 0x13;

    private long entityRuntimeId;
    private Vector3 position;
    private float pitch;
    private float yaw;
    private float headYaw;
    private MovementMode mode;
    private boolean onGround;

    private long ridingEntityRuntimeId;

    private TeleportationCause teleportationCause;
    private int entityType;

    private long tick;


    public MovePlayerPacket() {
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

    public MovementMode getMode() {
        return this.mode;
    }

    public void setMode(MovementMode mode) {
        this.mode = mode;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public long getRidingEntityRuntimeId() {
        return this.ridingEntityRuntimeId;
    }

    public void setRidingEntityRuntimeId(long ridingEntityRuntimeId) {
        this.ridingEntityRuntimeId = ridingEntityRuntimeId;
    }

    public TeleportationCause getTeleportationCause() {
        return this.teleportationCause;
    }

    public void setTeleportationCause(TeleportationCause cause) {
        this.teleportationCause = cause;
    }

    public int getEntityType() {
        return this.entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public long getTick() {
        return this.tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }



}
