package io.github.pizzaserver.format.dimension.chunks.subchunk;

import org.cloudburstmc.nbt.NbtMap;

/**
 * These entries represent a block state that exists within the block palette.
 */
public class BlockPaletteEntry {

    public static final String AIR_ID = "minecraft:air";

    private final String id;
    private final int version;
    private final NbtMap state;


    public BlockPaletteEntry(String id, int version, NbtMap state) {
        this.id = id;
        this.version = version;
        this.state = state;
    }

    public BlockPaletteEntry() {
        this(AIR_ID, 0, NbtMap.EMPTY);
    }

    /**
     * The name of the block.
     * E.g. minecraft:air
     * @return id of the block
     */
    public String getId() {
        return this.id;
    }

    /**
     * Version of the block state.
     * @return version of the block state
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * {@link NbtMap} of the block state data.
     * @return {@link NbtMap} containing block state data
     */
    public NbtMap getState() {
        return this.state;
    }

    @Override
    public int hashCode() {
        return 47 * this.getId().hashCode() + 47 * this.getState().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockPaletteEntry entry) {
            return entry.getState().equals(this.getState())
                    && entry.getId().equals(this.getId());
        }
        return false;
    }

}
