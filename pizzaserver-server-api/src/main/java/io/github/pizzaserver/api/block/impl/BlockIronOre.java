package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemRawIron;

import java.util.Collections;
import java.util.Set;

public class BlockIronOre extends Block {

    @Override
    public String getBlockId() {
        return BlockID.IRON_ORE;
    }

    @Override
    public String getName() {
        return "Iron Ore";
    }

    @Override
    public float getHardness() {
        return 3f;
    }

    @Override
    public float getBlastResistance() {
        return 3;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.STONE;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(new ItemRawIron());
    }

}
