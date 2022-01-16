package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.Collections;
import java.util.Set;

public class BlockBuddingAmethyst extends Block {

    @Override
    public String getBlockId() {
        return BlockID.BUDDING_AMETHYST;
    }

    @Override
    public String getName() {
        return "Budding Amethyst";
    }

    @Override
    public float getHardness() {
        return 1.5f;
    }

    @Override
    public float getBlastResistance() {
        return 1.5f;
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
        return Collections.emptySet();
    }

}
