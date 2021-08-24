package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.server.utils.Config;

public class ServerConfig {

    private final Config config;


    public ServerConfig(Config config) {
        this.config = config;
    }

    public String getIp() {
        return this.config.getString("server-ip");
    }

    public int getPort() {
        return this.config.getInteger("server-port");
    }

    public int getMaximumPlayers() {
        return this.config.getInteger("player-max");
    }

    public String getMotd() {
        return this.config.getString("server-motd");
    }

    public boolean arePacksForced() {
        return this.config.getBoolean("player-force-packs");
    }

    public int getChunkRadius() {
        return this.config.getInteger("world-chunk-radius");
    }

    /**
     * Max chunks that be requested per player each tick to be sent to itself
     * Additionally, this makes up the max chunks that be offloaded per tick
     * @return
     */
    public int getChunkRequestsPerTick() {
        return this.config.getInteger("chunk-requests-per-tick");
    }

    /**
     * Max chunks that be queued to the ChunkProcessingThread for each player/for unloading
     * @return max chunk queue count
     */
    public int getChunkProcessingCap() {
        return this.config.getInteger("chunk-requests-processing-cap");
    }

    public String getDefaultWorldName() {
        return this.config.getString("world-default");
    }

    /**
     * Default yaw to assign players who join the server
     * @return default yaw
     */
    public float getDefaultYaw() {
        return (float)this.config.getDouble("world-default-yaw");
    }

    /**
     * Default pitch to assign players who join the server
     * @return default pitch
     */
    public float getDefaultPitch() {
        return (float)this.config.getDouble("world-default-pitch");
    }

    public int getNetworkCompressionLevel() {
        return this.config.getInteger("network-compression-level");
    }

    /**
     * Returns if xbox chains should be verified
     * @return if xbox chains should be verified
     */
    public boolean isOnlineMode() {
        return this.config.getBoolean("online-mode");
    }

}
