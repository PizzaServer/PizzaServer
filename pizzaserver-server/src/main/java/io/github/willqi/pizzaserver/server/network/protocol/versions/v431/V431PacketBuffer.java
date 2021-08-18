package io.github.willqi.pizzaserver.server.network.protocol.versions.v431;

import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.V428PacketBuffer;
import io.netty.buffer.ByteBuf;

public class V431PacketBuffer extends V428PacketBuffer {

    public V431PacketBuffer() {}

    public V431PacketBuffer(int initialCapacity) {
        super(initialCapacity);
    }

    public V431PacketBuffer(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V431PacketBuffer(buffer);
    }

    @Override
    protected BasePacketBufferData getData() {
        return V431PacketBufferData.INSTANCE;
    }

}
