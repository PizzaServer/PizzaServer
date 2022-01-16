package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.Collections;
import java.util.Set;

public class BlockSnow extends Block {

    @Override
    public String getBlockId() {
        return BlockID.SNOW;
    }

    @Override
    public String getName() {
        return "Snow Block";
    }

    @Override
    public float getHardness() {
        return 0.2f;
    }

    @Override
    public float getBlastResistance() {
        return 0.2f;
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
        // TODO: return snowballs/snow block depending on tool used
        return Collections.emptySet();
    }

}
