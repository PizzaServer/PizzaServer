package io.github.pizzaserver.server;

import io.github.pizzaserver.server.utils.Config;

public class ServerConfig {

    private final Config config;


    public ServerConfig(Config config) {
        this.config = config;
    }

    public String getIp() {
        return this.config.getString("network.ip");
    }

    public int getPort() {
        return this.config.getInteger("network.port");
    }

    public int getMaximumPlayers() {
        return this.config.getInteger("server.max-players");
    }

    public String getMotd() {
        return this.config.getString("server.motd");
    }

    public boolean arePacksForced() {
        return this.config.getBoolean("server.force-packs");
    }

    public int getMaxChunkThreads() {
        return this.config.getInteger("world.chunk.threads");
    }

    public int getChunkRadius() {
        return this.config.getInteger("world.chunk.radius");
    }

    public int getEntityChunkRenderDistance() {
        return this.config.getInteger("world.chunk.entity-render-radius");
    }

    /**
     * Max amount of chunk requests a level chunk processing thread can handle per tick.
     * @return max chunk request processing count per tick
     */
    public int getMaxChunkProcessingCountPerTick() {
        return this.config.getInteger("world.chunk.thread-processing-cap");
    }

    /**
     * Returns the amount of seconds it takes for a chunk with no chunk loaders
     * (caused by plugins fetching a block in an unloaded chunk) to be automatically unloaded.
     * @return amount of seconds
     */
    public int getChunkExpiryTime() {
        return this.config.getInteger("world.chunk.expiry-time");
    }

    public String getDefaultWorldName() {
        return this.config.getString("world.default-name");
    }

    /**
     * Default yaw to assign players who join the server.
     * @return default yaw
     */
    public float getDefaultYaw() {
        return (float) this.config.getDouble("world.default-yaw");
    }

    /**
     * Default pitch to assign players who join the server.
     * @return default pitch
     */
    public float getDefaultPitch() {
        return (float) this.config.getDouble("world.default-pitch");
    }

    public int getNetworkCompressionLevel() {
        return this.config.getInteger("network.compression-level");
    }

    /**
     * Returns if xbox chains should be verified.
     * @return if xbox chains should be verified
     */
    public boolean isOnlineMode() {
        return this.config.getBoolean("server.online-mode");
    }

    public boolean isDebugLoggingEnabled() {
        return this.config.getBoolean("debug.messages");
    }

}
