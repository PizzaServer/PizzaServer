package io.github.pizzaserver.server.network.protocol.versions;

import java.io.IOException;

public class V440MinecraftVersion extends V431MinecraftVersion {

    public static final int PROTOCOL = 440;
    public static final String VERSION = "1.17.0";


    public V440MinecraftVersion() throws IOException {}

    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

}
