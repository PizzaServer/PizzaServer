package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent by the server to change properties about an entity.
 */
public class SetEntityDataPacket extends BaseBedrockPacket {

    public static final int ID = 0x27;

    private long runtimeId;
    private EntityMetaData data;

    private long tick;


    public SetEntityDataPacket() {
        super(ID);
    }

    public long getRuntimeId() {
        return this.runtimeId;
    }

    public void setRuntimeId(long runtimeId) {
        this.runtimeId = runtimeId;
    }

    public EntityMetaData getData() {
        return this.data;
    }

    public void setData(EntityMetaData data) {
        this.data = data;
    }

    public long getTick() {
        return this.tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

}
