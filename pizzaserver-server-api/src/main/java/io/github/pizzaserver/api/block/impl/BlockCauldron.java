package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.ArrayList;
import java.util.List;

public class BlockCauldron extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            String[] contentTypes = new String[]{ "water", "lava", "snow_powder" };

            for (String contentType : contentTypes) {
                for (int fillLevel = 0; fillLevel < 7; fillLevel++) {
                    this.add(NbtMap.builder()
                            .putString("cauldron_liquid", contentType)
                            .putInt("fill_level", fillLevel)
                            .build());
                }
            }
        }
    };

    @Override
    public String getBlockId() {
        return BlockID.CAULDRON;
    }

    @Override
    public String getName() {
        return "Cauldron";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance() {
        return 2;
    }

    @Override
    public float getHardness() {
        return 2;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

}
