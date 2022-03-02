package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemMelonSlice;

import java.util.Collections;
import java.util.Set;

public class BlockMelon extends Block {

    @Override
    public String getBlockId() {
        return BlockID.MELON_BLOCK;
    }

    @Override
    public String getName() {
        return "Melon";
    }

    @Override
    public float getHardness() {
        return 1;
    }

    @Override
    public float getBlastResistance() {
        return 1;
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
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(new ItemMelonSlice((int) Math.floor(Math.random() * 5) + 3));
    }
}
