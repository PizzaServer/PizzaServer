package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketRegistry;

import java.io.IOException;

public class V419MinecraftVersion extends BaseMinecraftVersion {

    public static final int PROTOCOL = 419;
    public static final String VERSION = "1.16.100";

    private final BasePacketRegistry packetRegistry = new V419PacketRegistry();

    public V419MinecraftVersion(ImplServer server) throws IOException {
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
        return this.packetRegistry;
    }

}
