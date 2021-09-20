package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

import java.util.UUID;

/**
 * Sent by the client to request a chunk of a resource pack.
 */
public class ResourcePackChunkRequestPacket extends BaseBedrockPacket {

    public static final int ID = 0x54;

    private UUID uuid;
    private int chunkIndex;


    public ResourcePackChunkRequestPacket() {
        super(ID);
    }

    /**
     * Get the UUID of the resource pack requested.
     * @return UUID
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Change the UUID of the resource pack requested.
     * @param uuid UUID
     */
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the section of the pack the client is requesting.
     * @return section index
     */
    public int getChunkIndex() {
        return this.chunkIndex;
    }

    /**
     * Change thes ection of the pack that the client is requesting.
     * @param index section index
     */
    public void setChunkIndex(int index) {
        this.chunkIndex = index;
    }

}
