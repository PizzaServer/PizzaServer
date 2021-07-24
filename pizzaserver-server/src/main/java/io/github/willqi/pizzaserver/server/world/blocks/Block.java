package io.github.willqi.pizzaserver.server.world.blocks;

import io.github.willqi.pizzaserver.api.world.blocks.APIBlock;
import io.github.willqi.pizzaserver.api.world.blocks.types.APIBlockType;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public class Block implements APIBlock {

    private final APIBlockType blockType;
    private int blockStateIndex = 0;

    public Block(APIBlockType blockType) {
        this.blockType = blockType;
    }

    @Override
    public APIBlockType getBlockType() {
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
