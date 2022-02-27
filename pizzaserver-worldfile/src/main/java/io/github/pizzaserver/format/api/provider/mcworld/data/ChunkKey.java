package io.github.pizzaserver.format.api.provider.mcworld.data;

public enum ChunkKey {
    /**
     * Single byte that contains the chunk version.
     */
    VERSION(0x2c),

    /**
     * Old key that used to store the chunk version.
     */
    OLD_VERSION(0x76),

    /**
     * 2D height map/biome of a chunk.
     * Replaced with DATA_3D in newer versions.
     */
    DATA_2D(0x2d),

    /**
     * height map & 3D biome data of a chunk.
     */
    DATA_3D(0x2b),

    /**
     * Stores specific sub chunk data.
     */
    SUB_CHUNK_DATA(0x2f),

    /**
     * Stores block entities in a chunk.
     */
    BLOCK_ENTITIES(0x31),

    /**
     * Stores entities in a chunk.
     */
    ENTITIES(0x32),

    /**
     * Used to represent the state of a chunk.
     */
    FINALIZATION(0x36);


    private final int id;


    ChunkKey(int id) {
        this.id = id;
    }

    public byte[] getLevelDBKey(int x, int z) {
        return new byte[]{
                (byte) (x & 0xff), (byte) ((x >> 8) & 0xff), (byte) ((x >> 16) & 0xff), (byte) ((x >> 24) & 0xff),
                (byte) (z & 0xff), (byte) ((z >> 8) & 0xff), (byte) ((z >> 16) & 0xff), (byte) ((z >> 24) & 0xff),
                (byte) this.id
        };
    }

    public byte[] getLevelDBKey(int x, int z, int extra) {
        return new byte[]{
                (byte) (x & 0xff), (byte) ((x >> 8) & 0xff), (byte) ((x >> 16) & 0xff), (byte) ((x >> 24) & 0xff),
                (byte) (z & 0xff), (byte) ((z >> 8) & 0xff), (byte) ((z >> 16) & 0xff), (byte) ((z >> 24) & 0xff),
                (byte) this.id, (byte) extra
        };
    }

    public byte[] getLevelDBKeyWithDimension(int x, int z, int dimension) {
        return new byte[]{
                (byte) (x & 0xff), (byte) ((x >> 8) & 0xff), (byte) ((x >> 16) & 0xff), (byte) ((x >> 24) & 0xff),
                (byte) (z & 0xff), (byte) ((z >> 8) & 0xff), (byte) ((z >> 16) & 0xff), (byte) ((z >> 24) & 0xff),
                (byte) (dimension & 0xff), (byte) ((dimension >> 8) & 0xff), (byte) ((dimension >> 16) & 0xff), (byte) ((dimension >> 24) & 0xff),
                (byte) this.id
        };
    }

    public byte[] getLevelDBKeyWithDimension(int x, int z, int dimension, int extra) {
        return new byte[]{
                (byte) (x & 0xff), (byte) ((x >> 8) & 0xff), (byte) ((x >> 16) & 0xff), (byte) ((x >> 24) & 0xff),
                (byte) (z & 0xff), (byte) ((z >> 8) & 0xff), (byte) ((z >> 16) & 0xff), (byte) ((z >> 24) & 0xff),
                (byte) (dimension & 0xff), (byte) ((dimension >> 8) & 0xff), (byte) ((dimension >> 16) & 0xff), (byte) ((dimension >> 24) & 0xff),
                (byte) this.id, (byte) extra
        };
    }

}
