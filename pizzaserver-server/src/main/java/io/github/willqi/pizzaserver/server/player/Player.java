package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.entity.Entity;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LoginPacket;
import io.github.willqi.pizzaserver.server.player.data.Device;
import io.github.willqi.pizzaserver.server.player.data.skin.Skin;

import java.util.UUID;

public class Player extends Entity {

    private final Server server;
    private final BedrockClientSession session;

    private final int protocolVerison;
    private final Device device;
    private final String xuid;
    private final UUID uuid;
    private final String username;
    private final String languageCode;
    private Skin skin;


    public Player(Server server, BedrockClientSession session, LoginPacket loginPacket) {
        this.server = server;
        this.session = session;

        this.protocolVerison = loginPacket.getProtocol();
        this.device = loginPacket.getDevice();
        this.xuid = loginPacket.getXuid();
        this.uuid = loginPacket.getUuid();
        this.username = loginPacket.getUsername();
        this.languageCode = loginPacket.getLanguageCode();
        this.skin = loginPacket.getSkin();
    }

    public int getProtocolVerison() {
        return this.protocolVerison;
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

    public Server getServer() {
        return this.server;
    }

    public void sendPacket(BedrockPacket packet) {
        this.session.sendPacket(packet);
    }

}
