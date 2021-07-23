package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.entity.meta.APIEntityMetaData;

public class SetEntityDataPacket extends BedrockPacket {

    public static final int ID = 0x27;

    private long runtimeId;
    private APIEntityMetaData data;

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

    public APIEntityMetaData getData() {
        return this.data;
    }

    public void setData(APIEntityMetaData data) {
        this.data = data;
    }

    public long getTick() {
        return this.tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

}
