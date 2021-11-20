package io.github.pizzaserver.server.network.protocol.versions;

import java.io.IOException;

public class V448MinecraftVersion extends V440MinecraftVersion {

    public static final int PROTOCOL = 448;
    public static final String VERSION = "1.17.10";


    public V448MinecraftVersion() throws IOException {}

    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

}
