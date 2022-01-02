package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class BlockTypeCauldron extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
        {
            String[] contentTypes = new String[]{ "water", "lava", "snow_powder" };

            int blockStateIndex = 0;
            for (String contentType : contentTypes) {
                for (int fillLevel = 0; fillLevel < 7; fillLevel++) {
                    this.put(NbtMap.builder()
                            .putString("cauldron_liquid", contentType)
                            .putInt("fill_level", fillLevel)
                            .build(), blockStateIndex++);
                }
            }
        }
    });

    @Override
    public String getBlockId() {
        return BlockTypeID.CAULDRON;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Cauldron";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStateNBTs() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance(int blockStateIndex) {
        return 2;
    }

    @Override
    public float getHardness(int blockStateIndex) {
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
