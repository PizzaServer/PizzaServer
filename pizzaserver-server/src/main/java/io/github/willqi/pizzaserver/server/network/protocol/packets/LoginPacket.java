package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.player.data.Device;
import io.github.willqi.pizzaserver.server.player.skin.Skin;

import java.util.UUID;

public class LoginPacket extends BedrockPacket {

    public static final int ID = 0x01;

    private int protocol;
    private boolean authenticated;

    private String xuid;
    private UUID uuid;
    private Device device;
    private String username;
    private String languageCode;

    private Skin skin;

    public LoginPacket() {
        super(ID);
    }

    public int getProtocol() {
        return this.protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    /**
     * If the player is xbox authenticated
     * @return if the player is authenticated
     */
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getXuid() {
        return this.xuid;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }


}
