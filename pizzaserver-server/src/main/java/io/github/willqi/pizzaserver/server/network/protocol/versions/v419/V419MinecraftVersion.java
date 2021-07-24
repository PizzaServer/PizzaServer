package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.server.BedrockServer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BedrockMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketRegistry;

import java.io.IOException;

public class V419MinecraftVersion extends BedrockMinecraftVersion {

    public static final int PROTOCOL = 419;
    public static final String VERSION = "1.16.100";

    private final PacketRegistry packetRegistry = new V419PacketRegistry();

    public V419MinecraftVersion(BedrockServer server) throws IOException {
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
    public PacketRegistry getPacketRegistry() {
        return this.packetRegistry;
    }

}
