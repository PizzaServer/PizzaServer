package io.github.willqi.pizzaserver.server.network.protocol.versions.v422;

import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketRegistry;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class V422MinecraftVersion extends BaseMinecraftVersion {

    public static final int PROTOCOL = 422;
    public static final String VERSION = "1.16.200";
    private final BasePacketRegistry PACKET_REGISTRY = new V422PacketRegistry();


    public V422MinecraftVersion(ImplServer server) throws IOException {
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
        return new V422PacketBuffer(buf);
    }

    @Override
    public BasePacketBuffer createPacketBuffer(int initialCapacity) {
        return new V422PacketBuffer(initialCapacity);
    }

}
