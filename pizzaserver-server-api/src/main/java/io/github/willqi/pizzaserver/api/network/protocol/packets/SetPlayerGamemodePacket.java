package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.player.data.Gamemode;

/**
 * Sent by the server to update the player's gamemode.
 */
public class SetPlayerGamemodePacket extends BaseBedrockPacket {

    public static final int ID = 0x3e;

    private Gamemode gamemode;

    public SetPlayerGamemodePacket() {
        super(ID);
    }

    public Gamemode getGamemode() {
        return this.gamemode;
    }

    public void setGamemode(Gamemode gamemode) {
        this.gamemode = gamemode;
    }

}
