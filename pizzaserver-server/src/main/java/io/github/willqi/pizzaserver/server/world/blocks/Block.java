package io.github.willqi.pizzaserver.server.world.blocks;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;

public class Block {

    private final BlockType blockType;
    private int blockStateIndex = 0;

    private final Chunk chunk;
    private final Vector3i position;

    public Block(BlockType blockType, Chunk chunk, Vector3i position) {
        this.blockType = blockType;
        this.chunk = chunk;
        this.position = position;
    }

    public NBTCompound getBlockState() {
        return this.blockType.getBlockState(this.blockStateIndex);
    }

    public int getBlockStateIndex() {
        return this.blockStateIndex;
    }

    public void setBlockStateIndex(int index) {
        if (index >= this.getBlockType().getBlockStates().size() || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Attempted to access block state index " + index + " for block " + this.getBlockType().getBlockId() + " but none could be found.");
        }
        this.blockStateIndex = index;
    }

    public BlockType getBlockType() {
        return this.blockType;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

    public Vector3i getChunkPosition() {
        return this.position;
    }

    public int getLightLevel() {
        return 0;   // TODO
    }

}
