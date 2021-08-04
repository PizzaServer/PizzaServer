package io.github.willqi.pizzaserver.server.network.protocol.packets;

import java.util.UUID;

/**
 * Contains a portion of the data of a resource pack
 */
public class ResourcePackChunkDataPacket extends ImplBedrockPacket {

    public static final int ID = 0x53;

    private UUID uuid;
    private int chunkIndex;
    private long chunkProgress;
    private byte[] data;


    public ResourcePackChunkDataPacket() {
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

    public long getChunkProgress() {
        return this.chunkProgress;
    }

    public void setChunkProgress(long progress) {
        this.chunkProgress = progress;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
