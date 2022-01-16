package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.DirtType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.ArrayList;
import java.util.List;

public class BlockDirt extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            this.add(NbtMap.builder().putString("dirt_type", "normal").build());
            this.add(NbtMap.builder().putString("dirt_type", "coarse").build());
        }
    };


    public BlockDirt() {
        this(DirtType.NORMAL);
    }

    public BlockDirt(DirtType dirtType) {
        this.setDirtType(dirtType);
    }

    public DirtType getDirtType() {
        return DirtType.values()[this.getBlockState()];
    }

    public void setDirtType(DirtType dirtType) {
        this.setBlockState(dirtType.ordinal());
    }

    @Override
    public String getBlockId() {
        return BlockID.DIRT;
    }

    @Override
    public String getName() {
        return "Dirt";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }
}
