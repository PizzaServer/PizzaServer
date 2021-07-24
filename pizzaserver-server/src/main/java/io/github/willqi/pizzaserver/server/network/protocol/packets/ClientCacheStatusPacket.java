package io.github.willqi.pizzaserver.server.network.protocol.packets;

/**
 * Whether or not the client supports chunk caching
 */
public class ClientCacheStatusPacket extends BedrockNetworkPacket {

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
