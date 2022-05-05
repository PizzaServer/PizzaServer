package io.github.pizzaserver.api;

import io.github.pizzaserver.api.utils.Config;

public class ServerConfig {

    private final boolean forcePacks;
    private final int maxPlayers;
    private final String motd;
    private final boolean onlineMode;
    private final boolean encryptedEnabled;
    private final int minimumSupportedProtocol;

    private final String ip;
    private final int port;
    private final int compressionLevel;

    private final String defaultWorldName;
    private final float defaultYaw;
    private final float defaultPitch;
    private final boolean saving;

    private final int chunkRadius;
    private final int entityRenderDistance;
    private final int playerChunkRangeTickRadius;
    private final int chunkProcessingThreads;
    private final int chunkProcessingCapPerThread;
    private final int chunkExpirySeconds;

    private final boolean debugMessages;


    public ServerConfig(Config config) {
        this.motd = config.getString("server.motd");
        this.maxPlayers = config.getInteger("server.max-players");
        this.forcePacks = config.getBoolean("server.force-packs");
        this.onlineMode = config.getBoolean("server.online-mode");
        this.encryptedEnabled = config.getBoolean("server.enable-encryption");
        this.minimumSupportedProtocol = config.getInteger("server.minimum-supported-protocol");

        this.ip = config.getString("network.ip");
        this.port = config.getInteger("network.port");
        this.compressionLevel = config.getInteger("network.compression-level");

        this.defaultWorldName = config.getString("world.default-name");
        this.defaultYaw = (float) config.getDouble("world.default-yaw");
        this.defaultPitch = (float) config.getDouble("world.default-pitch");
        this.saving = config.getBoolean("world.saving");

        this.chunkRadius = config.getInteger("world.chunk.radius");
        this.entityRenderDistance = config.getInteger("world.chunk.entity-render-radius");
        this.playerChunkRangeTickRadius = config.getInteger("world.chunk.player-tick-radius");
        this.chunkProcessingThreads = config.getInteger("world.chunk.threads");
        this.chunkProcessingCapPerThread = config.getInteger("world.chunk.thread-processing-cap");
        this.chunkExpirySeconds = config.getInteger("world.chunk.expiry-time");

        this.debugMessages = config.getBoolean("debug.messages");
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public int getMaximumPlayers() {
        return this.maxPlayers;
    }

    public String getMotd() {
        return this.motd;
    }

    public boolean arePacksForced() {
        return this.forcePacks;
    }

    /**
     * Returns if xbox chains should be verified.
     * @return if xbox chains should be verified
     */
    public boolean isOnlineMode() {
        return this.onlineMode;
    }

    public boolean isEncryptionEnabled() {
        return this.encryptedEnabled;
    }

    public int getMinimumSupportedProtocol() {
        return this.minimumSupportedProtocol;
    }

    public boolean isSavingEnabled() {
        return this.saving;
    }

    public int getMaxChunkThreads() {
        return this.chunkProcessingThreads;
    }

    public int getChunkRadius() {
        return this.chunkRadius;
    }

    public int getEntityChunkRenderDistance() {
        return this.entityRenderDistance;
    }

    /**
     * Max amount of chunk requests a level chunk processing thread can handle per tick.
     * @return max chunk request processing count per tick
     */
    public int getMaxChunkProcessingCountPerTick() {
        return this.chunkProcessingCapPerThread;
    }

    /**
     * Returns the amount of seconds it takes for a chunk with no chunk loaders
     * (caused by plugins fetching a block in an unloaded chunk) to be automatically unloaded.
     * @return amount of seconds
     */
    public int getChunkExpiryTime() {
        return this.chunkExpirySeconds;
    }

    /**
     * Returns the radius of chunks a player must be to a chunk for it to tick block updates/entities.
     * @return chunk radius
     */
    public int getChunkPlayerTickRadius() {
        return this.playerChunkRangeTickRadius;
    }

    public String getDefaultWorldName() {
        return this.defaultWorldName;
    }

    /**
     * Default yaw to assign players who join the server.
     * @return default yaw
     */
    public float getDefaultYaw() {
        return this.defaultYaw;
    }

    /**
     * Default pitch to assign players who join the server.
     * @return default pitch
     */
    public float getDefaultPitch() {
        return this.defaultPitch;
    }

    public int getNetworkCompressionLevel() {
        return this.compressionLevel;
    }

    public boolean isDebugLoggingEnabled() {
        return this.debugMessages;
    }

}
