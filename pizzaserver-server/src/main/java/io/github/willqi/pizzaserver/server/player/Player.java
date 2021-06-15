package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.entity.Entity;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LoginPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.server.player.data.Device;
import io.github.willqi.pizzaserver.server.player.data.skin.Skin;

import java.util.UUID;

public class Player extends Entity {

    private final Server server;
    private final BedrockClientSession session;

    private final MinecraftVersion version;
    private final Device device;
    private final String xuid;
    private final UUID uuid;
    private final String username;
    private final String languageCode;
    private Skin skin;

    private int chunkRadius;


    public Player(Server server, BedrockClientSession session, LoginPacket loginPacket) {
        this.server = server;
        this.session = session;

        this.version = session.getVersion();
        this.device = loginPacket.getDevice();
        this.xuid = loginPacket.getXuid();
        this.uuid = loginPacket.getUuid();
        this.username = loginPacket.getUsername();
        this.languageCode = loginPacket.getLanguageCode();
        this.skin = loginPacket.getSkin();
    }

    public MinecraftVersion getVersion() {
        return this.version;
    }

    public Device getDevice() {
        return this.device;
    }

    public String getXuid() {
        return this.xuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public void setSkin(Skin newSkin) {
        // TODO: packet level stuff for player skin updates
        this.skin = newSkin;
    }

    public int getChunkRadiusRequested() {
        return this.chunkRadius;
    }

    public void setChunkRadiusRequested(int radius) {
        this.chunkRadius = radius;
    }

    public Server getServer() {
        return this.server;
    }

    public void sendPacket(BedrockPacket packet) {
        this.session.sendPacket(packet);
    }

}
