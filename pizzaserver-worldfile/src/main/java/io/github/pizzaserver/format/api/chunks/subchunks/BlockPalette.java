package io.github.pizzaserver.format.api.chunks.subchunks;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.format.api.chunks.BedrockNetworkDiskSerializable;

import java.util.Set;

/**
 * A block palette holds the block states that this chunk is using.
 * Each block state used is assigned a index starting from 0 that is used
 * during network serialization to determine which block a {@link BlockPalette.Entry} is referring to.
 */
public interface BlockPalette extends BedrockNetworkDiskSerializable {

    /**
     * Create a new {@link BlockPalette.Entry} given the name and compound.
     *
     * @param name    the id of the block (e.g. minecraft:stone)
     * @param states  an {@link NbtMap} that contains the block state data
     * @param version current version of block state
     * @return the {@link BlockPalette.Entry} that represents the name and block state
     */
    Entry create(String name, NbtMap states, int version);

    /**
     * Add a new block state to this palette.
     *
     * @param data the {@link BlockPalette.Entry} to be added
     */
    void add(Entry data);

    /**
     * Retrieve the total palette size.
     *
     * @return length of the palette
     */
    int size();

    /**
     * Retrieve all of the current palette {@link BlockPalette.Entry}s.
     *
     * @return set of all block palette entries in this palette
     */
    Set<Entry> getEntries();

    /**
     * Remove a {@link BlockPalette.Entry} from the palette.
     *
     * @param entry the {@link BlockPalette.Entry} to remove
     */
    void removeEntry(Entry entry);

    /**
     * Retrieve a {@link BlockPalette.Entry} given the index.
     *
     * @param index the index of the {@link BlockPalette.Entry}
     * @return the {@link BlockPalette.Entry} associated with that index
     */
    Entry getEntry(int index);

    /**
     * Retrieve the index associated with the {@link BlockPalette.Entry}.
     *
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
         *
         * @return id of the block
         */
        public abstract String getId();

        /**
         * Version of the block state.
         *
         * @return version of the block state
         */
        public abstract int getVersion();

        /**
         * {@link NbtMap} of the block state data.
         *
         * @return {@link NbtMap} containing block state data
         */
        public abstract NbtMap getState();

        @Override
        public int hashCode() {
            return 47 * this.getId().hashCode() + 47 * this.getState().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Entry) {
                Entry entry = (Entry) obj;
                return entry.getState().equals(this.getState()) && entry.getId().equals(this.getId());
            }
            return false;
        }
    }


    /**
     * Helper class that represents an empty air entry.
     */
    class EmptyEntry extends Entry {

        public static final String ID = "minecraft:air";

        @Override
        public String getId() {
            return ID;
        }

        @Override
        public int getVersion() {
            return 0;
        }

        @Override
        public NbtMap getState() {
            return NbtMap.EMPTY;
        }
    }
}
