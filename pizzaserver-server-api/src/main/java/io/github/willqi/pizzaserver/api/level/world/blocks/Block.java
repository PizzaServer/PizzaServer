package io.github.willqi.pizzaserver.api.level.world.blocks;

import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public class Block {

    private final BaseBlockType blockType;
    private int blockStateIndex = 0;


    public Block(BaseBlockType blockType) {
        this.blockType = blockType;
    }

    public BaseBlockType getBlockType() {
        return this.blockType;
    }

    /**
     * The block state data of the block
     * This is equivalent to a block state stored in the block_states.nbt file
     * @return {@link NBTCompound} of the block state data
     */
    public NBTCompound getBlockState() {
        return this.getBlockType().getBlockState(this.blockStateIndex);
    }

    public int getBlockStateIndex() {
        return this.blockStateIndex;
    }

    public void setBlockStateIndex(int index) {
        this.blockStateIndex = index;
    }

}
