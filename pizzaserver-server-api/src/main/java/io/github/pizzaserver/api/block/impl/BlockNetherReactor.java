package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemDiamond;
import io.github.pizzaserver.api.item.impl.ItemGoldIngot;
import io.github.pizzaserver.api.item.impl.ItemIronIngot;

import java.util.HashSet;
import java.util.Set;

public class BlockNetherReactor extends Block {

    private static final Set<Item> DROPS = new HashSet<>() {
        {
            this.add(new ItemIronIngot(6));
            this.add(new ItemDiamond(3));
        }
    };

    @Override
    public String getBlockId() {
        return BlockID.NETHER_REACTOR;
    }

    @Override
    public String getName() {
        return "Nether Reactor Core";
    }

    // TODO: find right hardness
    @Override
    public float getHardness() {
        return 0f;
    }

    @Override
    public float getBlastResistance() {
        return 6f;
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
        return DROPS;
    }

}
