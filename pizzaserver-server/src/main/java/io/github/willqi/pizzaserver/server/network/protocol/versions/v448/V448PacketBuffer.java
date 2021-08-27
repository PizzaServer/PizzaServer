package io.github.willqi.pizzaserver.server.network.protocol.versions.v448;

import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v440.V440PacketBuffer;
import io.netty.buffer.ByteBuf;

public class V448PacketBuffer extends V440PacketBuffer {

    public V448PacketBuffer(BaseMinecraftVersion version) {
        super(version);
    }

    public V448PacketBuffer(BaseMinecraftVersion version, int initialCapacity) {
        super(version, initialCapacity);
    }

    public V448PacketBuffer(BaseMinecraftVersion version, ByteBuf byteBuf) {
        super(version, byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V448PacketBuffer(this.getVersion(), buffer);
    }

    @Override
    protected BasePacketBufferData getData() {
        return V448PacketBufferData.INSTANCE;
    }
}
