package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.data.EntityEventType;

/**
 * Sent by the server when a particular event occurs with an entity.
 * Some are entity specific (not all)
 */
public class EntityEventPacket extends BaseBedrockPacket {

    public static final int ID = 0x1b;

    private long runtimeEntityId;
    private EntityEventType type;
    private int data;


    public EntityEventPacket() {
        super(ID);
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public EntityEventType getType() {
        return this.type;
    }

    public void setType(EntityEventType type) {
        this.type = type;
    }

    public int getData() {
        return this.data;
    }

    public void setData(int data) {
        this.data = data;
    }

}
