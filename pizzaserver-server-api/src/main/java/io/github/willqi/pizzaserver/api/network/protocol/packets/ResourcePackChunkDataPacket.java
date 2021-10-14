package io.github.willqi.pizzaserver.api.network.protocol.packets;

import java.util.UUID;

/**
 * Contains a portion of the data of a resource pack.
 * Sent in response to a ResourcePackChunkRequest packet
 */
public class ResourcePackChunkDataPacket extends BaseBedrockPacket {

    public static final int ID = 0x53;

    private UUID uuid;
    private int chunkIndex;
    private long chunkProgress;
    private byte[] data;


    public ResourcePackChunkDataPacket() {
        super(ID);
    }

    /**
     * Get the UUID of the resource pack.
     * @return UUID of the resource pack
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Change the UUID of the resource pack.
     * @param uuid UUID of the resource pack
     */
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Change the section of the resource pack this packet represents.
     * @return section of the resource pack
     */
    public int getChunkIndex() {
        return this.chunkIndex;
    }

    /**
     * Change the section this resource pack represents.
     * @param index section of the resource pack
     */
    public void setChunkIndex(int index) {
        this.chunkIndex = index;
    }

    /**
     * Get the offset of the resource pack this packet represents.
     * @return pack offset
     */
    public long getChunkProgress() {
        return this.chunkProgress;
    }

    /**
     * Set the offset of the resource pack this packet represents.
     * @param progress pack offset
     */
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
