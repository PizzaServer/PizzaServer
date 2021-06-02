package io.github.willqi.pizzaserver.server.network.protocol.packets;

public class DisconnectPacket extends BedrockPacket {

    public static final int ID = 0x05;

    private boolean hideDisconnectScreen;
    private String kickMessage = "Disconnected From Server";

    public DisconnectPacket() {
        super(ID);
    }

    public boolean isHidingDisconnectScreen() {
        return this.hideDisconnectScreen;
    }

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
