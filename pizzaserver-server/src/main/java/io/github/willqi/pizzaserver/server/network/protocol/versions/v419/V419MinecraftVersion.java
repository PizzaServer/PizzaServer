package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.server.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketRegistry;

public class V419MinecraftVersion extends MinecraftVersion {

    public static final int PROTOCOL = 419;


    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public PacketRegistry getPacketRegistry() {
        return null;
    }

}
