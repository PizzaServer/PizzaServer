package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.StoneType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockStone extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            Arrays.asList("stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth")
                    .forEach(stoneType -> this.add(NbtMap.builder().putString("stone_type", stoneType).build()));
        }
    };


    public BlockStone() {
        this(StoneType.STONE);
    }

    public BlockStone(StoneType stoneType) {
        this.setStoneType(stoneType);
    }

    public StoneType getStoneType() {
        return StoneType.values()[this.getBlockState()];
    }

    public void setStoneType(StoneType stoneType) {
        this.setBlockState(stoneType.ordinal());
    }

    @Override
    public String getBlockId() {
        return BlockID.STONE;
    }

    @Override
    public String getName() {
        return switch (this.getStoneType()) {
            case STONE -> "Stone";
            case GRANITE -> "Granite";
            case GRANITE_SMOOTH -> "Smooth Granite";
            case DIORITE -> "Diorite";
            case DIORITE_SMOOTH -> "Smooth Diorite";
            case ANDESITE -> "Andesite";
            case ANDESITE_SMOOTH -> "Andesite Smooth";
        };
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance() {
        return 6;
    }

    @Override
    public float getHardness() {
        return 1.5f;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ItemBlock toStack() {
        return new ItemBlock(this, 1);
    }

}
