package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemClayBall;

import java.util.Collections;
import java.util.Set;

public class BlockClay extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.CLAY;
    }

    @Override
    public String getName() {
        return "Clay Block";
    }

    @Override
    public float getHardness() {
        return 0.6f;
    }

    @Override
    public float getBlastResistance() {
        return 0.6f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(new ItemClayBall(4));
    }

}
