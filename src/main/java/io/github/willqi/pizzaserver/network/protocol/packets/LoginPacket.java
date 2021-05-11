package io.github.willqi.pizzaserver.network.protocol.packets;

public class LoginPacket extends BedrockPacket {

    public static final int ID = 0x01;

    private int protocol;

    public int getProtocol() {
        return this.protocol;
    }

    public LoginPacket setProtocol(int protocol) {
        this.protocol = protocol;
        return this;
    }


}
