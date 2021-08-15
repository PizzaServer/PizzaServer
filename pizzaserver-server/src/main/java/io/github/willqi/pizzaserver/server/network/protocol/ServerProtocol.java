package io.github.willqi.pizzaserver.server.network.protocol;

import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419MinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v422.V422MinecraftVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ServerProtocol {

    public static final String GAME_VERSION = "1.16.200";
    public static final int LATEST_PROTOCOL_VERISON = V422MinecraftVersion.PROTOCOL;
    public static final int LATEST_BLOCK_STATES_VERSION = 17825806;

    public static final Map<Integer, BaseMinecraftVersion> VERSIONS = new HashMap<>();

    /**
     * Called to load all version resources at boot rather than when a player joins.
     * Doing this allows developers to be aware of missing files in version support immediately on boot.
     */
    public static void loadVersions() {
        loadVersion(V419MinecraftVersion.PROTOCOL, V419MinecraftVersion.class);
        loadVersion(V422MinecraftVersion.PROTOCOL, V422MinecraftVersion.class);
    }

    private static void loadVersion(int protocol, Class<? extends BaseMinecraftVersion> minecraftVersionClazz) {
        try {
            Constructor<? extends BaseMinecraftVersion> constructor = minecraftVersionClazz.getDeclaredConstructor(ImplServer.class);
            VERSIONS.put(protocol, constructor.newInstance(ImplServer.getInstance()));
        } catch (IllegalAccessException exception) {
            ImplServer.getInstance().getLogger().error("Failed to access protocol version v" + protocol, exception);
            return;
        } catch (InstantiationException exception) {
            ImplServer.getInstance().getLogger().error("Failed to load protocol version v" + protocol, exception);
            return;
        } catch (NoSuchMethodException exception) {
            ImplServer.getInstance().getLogger().error("Failed to find protocol version v" + protocol + " constructor.", exception);
            return;
        } catch (InvocationTargetException exception) {
            ImplServer.getInstance().getLogger().error("Failed to call protocol version v" + protocol + " constructor.", exception);
            return;
        }
        ImplServer.getInstance().getLogger().info("Loaded protocol version v" + protocol);
    }

}
