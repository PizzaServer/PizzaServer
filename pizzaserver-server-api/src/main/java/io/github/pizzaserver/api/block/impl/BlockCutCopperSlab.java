package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.CopperType;
import io.github.pizzaserver.api.block.data.SlabType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockCutCopperSlab extends BlockSlab {

    private CopperType copperType;


    public BlockCutCopperSlab() {
        this(CopperType.NORMAL);
    }

    public BlockCutCopperSlab(SlabType slabType) {
        this(CopperType.NORMAL, slabType);
    }

    public BlockCutCopperSlab(CopperType copperType) {
        this(copperType, SlabType.SINGLE);
    }

    public BlockCutCopperSlab(CopperType copperType, SlabType slabType) {
        super(slabType);
        this.setCopperType(copperType);
    }

    public CopperType getCopperType() {
        return this.copperType;
    }

    public void setCopperType(CopperType copperType) {
        this.copperType = copperType;
    }

    @Override
    public String getBlockId() {
        return switch (this.getCopperType()) {
            case NORMAL -> this.isDouble() ? BlockID.DOUBLE_CUT_COPPER_SLAB : BlockID.CUT_COPPER_SLAB;
            case EXPOSED -> this.isDouble() ? BlockID.EXPOSED_DOUBLE_CUT_COPPER_SLAB : BlockID.EXPOSED_CUT_COPPER_SLAB;
            case OXIDIZED -> this.isDouble() ? BlockID.OXIDIZED_DOUBLE_CUT_COPPER_SLAB : BlockID.OXIDIZED_CUT_COPPER_SLAB;
            case WEATHERED -> this.isDouble() ? BlockID.WEATHERED_DOUBLE_CUT_COPPER_SLAB : BlockID.WEATHERED_CUT_COPPER_SLAB;
        };
    }

    @Override
    public String getName() {
        if (this.getCopperType() == CopperType.NORMAL) {
            return "Cut Copper Slab";
        }
        return this.getCopperType().getDisplayName() + " Cut Copper Slab";
    }

    @Override
    public float getHardness() {
        return 3;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.STONE;
    }

}
