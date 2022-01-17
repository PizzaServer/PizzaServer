package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.CopperType;
import io.github.pizzaserver.api.block.data.SlabType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockWaxedCutCopperSlab extends BlockSlab {

    private CopperType copperType;


    public BlockWaxedCutCopperSlab() {
        this(CopperType.NORMAL);
    }

    public BlockWaxedCutCopperSlab(SlabType slabType) {
        this(CopperType.NORMAL, slabType);
    }

    public BlockWaxedCutCopperSlab(CopperType copperType) {
        this(copperType, SlabType.SINGLE);
    }

    public BlockWaxedCutCopperSlab(CopperType copperType, SlabType slabType) {
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
            case NORMAL -> this.isDouble() ? BlockID.WAXED_DOUBLE_CUT_COPPER_SLAB : BlockID.WAXED_CUT_COPPER_SLAB;
            case EXPOSED -> this.isDouble() ? BlockID.WAXED_EXPOSED_DOUBLE_CUT_COPPER_SLAB : BlockID.WAXED_EXPOSED_CUT_COPPER_SLAB;
            case OXIDIZED -> this.isDouble() ? BlockID.WAXED_OXIDIZED_DOUBLE_CUT_COPPER_SLAB : BlockID.WAXED_OXIDIZED_CUT_COPPER_SLAB;
            case WEATHERED -> this.isDouble() ? BlockID.WAXED_WEATHERED_DOUBLE_CUT_COPPER_SLAB : BlockID.WAXED_WEATHERED_CUT_COPPER_SLAB;
        };
    }

    @Override
    public String getName() {
        if (this.getCopperType() == CopperType.NORMAL) {
            return "Waxed Cut Copper Slab";
        }
        return "Waxed " + this.getCopperType().getDisplayName() + " Cut Copper Slab";
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
