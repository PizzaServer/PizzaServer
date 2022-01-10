package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.descriptors.Flammable;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockBookshelf extends BaseBlock implements Flammable {

    @Override
    public String getBlockId() {
        return BlockID.BOOKSHELF;
    }

    @Override
    public String getName() {
        return "Bookshelf";
    }

    @Override
    public float getHardness() {
        return 1.5f;
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

    @Override
    public int getBurnOdds() {
        return 30;
    }

    @Override
    public int getFlameOdds() {
        return 20;
    }

}
