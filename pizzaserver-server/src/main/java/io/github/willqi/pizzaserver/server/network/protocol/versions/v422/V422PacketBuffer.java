package io.github.willqi.pizzaserver.server.network.protocol.versions.v422;

import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419PacketBuffer;
import io.netty.buffer.ByteBuf;

public class V422PacketBuffer extends V419PacketBuffer {

    public V422PacketBuffer() {}

    public V422PacketBuffer(int initialCapacity) {
        super(initialCapacity);
    }

    public V422PacketBuffer(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V422PacketBuffer(buffer);
    }

    @Override
    protected BasePacketBufferData getData() {
        return V422PacketBufferData.INSTANCE;
    }
}
