package io.github.willqi.pizzaserver.format.api.chunks.subchunks;

import io.github.willqi.pizzaserver.format.api.chunks.api.BedrockNetworkDiskSerializable;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.List;

public interface BlockPalette extends BedrockNetworkDiskSerializable {

    void add(NBTCompound data);

    List<Entry> getAllEntries();

    Entry getEntry(int index);


    class Entry {

        private final String name;
        private final int id;
        private final int version;
        private final NBTCompound state;


        public Entry(NBTCompound data, int id) {
            this.name = data.getString("name").getValue();
            this.version = data.getInteger("version").getValue();
            this.state = data.getCompound("states");
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public int getPaletteIndex() {
            return this.id;
        }

        public int getVersion() {
            return this.version;
        }

        public NBTCompound getState() {
            return this.state;
        }


    }

}
