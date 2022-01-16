package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockChiseledDeepslate extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.CHISELED_DEEPSLATE;
    }

    @Override
    public String getName() {
        return "Chiseled Deepslate";
    }

    @Override
    public float getHardness() {
        return 3;
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
