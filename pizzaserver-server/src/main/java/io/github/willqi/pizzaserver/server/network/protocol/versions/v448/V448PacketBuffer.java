package io.github.willqi.pizzaserver.server.network.protocol.versions.v448;

import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.V440PacketBuffer;
import io.netty.buffer.ByteBuf;

public class V448PacketBuffer extends V440PacketBuffer {

    public V448PacketBuffer() {}

    public V448PacketBuffer(int initialCapacity) {
        super(initialCapacity);
    }

    public V448PacketBuffer(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V448PacketBuffer(buffer);
    }

    @Override
    protected BasePacketBufferData getData() {
        return V448PacketBufferData.INSTANCE;
    }
}
