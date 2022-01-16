package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.Collections;
import java.util.Set;

public class BlockBlueIce extends Block {

    @Override
    public String getBlockId() {
        return BlockID.BLUE_ICE;
    }

    @Override
    public String getName() {
        return "Blue Ice";
    }

    @Override
    public float getHardness() {
        return 2.8f;
    }

    @Override
    public float getBlastResistance() {
        return 2.8f;
    }

    @Override
    public float getFriction() {
        return 0.99f;
    }

    @Override
    public int getLightEmission() {
        return 4;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
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
        // TODO: return blue ice block if entity is has silk touch
        return Collections.emptySet();
    }

}
