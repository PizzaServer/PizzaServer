package io.github.willqi.pizzaserver.format.api.chunks.subchunks;

import io.github.willqi.pizzaserver.format.api.chunks.BedrockNetworkDiskSerializable;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.Set;

/**
 * A block palette holds the block states that this chunk is using.
 * Each block state used is assigned a index starting from 0 that is used
 * during network serialization to determine which block a {@link BlockPalette.Entry} is referring to.
 */
public interface BlockPalette extends BedrockNetworkDiskSerializable {

    /**
     * Create a new {@link BlockPalette.Entry} given the name and compound.
     * @param name the id of the block (e.g. minecraft:stone)
     * @param states an {@link NBTCompound} that contains the block state data
     * @param version current version of block state
     * @return the {@link BlockPalette.Entry} that represents the name and block state
     */
    Entry create(String name, NBTCompound states, int version);

    /**
     * Add a new block state to this palette.
     * @param data the {@link BlockPalette.Entry} to be added
     */
    void add(Entry data);

    /**
     * Retrieve the total palette size.
     * @return length of the palette
     */
    int getPaletteSize();

    /**
     * Retrieve all of the current palette {@link BlockPalette.Entry}s.
     * @return set of all block palette entries in this palette
     */
    Set<Entry> getAllEntries();

    /**
     * Remove a {@link BlockPalette.Entry} from the palette.
     * @param entry the {@link BlockPalette.Entry} to remove
     */
    void removeEntry(Entry entry);

    /**
     * Retrieve a {@link BlockPalette.Entry} given the index.
     * @param index the index of the {@link BlockPalette.Entry}
     * @return the {@link BlockPalette.Entry} associated with that index
     */
    Entry getEntry(int index);

    /**
     * Retrieve the index associated with the {@link BlockPalette.Entry}.
     * @param entry the {@link BlockPalette.Entry}
     * @return the index associated with the {@link BlockPalette.Entry}
     */
    int getPaletteIndex(Entry entry);

    /**
     * Represents an entry of the {@link BlockPalette}.
     */
    abstract class Entry {

        /**
         * The name of the block.
         * E.g. minecraft:air
         * @return id of the block
         */
        public abstract String getId();

        /**
         * Version of the block state.
         * @return version of the block state
         */
        public abstract int getVersion();

        /**
         * {@link NBTCompound} of the block state data.
         * @return {@link NBTCompound} containing block state data
         */
        public abstract NBTCompound getState();

        @Override
        public int hashCode() {
            return 47 * this.getId().hashCode() + 47 * this.getState().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Entry) {
                Entry entry = (Entry) obj;
                return entry.getState().equals(this.getState())
                        && entry.getId().equals(this.getId());
            }
            return false;
        }
    }

    /**
     * Helper class that represents an empty air entry.
     */
    class EmptyEntry extends Entry {

        @Override
        public String getId() {
            return "minecraft:air";
        }

        @Override
        public int getVersion() {
            return 0;
        }

        @Override
        public NBTCompound getState() {
            return new NBTCompound("states");
        }

    }

}
