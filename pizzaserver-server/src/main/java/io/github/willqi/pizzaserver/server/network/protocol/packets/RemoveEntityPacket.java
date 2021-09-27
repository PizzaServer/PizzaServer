package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent to remove an entity.
 */
public class RemoveEntityPacket extends BaseBedrockPacket {

    public static final int ID = 0x0e;

    private long uniqueEntityId;


    public RemoveEntityPacket() {
        super(ID);
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }



}
