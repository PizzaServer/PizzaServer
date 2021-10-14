package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.player.skin.Skin;

import java.util.UUID;

public class PlayerSkinPacket extends BaseBedrockPacket {

    public static final int ID = 0x5d;

    private UUID playerUUID;
    private Skin skin;
    private boolean trusted;
    private String newSkinName = "";
    private String oldSkinName = "";


    public PlayerSkinPacket() {
        super(ID);
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public void setPlayerUUID(UUID uuid) {
        this.playerUUID = uuid;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public boolean isTrusted() {
        return this.trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public String getNewSkinName() {
        return this.newSkinName;
    }

    public void setNewSkinName(String skinName) {
        this.newSkinName = skinName;
    }

    public String getOldSkinName() {
        return this.oldSkinName;
    }

    public void setOldSkinName(String skinName) {
        this.oldSkinName = skinName;
    }

}
