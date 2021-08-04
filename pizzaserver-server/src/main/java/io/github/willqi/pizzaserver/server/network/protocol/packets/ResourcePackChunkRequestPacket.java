package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

import java.util.UUID;

public class ResourcePackChunkRequestPacket extends BaseBedrockPacket {

    public static final int ID = 0x54;

    private UUID uuid;
    private int chunkIndex;


    public ResourcePackChunkRequestPacket() {
        super(ID);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public int getChunkIndex() {
        return this.chunkIndex;
    }

    public void setChunkIndex(int index) {
        this.chunkIndex = index;
    }

}
