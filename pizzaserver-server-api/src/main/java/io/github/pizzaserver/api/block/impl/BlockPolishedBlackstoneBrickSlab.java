package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.SlabType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockPolishedBlackstoneBrickSlab extends BlockSlab {

    public BlockPolishedBlackstoneBrickSlab() {
        this(SlabType.SINGLE);
    }

    public BlockPolishedBlackstoneBrickSlab(SlabType slabType) {
        super(slabType);
    }

    @Override
    public String getBlockId() {
        return this.isDouble() ? BlockID.POLISHED_BLACKSTONE_DOUBLE_BRICK_SLAB : BlockID.POLISHED_BLACKSTONE_BRICK_SLAB;
    }

    @Override
    public String getName() {
        return "Polished Blackstone Brick Slab";
    }

    @Override
    public float getHardness() {
        return 2;
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
