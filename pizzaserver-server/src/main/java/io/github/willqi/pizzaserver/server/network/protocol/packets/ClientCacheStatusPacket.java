package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent to the server to notify it if the client supports chunk caching.
 */
public class ClientCacheStatusPacket extends BaseBedrockPacket {

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
