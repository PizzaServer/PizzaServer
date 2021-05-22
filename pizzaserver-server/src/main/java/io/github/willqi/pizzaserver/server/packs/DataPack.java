package io.github.willqi.pizzaserver.server.packs;

import java.util.UUID;

public interface DataPack {

    // amount of data we send each chunk of data is at max.
    int CHUNK_LENGTH = 1048576;

    UUID getUuid();
    String getVersion();
    long getDataLength();
    int getChunkCount();
    byte[] getHash();
    byte[] getChunk(int index);

}
