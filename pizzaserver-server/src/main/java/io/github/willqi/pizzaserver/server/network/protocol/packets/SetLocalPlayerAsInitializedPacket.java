package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent by the client to notify the server that the client can be sent all packets.
 */
public class SetLocalPlayerAsInitializedPacket extends BaseBedrockPacket {

    public static final int ID = 0x71;

    private long runtimeEntityId;


    public SetLocalPlayerAsInitializedPacket() {
        super(ID);
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

}
