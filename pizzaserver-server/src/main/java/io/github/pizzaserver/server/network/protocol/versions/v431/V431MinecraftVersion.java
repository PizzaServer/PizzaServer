package io.github.pizzaserver.server.network.protocol.versions.v431;

import io.github.pizzaserver.server.network.protocol.versions.v428.V428MinecraftVersion;

import java.io.IOException;

public class V431MinecraftVersion extends V428MinecraftVersion {

    public static final int PROTOCOL = 431;
    public static final String VERSION = "1.16.220";


    public V431MinecraftVersion() throws IOException {}

    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
}
