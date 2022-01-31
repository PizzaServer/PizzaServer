package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockCraftingTable extends Block {

    @Override
    public String getBlockId() {
        return BlockID.CRAFTING_TABLE;
    }

    @Override
    public String getName() {
        return "Crafting Table";
    }

    @Override
    public float getHardness() {
        return 2.5f;
    }

    @Override
    public float getBlastResistance() {
        return 2.5f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.AXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

}
