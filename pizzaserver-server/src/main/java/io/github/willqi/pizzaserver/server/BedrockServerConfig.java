package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.server.utils.Config;

public class BedrockServerConfig {

    private final Config config;


    public BedrockServerConfig(Config config) {
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

    public String getDefaultWorldName() {
        return this.config.getString("world-default");
    }

}
