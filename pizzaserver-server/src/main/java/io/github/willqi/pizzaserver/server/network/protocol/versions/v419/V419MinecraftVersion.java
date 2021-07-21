package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketRegistry;

import java.io.IOException;

public class V419MinecraftVersion extends MinecraftVersion {

    public static final int PROTOCOL = 419;
    public static final String VERSION = "1.16.100";

    private final PacketRegistry packetRegistry = new V419PacketRegistry();

    public V419MinecraftVersion(Server server) throws IOException {
        super(server);
    }


    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getVersionString() {
        return VERSION;
    }

    @Override
    public PacketRegistry getPacketRegistry() {
        return this.packetRegistry;
    }

}
