package io.github.willqi.pizzaserver.format.mcworld.world.chunks;

import io.github.willqi.pizzaserver.commons.world.Dimension;
import io.github.willqi.pizzaserver.format.exceptions.world.chunks.NoChunkFoundException;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.iq80.leveldb.DB;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MCChunkDatabase implements Closeable {

    private final DB database;

    private final Map<Dimension, Integer> dimensionIntegerMap = new HashMap<Dimension, Integer>() {
        {
            this.put(Dimension.OVERWORLD, null);
            this.put(Dimension.NETHER, 1);
            this.put(Dimension.END, 2);
        }
    };


    public MCChunkDatabase(DB database) {
        this.database = database;
    }

    public MCWorldChunk getChunk(int x, int z, Dimension dimension) throws IOException {
        // First extract the chunk version
        byte[] versionData = this.database.get(ifOverworld(
                dimension,
                () -> ChunkKey.VERSION.getLevelDBKey(x, z),
                () -> ChunkKey.VERSION.getLevelDBKeyWithDimension(x, z, this.dimensionIntegerMap.get(dimension))));

        if (versionData == null) {
            versionData = this.database.get(ifOverworld(
                    dimension,
                    () -> ChunkKey.OLD_VERSION.getLevelDBKey(x, z),
                    () -> ChunkKey.OLD_VERSION.getLevelDBKeyWithDimension(x, z, this.dimensionIntegerMap.get(dimension))));
            if (versionData == null) {
                throw new NoChunkFoundException("Could not find a chunk at (" + x + ", " + z + ")");
            }
        }
        int chunkVersion = versionData[0];

        // Extract height map and biome data
        byte[] heightAndBiomeData = this.database.get(ifOverworld(
                dimension,
                () -> ChunkKey.DATA_2D.getLevelDBKey(x, z),
                () -> ChunkKey.DATA_2D.getLevelDBKeyWithDimension(x, z, this.dimensionIntegerMap.get(dimension))));

        // Extract block entities within this chunk
        byte[] blockEntityData = this.database.get(ifOverworld(
                dimension,
                () -> ChunkKey.BLOCK_ENTITIES.getLevelDBKey(x, z),
                () -> ChunkKey.BLOCK_ENTITIES.getLevelDBKeyWithDimension(x, z, this.dimensionIntegerMap.get(dimension))));
        if (blockEntityData == null) {
            blockEntityData = new byte[0];  // Not all chunks will have block entities.
        }

        // Extract entities within this chunk
        byte[] entityData = this.database.get(ifOverworld(
                dimension,
                () -> ChunkKey.ENTITIES.getLevelDBKey(x, z),
                () -> ChunkKey.ENTITIES.getLevelDBKeyWithDimension(x, z, this.dimensionIntegerMap.get(dimension))));
        if (entityData == null) {
            entityData = new byte[0];   // Not all chunks have entities.
        }

        // Extract subchunks
        byte[][] subChunks = new byte[16][];
        for (int y = 0; y < 16; y++) {
            final int subChunkY = y;
            byte[] subChunk = this.database.get(ifOverworld(
                    dimension,
                    () -> ChunkKey.SUB_CHUNK_DATA.getLevelDBKey(x, z, subChunkY),
                    () -> ChunkKey.SUB_CHUNK_DATA.getLevelDBKeyWithDimension(x, z, this.dimensionIntegerMap.get(dimension), subChunkY)));
            if (subChunk == null) {
                subChunks[y] = new byte[]{8, 0};    // empty v8 chunk
            } else {
                subChunks[y] = subChunk;
            }
        }

        return new MCWorldChunk.Builder()
                .setX(x)
                .setZ(z)
                .setDimension(dimension)
                .setChunkVersion(chunkVersion)
                .setHeightAndBiomeData(heightAndBiomeData)
                .setBlockEntityData(blockEntityData)
                .setEntityData(entityData)
                .setSubChunks(subChunks)
                .build();
    }

    public void saveChunk(MCWorldChunk bedrockChunk) throws IOException {
        byte[] chunkVersionKey = ifOverworld(
                bedrockChunk.getDimension(),
                () -> ChunkKey.VERSION.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()),
                () -> ChunkKey.VERSION.getLevelDBKeyWithDimension(
                        bedrockChunk.getX(),
                        bedrockChunk.getZ(),
                        this.dimensionIntegerMap.get(bedrockChunk.getDimension())));
        this.database.put(chunkVersionKey, new byte[]{ (byte) bedrockChunk.getChunkVersion() });


        // Data2D (height map + biome)
        ByteBuf heightAndBiomeBuffer = ByteBufAllocator.DEFAULT.buffer(768, 768);    // 512 + 256
        for (int height : bedrockChunk.getHeightMap()) {
            heightAndBiomeBuffer.writeShortLE(height);
        }
        heightAndBiomeBuffer.writeBytes(bedrockChunk.getBiomeData());

        byte[] heightAndBiomeData = new byte[768];
        heightAndBiomeBuffer.readBytes(heightAndBiomeBuffer);
        heightAndBiomeBuffer.release();

        byte[] data2DKey = ifOverworld(
                bedrockChunk.getDimension(),
                () -> ChunkKey.DATA_2D.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()),
                () -> ChunkKey.DATA_2D.getLevelDBKeyWithDimension(
                        bedrockChunk.getX(),
                        bedrockChunk.getZ(),
                        this.dimensionIntegerMap.get(bedrockChunk.getDimension())));
        this.database.put(data2DKey, heightAndBiomeData);


        // block entities
        ByteArrayOutputStream blockEntityOutput = new ByteArrayOutputStream();
        NBTOutputStream blockEntityNBTOutputStream = new NBTOutputStream(new LittleEndianDataOutputStream(blockEntityOutput));
        for (NBTCompound blockEntityCompound : bedrockChunk.getBlockEntityNBTs()) {
            blockEntityNBTOutputStream.writeCompound(blockEntityCompound);
        }

        byte[] blockEntitiesKey = ifOverworld(
                bedrockChunk.getDimension(),
                () -> ChunkKey.BLOCK_ENTITIES.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()),
                () -> ChunkKey.BLOCK_ENTITIES.getLevelDBKeyWithDimension(
                        bedrockChunk.getX(),
                        bedrockChunk.getZ(),
                        this.dimensionIntegerMap.get(bedrockChunk.getDimension())));
        this.database.put(blockEntitiesKey, blockEntityOutput.toByteArray());


        // entities
        ByteArrayOutputStream entityOutput = new ByteArrayOutputStream();
        NBTOutputStream entityNBTOutputStream = new NBTOutputStream(new LittleEndianDataOutputStream(entityOutput));
        for (NBTCompound entityCompound : bedrockChunk.getEntityNBTs()) {
            entityNBTOutputStream.writeCompound(entityCompound);
        }

        byte[] entityKey = ifOverworld(
                bedrockChunk.getDimension(),
                () -> ChunkKey.ENTITIES.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()),
                () -> ChunkKey.ENTITIES.getLevelDBKeyWithDimension(
                        bedrockChunk.getX(),
                        bedrockChunk.getZ(),
                        this.dimensionIntegerMap.get(bedrockChunk.getDimension())));
        this.database.put(entityKey, entityOutput.toByteArray());


        // sub chunks
        for (int y = 0; y < 16; y++) {
            final int chunkY = y;

            byte[] subChunk = bedrockChunk.getSubChunk(chunkY).serializeForDisk();
            byte[] subChunkKey = ifOverworld(
                    bedrockChunk.getDimension(),
                    () -> ChunkKey.SUB_CHUNK_DATA.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ(), chunkY),
                    () -> ChunkKey.SUB_CHUNK_DATA.getLevelDBKeyWithDimension(
                            bedrockChunk.getX(),
                            bedrockChunk.getZ(),
                            this.dimensionIntegerMap.get(bedrockChunk.getDimension()),
                            chunkY));
            this.database.put(subChunkKey, subChunk);
        }

    }

    /**
     * Returns a chunk key depending on whether or not the dimension is an overworld.
     * @param dimension dimension we are checking
     * @param overworldCallback if this dimension is an overworld
     * @param otherCallback if this dimension isn't an overworld
     * @return bytes representative of the key
     */
    private static byte[] ifOverworld(Dimension dimension, Supplier<byte[]> overworldCallback, Supplier<byte[]> otherCallback) {
        return dimension == Dimension.OVERWORLD ? overworldCallback.get() : otherCallback.get();
    }

    @Override
    public void close() throws IOException {
        this.database.close();
    }

}
