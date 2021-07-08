package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.network.protocol.data.MovementMode;
import io.github.willqi.pizzaserver.server.network.protocol.data.TeleportationCause;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class MovePlayerPacket extends BedrockPacket {

    public static final int ID = 0x13;

    private long entityRuntimeId;
    private Vector3 position;
    private Vector3 rotation;   // pitch, yaw, and head yaw?
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

    public Vector3 getRotation() {
        return this.rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
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
