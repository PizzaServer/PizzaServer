package io.github.willqi.pizzaserver.server.network.protocol.packets;

public class AvailableCommandsPacket extends ImplBedrockPacket {

    public static final int ID = 0x4c;

    public AvailableCommandsPacket() {
        super(ID);
    }
}
