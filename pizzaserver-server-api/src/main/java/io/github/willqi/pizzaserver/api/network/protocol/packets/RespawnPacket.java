package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * Sent by the client and server to handle player respawning.
 */
public class RespawnPacket extends BaseBedrockPacket {

    public static final int ID = 0x2d;

    private long entityRuntimeId;
    private Vector3 position;
    private State state;


    public RespawnPacket() {
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

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public enum State {
        SEARCHING,
        SERVER_READY,
        CLIENT_READY
    }

}
