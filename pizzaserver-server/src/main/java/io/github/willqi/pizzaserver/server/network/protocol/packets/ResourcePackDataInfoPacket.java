package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

import java.util.UUID;

/**
 * Sent when the client requests the information about a resource pack
 */
public class ResourcePackDataInfoPacket extends BaseBedrockPacket {

    public static final int ID = 0x52;

    private UUID uuid;
    private String version;
    private PackType type;
    private byte[] hash;
    private boolean premium;

    private long compressedPackageSize;
    private int maxChunkSize;
    private int chunkCount;


    public ResourcePackDataInfoPacket() {
        super(ID);
    }

    /**
     * Get the UUID of the resource pack found in the resource pack's manifest
     * @return UUID
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Change the UUID of the the resource pack
     * @param uuid UUID
     */
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the version of the resource pack found in the resource pack's manifest
     * @return version of the pack (e.g. 1.0.0)
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Change the version of the resource pack
     * @param version version of the pack (e.g. 1.0.0)
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * What type of downloadable content is this
     * @return the downloaded content type
     */
    public PackType getType() {
        return this.type;
    }

    /**
     * Change the type of downloadable content this is
     * @param type new type
     */
    public void setType(PackType type) {
        this.type = type;
    }

    /**
     * SHA-256 of the pack's contents
     * @return byte array of the hash
     */
    public byte[] getHash() {
        return this.hash;
    }

    /**
     * Change the SHA-256 of the pack's contents
     * @param hash byte array of the hash
     */
    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    /**
     * Get the size of the resource pack
     * @return size of the pack
     */
    public long getCompressedPackageSize() {
        return this.compressedPackageSize;
    }

    /**
     * Change the size of the resource pack
     * @param size size of the pack
     */
    public void setCompressedPackageSize(long size) {
        this.compressedPackageSize = size;
    }

    /**
     * Get the maximum size a ResourcePackChunkDataPacket would return for a chunk
     * @return maximum size returned
     */
    public int getMaxChunkSize() {
        return this.maxChunkSize;
    }

    /**
     * Change the maximum size a ResourcePackChunkDataPacket would return for a chunk
     * @param maxChunkSize maximum size
     */
    public void setMaxChunkSize(int maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
    }

    /**
     * Get the amount of chunks this pack is split into
     * @return amount of chunks
     */
    public int getChunkCount() {
        return this.chunkCount;
    }

    /**
     * Change the amount of chunks this pack is split into
     * @param count amount of chunks
     */
    public void setChunkCount(int count) {
        this.chunkCount = count;
    }


    public enum PackType {
        INVALID,
        RESOURCE_PACK,
        BEHAVIOR_PACK
    }

}
