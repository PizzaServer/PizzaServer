package io.github.willqi.pizzaserver.server.network.protocol.versions.v431;

import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419PacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v428.V428PacketRegistry;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class V431MinecraftVersion extends BaseMinecraftVersion {

    public static final int PROTOCOL = 431;
    public static final String VERSION = "1.16.220";
    private final BasePacketRegistry PACKET_REGISTRY = new V431PacketRegistry();


    public V431MinecraftVersion(ImplServer server) throws IOException {
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
        return new V419PacketBuffer(buf);
    }

    @Override
    public BasePacketBuffer createPacketBuffer(int initialCapacity) {
        return new V419PacketBuffer(initialCapacity);
    }

}
