package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.player.data.AnimationAction;

/**
 * Sent by the client and server to trigger a {@link AnimationAction}.
 */
public class PlayerAnimatePacket extends BaseBedrockPacket {

    public static final int ID = 0x2c;

    private AnimationAction action;
    private long entityRuntimeID;
    private float rowingTime;

    public PlayerAnimatePacket() {
        super(ID);
    }

    public AnimationAction getAction() {
        return this.action;
    }

    public void setAction(AnimationAction action) {
        this.action = action;
    }

    public long getEntityRuntimeID() {
        return this.entityRuntimeID;
    }

    public void setEntityRuntimeID(long entityRuntimeID) {
        this.entityRuntimeID = entityRuntimeID;
    }

    public float getRowingTime() {
        return this.rowingTime;
    }

    public void setRowingTime(float rowingTime) {
        this.rowingTime = rowingTime;
    }
}
