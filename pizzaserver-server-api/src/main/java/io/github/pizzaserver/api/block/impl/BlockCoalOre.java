package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemCoal;

import java.util.Collections;
import java.util.Set;

public class BlockCoalOre extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.COAL_ORE;
    }

    @Override
    public String getName() {
        return "Coal Ore";
    }

    @Override
    public float getHardness() {
        return 3f;
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
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(new ItemCoal());
    }

}
