package io.github.willqi.pizzaserver.server.network.protocol.packets;

import java.util.UUID;

/**
 * Sent when the client requests the information about a resource pack
 */
public class ResourcePackDataInfoPacket extends BedrockPacket {

    public static final int ID = 0x52;

    private UUID id;
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

    public UUID getPackId() {
        return this.id;
    }

    public void setPackId(UUID id) {
        this.id = id;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public PackType getType() {
        return this.type;
    }

    public void setType(PackType type) {
        this.type = type;
    }

    public byte[] getHash() {
        return this.hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public long getCompressedPackageSize() {
        return this.compressedPackageSize;
    }

    public void setCompressedPackageSize(long size) {
        this.compressedPackageSize = size;
    }

    public int getMaxChunkSize() {
        return this.maxChunkSize;
    }

    public void setMaxChunkSize(int maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
    }

    public int getChunkCount() {
        return this.chunkCount;
    }

    public void setChunkCount(int count) {
        this.chunkCount = count;
    }


    public enum PackType {
        RESOURCE_PACK,
        BEHAVIOR_PACK
    }

}
