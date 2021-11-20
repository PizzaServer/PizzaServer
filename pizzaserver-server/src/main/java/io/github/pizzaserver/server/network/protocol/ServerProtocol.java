package io.github.pizzaserver.server.network.protocol;

import io.github.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.pizzaserver.server.network.protocol.versions.V419MinecraftVersion;
import io.github.pizzaserver.server.network.protocol.versions.V422MinecraftVersion;
import io.github.pizzaserver.server.network.protocol.versions.V428MinecraftVersion;
import io.github.pizzaserver.server.network.protocol.versions.V431MinecraftVersion;
import io.github.pizzaserver.server.network.protocol.versions.V440MinecraftVersion;
import io.github.pizzaserver.server.network.protocol.versions.V448MinecraftVersion;
import io.github.pizzaserver.server.ImplServer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServerProtocol {

    public static final String GAME_VERSION = V448MinecraftVersion.VERSION;
    public static final int LATEST_PROTOCOL_VERISON = V448MinecraftVersion.PROTOCOL;
    public static final int LATEST_BLOCK_STATES_VERSION = 17825806;

    private static final Map<Integer, BaseMinecraftVersion> VERSIONS = new HashMap<>();

    /**
     * Called to load all version resources at boot rather than when a player joins.
     */
    public static void loadVersions() throws IOException {
        loadVersion(new V419MinecraftVersion());
        loadVersion(new V422MinecraftVersion());
        loadVersion(new V428MinecraftVersion());
        loadVersion(new V431MinecraftVersion());
        loadVersion(new V440MinecraftVersion());
        loadVersion(new V448MinecraftVersion());
    }

    public static Optional<BaseMinecraftVersion> getProtocol(int protocol) {
        return Optional.ofNullable(VERSIONS.getOrDefault(protocol, null));
    }

    private static void loadVersion(BaseMinecraftVersion version) {
        VERSIONS.put(version.getProtocol(), version);
        ImplServer.getInstance().getLogger().info("Loaded protocol version v" + version.getProtocol());
    }

}
