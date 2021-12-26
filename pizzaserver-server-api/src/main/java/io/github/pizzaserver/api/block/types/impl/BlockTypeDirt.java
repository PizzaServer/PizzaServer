package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;

import java.util.*;

public class BlockTypeDirt extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
        {
            List<String> dirtTypes = Arrays.asList("normal", "coarse");
            int stateIndex = 0;
            for (String dirtType : dirtTypes) {
                NbtMap state = NbtMap.builder()
                        .putString("dirt_type", dirtType)
                        .build();
                this.put(state, stateIndex++);
            }
        }
    });


    @Override
    public String getBlockId() {
        return BlockTypeID.DIRT;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Dirt";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStateNBTs() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness(int blockStateIndex) {
        return 0.5f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

}
