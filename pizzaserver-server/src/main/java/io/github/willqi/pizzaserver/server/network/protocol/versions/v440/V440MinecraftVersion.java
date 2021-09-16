package io.github.willqi.pizzaserver.server.network.protocol.versions.v440;

import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419PacketBuffer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class V440MinecraftVersion extends BaseMinecraftVersion {

    public static final int PROTOCOL = 440;
    public static final String VERSION = "1.17.0";
    private final BasePacketRegistry PACKET_REGISTRY = new V440PacketRegistry();


    public V440MinecraftVersion(ImplServer server) throws IOException {
        super(server);
    }

    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public BasePacketRegistry getPacketRegistry() {
        return PACKET_REGISTRY;
    }

    @Override
    public BasePacketBuffer createPacketBuffer(ByteBuf buf) {
        return new V440PacketBuffer(this, buf);
    }

    @Override
    public BasePacketBuffer createPacketBuffer(int initialCapacity) {
        return new V440PacketBuffer(this, initialCapacity);
    }

}
