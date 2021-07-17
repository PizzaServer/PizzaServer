package io.github.willqi.pizzaserver.format.api.chunks.subchunks;

import io.github.willqi.pizzaserver.format.api.chunks.api.BedrockNetworkDiskSerializable;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.List;

public interface BlockPalette extends BedrockNetworkDiskSerializable {

    void add(NBTCompound data);

    List<Entry> getAllEntries();

    boolean hasEntry(NBTCompound data);

    Entry getEntry(int index);


    /**
     * Represents an entry in this block palette
     */
    interface Entry {

        String getId();

        int getPaletteIndex();

        int getVersion();

        NBTCompound getState();


    }

}
