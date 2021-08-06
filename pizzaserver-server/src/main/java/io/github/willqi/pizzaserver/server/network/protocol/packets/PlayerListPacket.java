package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.player.PlayerList;

import java.util.Collection;

/**
 * Sent to update the player list in-game
 * This is also required to be sent before a AddPlayer packet
 * or else the player entity will not spawn.
 */
public class PlayerListPacket extends BaseBedrockPacket {

    public static final int ID = 0x3F;

    private ActionType actionType;
    private Collection<PlayerList.Entry> entries;


    public PlayerListPacket() {
        super(ID);
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Collection<PlayerList.Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(Collection<PlayerList.Entry> entries) {
        this.entries = entries;
    }


    public enum ActionType {
        ADD,
        REMOVE
    }

}
