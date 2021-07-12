package io.github.willqi.pizzaserver.format.api.chunks.subchunks;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.format.api.chunks.api.BedrockNetworkDiskSerializable;

public interface BlockLayer extends BedrockNetworkDiskSerializable {

    /**
     * Represents the raw data retrieved from parsing the sub chunk data. (paletteId and it's position)
     */
    class RawBlock {

        private final BlockPalette.Entry paletteEntry;
        private final Vector3i position;


        public RawBlock(BlockPalette.Entry entry, Vector3i position) {
            this.paletteEntry = entry;
            this.position = position;
        }

        public BlockPalette.Entry getPaletteEntry() {
            return this.paletteEntry;
        }

        public Vector3i getPosition() {
            return this.position;
        }

    }

}
