package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class InteractPacket extends BaseBedrockPacket {

    public static final int ID = 0x21;

    private Type action;
    private long targetEntityRuntimeId;
    private Vector3 position;


    public InteractPacket() {
        super(ID);
    }

    public Type getAction() {
        return this.action;
    }

    public void setAction(Type type) {
        this.action = type;
    }

    public long getTargetEntityRuntimeId() {
        return this.targetEntityRuntimeId;
    }

    public void setTargetEntityRuntimeId(long entityRuntimeId) {
        this.targetEntityRuntimeId = entityRuntimeId;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }


    public enum Type {
        NONE,
        INTERACT,
        DAMAGE,
        LEAVE_VEHICLE,
        MOUSE_OVER,
        NPC_OPEN,
        OPEN_INVENTORY
    }

}
