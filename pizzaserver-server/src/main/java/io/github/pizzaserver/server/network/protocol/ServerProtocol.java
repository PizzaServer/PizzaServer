package io.github.pizzaserver.server.network.protocol;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.server.network.protocol.version.BaseMinecraftVersion;
import io.github.pizzaserver.server.network.protocol.version.V475MinecraftVersion;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServerProtocol {

    public static final String LATEST_GAME_VERSION = V475MinecraftVersion.VERSION;
    public static final int LATEST_PROTOCOL_VERSION = V475MinecraftVersion.PROTOCOL;
    public static final int LATEST_BLOCK_STATES_VERSION = 17825806;

    private static final Map<Integer, BaseMinecraftVersion> VERSIONS = new HashMap<>();

    /**
     * Called to load all version resources at boot rather than when a player joins.
     */
    public static void loadVersions() throws IOException {
        loadVersion(new V475MinecraftVersion());
    }

    public static Optional<BaseMinecraftVersion> getProtocol(int protocol) {
        return Optional.ofNullable(VERSIONS.getOrDefault(protocol, null));
    }

    private static void loadVersion(BaseMinecraftVersion version) {
        if (version.getProtocol() >= Server.getInstance().getConfig().getMinimumSupportedProtocol()) {
            VERSIONS.put(version.getProtocol(), version);
            Server.getInstance().getLogger().info("Loaded protocol version v" + version.getProtocol());
        }
    }
}
