package io.github.willqi.pizzaserver.server.network.protocol;

import io.github.willqi.pizzaserver.server.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419MinecraftVersion;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerProtocol {

    public static final String GAME_VERSION = "1.16.100";
    public static final int LATEST_PROTOCOL_VERISON = V419MinecraftVersion.PROTOCOL;

    public static final Map<Integer, MinecraftVersion> VERSIONS = Collections.unmodifiableMap(new HashMap<Integer, MinecraftVersion>(){
        {
            put(V419MinecraftVersion.PROTOCOL, new V419MinecraftVersion());
        }
    });
}
