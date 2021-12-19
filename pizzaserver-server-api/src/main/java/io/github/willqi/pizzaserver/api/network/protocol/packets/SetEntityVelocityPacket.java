package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * Sent to the client to update an entity's velocity for smooth movement.
 */
public class SetEntityVelocityPacket extends BaseBedrockPacket {

    public static final int ID = 0x28;

    private long entityRuntimeId;
    private Vector3 velocity;


    public SetEntityVelocityPacket() {
        super(ID);
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public Vector3 getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

}
