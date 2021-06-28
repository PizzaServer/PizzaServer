package io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;


public class BedrockSubChunk {

    private final Layer[] layers;


    public BedrockSubChunk(Layer[] layers) {
        this.layers = layers;
    }

    public Layer getLayer(int layer) {
        return this.layers[layer];
    }


    public static class Layer {

        // x y z
        private final BlockPalette.BlockPaletteData[][][] data = new BlockPalette.BlockPaletteData[16][16][16];


        public Layer(RawBlock[] blocks, BlockPalette palette) {
            for (RawBlock block : blocks) {
                this.data[block.getPosition().getX()][block.getPosition().getY()][block.getPosition().getZ()] = palette.getPaletteData(block.getPaletteId());
            }
        }

        public BlockPalette.BlockPaletteData getBlock(Vector3i blockPosition) {
            return this.getBlock(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        }

        public BlockPalette.BlockPaletteData getBlock(int x, int y, int z) {
            return this.data[x][y][z];
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
