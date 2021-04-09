package io.github.willqi.pizzaserver.resourcepacks;

import java.util.UUID;

public interface ResourcePack {

    // amount of data do we send per ResourcePackChunkData packet (1MB)
    int CHUNK_LENGTH = 1048576;

    UUID getUuid();
    String getVersion();
    long getDataLength();
    int getChunkCount();
    byte[] getHash();
    byte[] getChunk(int index);

}
