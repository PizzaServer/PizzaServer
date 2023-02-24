package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;

import java.util.Collections;
import java.util.Set;

public class BlockCrimsonNylium extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.CRIMSON_NYLIUM;
    }

    @Override
    public String getName() {
        return "Crimson Nylium";
    }

    @Override
    public float getHardness() {
        return 0.4f;
    }

    @Override
    public float getBlastResistance() {
        return 1;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(new ItemBlock(BlockID.NETHERRACK));
    }

}
