package io.github.willqi.pizzaserver.format.api.chunks.subchunks;

import io.github.willqi.pizzaserver.format.api.chunks.api.BedrockNetworkDiskSerializable;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.Set;

public interface BlockPalette extends BedrockNetworkDiskSerializable {

    void add(NBTCompound data);

    int getPaletteSize();

    Set<Entry> getAllEntries();

    void removeEntry(Entry entry);

    Entry getEntry(int index);

    int getPaletteIndex(Entry entry);


    /**
     * Represents an entry in this block palette
     */
    abstract class Entry {

        public abstract String getId();

        public abstract int getVersion();

        public abstract NBTCompound getState();

        @Override
        public int hashCode() {
            return 47 * this.getId().hashCode() + 47 * this.getState().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Entry) {
                Entry entry = (Entry)obj;
                return entry.getState().equals(this.getState()) &&
                        entry.getId().equals(this.getId()) &&
                        entry.getVersion() == this.getVersion();
            }
            return false;
        }
    }

}
