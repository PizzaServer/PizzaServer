package io.github.willqi.pizzaserver.api.network.protocol.packets;

/**
 * Sent to disconnect a player from the server with an optional message.
 */
public class DisconnectPacket extends BaseBedrockPacket {

    public static final int ID = 0x05;

    private boolean hideDisconnectScreen;
    private String kickMessage = "Disconnected From Server";

    public DisconnectPacket() {
        super(ID);
    }

    /**
     * If the player should be sent directly to the server list rather than the
     * disconnect screen.
     * @return if it should hide the disconnect screen
     */
    public boolean isHidingDisconnectScreen() {
        return this.hideDisconnectScreen;
    }

    /**
     * Change if the disconnect should direct the player directly to the server list
     * or if it should show the message.
     * @param hide if it should hide the disconenct screen
     */
    public void setHideDisconnectScreen(boolean hide) {
        this.hideDisconnectScreen = hide;
    }

    public String getKickMessage() {
        return this.kickMessage;
    }

    public void setKickMessage(String message) {
        this.kickMessage = message;
    }

}
