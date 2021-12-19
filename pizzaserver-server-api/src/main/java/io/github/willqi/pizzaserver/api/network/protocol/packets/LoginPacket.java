package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.api.player.skin.Skin;

import java.util.UUID;

/**
 * Sent by the client to authenticate itself to the server.
 */
public class LoginPacket extends BaseBedrockPacket {

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

    /**
     * Network protocol the client is on.
     * @return game protocol
     */
    public int getProtocol() {
        return this.protocol;
    }

    /**
     * Change the game protocol of the player.
     * @param protocol game protocol
     */
    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    /**
     * If the player is xbox authenticated.
     * @return if the player is authenticated
     */
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    /**
     * Change if the player is xbox authenticated.
     * @param authenticated if the player is authenticated
     */
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * xbox id of the player if the client is authenticated.
     * @return xbox id
     */
    public String getXUID() {
        return this.xuid;
    }

    /**
     * Change the xbox id of the player.
     * @param xuid xbox id
     */
    public void setXUID(String xuid) {
        this.xuid = xuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the device the player is playing on.
     * @return device the player is on
     */
    public Device getDevice() {
        return this.device;
    }

    /**
     * Change the device the player is on.
     * @param device device the player is on
     */
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
