package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.SlabType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockDeepslateBrickSlab extends BlockSlab {

    public BlockDeepslateBrickSlab() {
        this(SlabType.SINGLE);
    }

    public BlockDeepslateBrickSlab(SlabType slabType) {
        super(slabType);
    }

    @Override
    public String getBlockId() {
        return this.isDouble() ? BlockID.DEEPSLATE_BRICK_DOUBLE_SLAB : BlockID.DEEPSLATE_BRICK_SLAB;
    }

    @Override
    public String getName() {
        return "Deepslate Brick Slab";
    }

    @Override
    public float getHardness() {
        return 3.5f;
    }

    @Override
    public float getBlastResistance() {
        return 6;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

}
