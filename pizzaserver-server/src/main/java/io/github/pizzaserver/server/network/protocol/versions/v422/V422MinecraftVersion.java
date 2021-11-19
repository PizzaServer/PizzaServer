package io.github.pizzaserver.server.network.protocol.versions.v422;

import io.github.pizzaserver.server.network.protocol.versions.v419.V419MinecraftVersion;

import java.io.IOException;

public class V422MinecraftVersion extends V419MinecraftVersion {

    public static final int PROTOCOL = 422;
    public static final String VERSION = "1.16.200";


    public V422MinecraftVersion() throws IOException {}

    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

}
