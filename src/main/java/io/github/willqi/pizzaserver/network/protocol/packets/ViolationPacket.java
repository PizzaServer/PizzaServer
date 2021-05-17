package io.github.willqi.pizzaserver.network.protocol.packets;

public class ViolationPacket extends BedrockPacket {

    public static final int ID = 0x9c;

    public ViolationPacket() {
        super(ID);
    }

}
