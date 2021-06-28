package io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.mcworld.world.chunks.versions.SubChunkVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;


public class BedrockSubChunk {

    private final int subChunkVersion;
    private final Layer[] layers;


    public BedrockSubChunk(int subChunkVersion, Layer[] layers) {
        this.subChunkVersion = subChunkVersion;
        this.layers = layers;
    }

    public int getSubChunkVersion() {
        return this.subChunkVersion;
    }

    public Layer getLayer(int layer) {
        return this.layers[layer];
    }

    public Layer[] getLayers() {
        return this.layers;
    }

    public byte[] serialize() throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeByte(this.subChunkVersion);
        buffer.writeBytes(SubChunkVersion.getSubChunkVersion(this.subChunkVersion).serialize(this));

        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);

        buffer.release();
        return serialized;
    }


    public static class Layer {

        // x y z
        private final BlockPalette.BlockPaletteData[] data;

        private final BlockPalette palette;


        public Layer(RawBlock[] blocks, BlockPalette palette) {
            this.data = new BlockPalette.BlockPaletteData[blocks.length];
            this.palette = palette;
            for (int i = 0; i < blocks.length; i++) {
                this.data[i] = palette.getPaletteData(blocks[i].getPaletteId());
            }
        }

        public BlockPalette.BlockPaletteData getBlock(Vector3i blockPosition) {
            return this.getBlock(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        }

        public BlockPalette.BlockPaletteData getBlock(int x, int y, int z) {
            return this.data[(x << 8) + (z << 4) | y];
        }

        public BlockPalette.BlockPaletteData[] getBlocks() {
            return this.data;
        }

        public BlockPalette getPalette() {
            return this.palette;
        }

    }

    /**
     * Represents the raw data retrieved from parsing the sub chunk data. (paletteId and it's position)
     */
    public static class RawBlock {

        private final int paletteId;
        private final Vector3i position;


        public RawBlock(int paletteId, Vector3i position) {
            this.paletteId = paletteId;
            this.position = position;
        }

        public int getPaletteId() {
            return this.paletteId;
        }

        public Vector3i getPosition() {
            return this.position;
        }

    }

}
