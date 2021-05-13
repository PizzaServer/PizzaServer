package io.github.willqi.pizzaserver.network.protocol;

import io.github.willqi.pizzaserver.network.protocol.versions.PacketRegistry;
import io.github.willqi.pizzaserver.network.protocol.versions.v419.V419PacketRegistry;
import io.github.willqi.pizzaserver.network.protocol.versions.v422.V422PacketRegistry;
import io.github.willqi.pizzaserver.network.protocol.versions.v428.V428PacketRegistry;
import io.github.willqi.pizzaserver.network.protocol.versions.v431.V431PacketRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerProtocol {

    public static final int MINECRAFT_VERSION_1_16_100_PROTOCOL = 419;
    public static final int MINECRAFT_VERSION_1_16_200_PROTOCOL = 422;
    public static final int MINECRAFT_VERSION_1_16_210_PROTOCOL = 428;
    public static final int MINECRAFT_VERSION_1_16_220_PROTOCOL = 431;

    public static final String GAME_VERSION = "1.16.220";

    public static final Map<Integer, PacketRegistry> PACKET_REGISTRIES = Collections.unmodifiableMap(new HashMap<Integer, PacketRegistry>(){
        {
            put(MINECRAFT_VERSION_1_16_100_PROTOCOL, new V419PacketRegistry());
            put(MINECRAFT_VERSION_1_16_200_PROTOCOL, new V422PacketRegistry());
            put(MINECRAFT_VERSION_1_16_210_PROTOCOL, new V428PacketRegistry());
            put(MINECRAFT_VERSION_1_16_220_PROTOCOL, new V431PacketRegistry());
        }
    });
}
