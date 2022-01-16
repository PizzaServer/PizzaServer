package io.github.pizzaserver.format.mcworld.world.chunks;

public enum ChunkKey {

    VERSION(0x2c),
    OLD_VERSION(0x76),
    DATA_2D(0x2d),
    SUB_CHUNK_DATA(0x2f),
    BLOCK_ENTITIES(0x31),
    ENTITIES(0x32);


    private final int id;


    ChunkKey(int id) {
        this.id = id;
    }

    public byte[] getLevelDBKey(int x, int z) {
        return new byte[] {
                (byte) (x & 0xff),
                (byte) ((x >> 8) & 0xff),
                (byte) ((x >> 16) & 0xff),
                (byte) ((x >> 24) & 0xff),
                (byte) (z & 0xff),
                (byte) ((z >> 8) & 0xff),
                (byte) ((z >> 16) & 0xff),
                (byte) ((z >> 24) & 0xff),
                (byte) this.id
        };
    }

    public byte[] getLevelDBKey(int x, int z, int extra) {
        return new byte[] {
                (byte) (x & 0xff),
                (byte) ((x >> 8) & 0xff),
                (byte) ((x >> 16) & 0xff),
                (byte) ((x >> 24) & 0xff),
                (byte) (z & 0xff),
                (byte) ((z >> 8) & 0xff),
                (byte) ((z >> 16) & 0xff),
                (byte) ((z >> 24) & 0xff),
                (byte) this.id,
                (byte) extra
        };
    }

    public byte[] getLevelDBKeyWithDimension(int x, int z, int dimension) {
        return new byte[] {
                (byte) (x & 0xff),
                (byte) ((x >> 8) & 0xff),
                (byte) ((x >> 16) & 0xff),
                (byte) ((x >> 24) & 0xff),
                (byte) (z & 0xff),
                (byte) ((z >> 8) & 0xff),
                (byte) ((z >> 16) & 0xff),
                (byte) ((z >> 24) & 0xff),
                (byte) (dimension & 0xff),
                (byte) ((dimension >> 8) & 0xff),
                (byte) ((dimension >> 16) & 0xff),
                (byte) ((dimension >> 24) & 0xff),
                (byte) this.id
        };
    }

    public byte[] getLevelDBKeyWithDimension(int x, int z, int dimension, int extra) {
        return new byte[] {
                (byte) (x & 0xff),
                (byte) ((x >> 8) & 0xff),
                (byte) ((x >> 16) & 0xff),
                (byte) ((x >> 24) & 0xff),
                (byte) (z & 0xff),
                (byte) ((z >> 8) & 0xff),
                (byte) ((z >> 16) & 0xff),
                (byte) ((z >> 24) & 0xff),
                (byte) (dimension & 0xff),
                (byte) ((dimension >> 8) & 0xff),
                (byte) ((dimension >> 16) & 0xff),
                (byte) ((dimension >> 24) & 0xff),
                (byte) this.id,
                (byte) extra
        };
    }
}
