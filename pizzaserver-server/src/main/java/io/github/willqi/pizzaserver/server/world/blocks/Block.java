package io.github.willqi.pizzaserver.server.world.blocks;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockType;

public class Block {

    private final BlockType blockType;
    private int blockStateIndex = 0;

    public Block(BlockType blockType) {
        this.blockType = blockType;
    }

    /**
     * The block state data of the block
     * This is equivalent to a block state stored in the block_states.nbt file
     * @return {@link NBTCompound} of the block state data
     */
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

}
