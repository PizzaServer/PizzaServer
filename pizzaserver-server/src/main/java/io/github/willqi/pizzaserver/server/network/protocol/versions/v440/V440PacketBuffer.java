package io.github.willqi.pizzaserver.server.network.protocol.versions.v440;

import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBufferData;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v431.V431PacketBuffer;
import io.netty.buffer.ByteBuf;

public class V440PacketBuffer extends V431PacketBuffer {

    public V440PacketBuffer(BaseMinecraftVersion version) {
        super(version);
    }

    public V440PacketBuffer(BaseMinecraftVersion version, int initialCapacity) {
        super(version, initialCapacity);
    }

    public V440PacketBuffer(BaseMinecraftVersion version, ByteBuf byteBuf) {
        super(version, byteBuf);
    }

    @Override
    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new V440PacketBuffer(this.getVersion(), buffer);
    }

    @Override
    public BasePacketBufferData getData() {
        return V440PacketBufferData.INSTANCE;
    }

}
