package io.github.willqi.pizzaserver.server.level.world.blocks;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public class ImplBlock implements Block {

    private final BlockType blockType;
    private int blockStateIndex = 0;

    public ImplBlock(BlockType blockType) {
        this.blockType = blockType;
    }

    @Override
    public BlockType getBlockType() {
        return this.blockType;
    }

    @Override
    public NBTCompound getBlockState() {
        return this.blockType.getBlockState(this.blockStateIndex);
    }

    @Override
    public int getBlockStateIndex() {
        return this.blockStateIndex;
    }

    @Override
    public void setBlockStateIndex(int index) {
        if (index >= this.getBlockType().getBlockStates().size() || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Attempted to access block state index " + index + " for block " + this.getBlockType().getBlockId() + " but none could be found.");
        }
        this.blockStateIndex = index;
    }

}
