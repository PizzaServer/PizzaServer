package io.github.willqi.pizzaserver.network.protocol.packets;

public class ClientCacheStatusPacket extends BedrockPacket {

    public static final int ID = 0x81;

    private boolean supported;

    public ClientCacheStatusPacket() {
        super(ID);
    }

    public boolean isSupported() {
        return this.supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

}
