package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.ToolTypeID;
import io.github.pizzaserver.api.item.ToolTypes;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;

import java.util.*;

public class BlockTypeDirt extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NbtMap, Integer>() {
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
    public BiMap<NbtMap, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getToughness(int blockStateIndex) {
        return 0.5f;
    }

    @Override
    public Set<ToolType> getCorrectTools(int blockStateIndex) {
        return new HashSet<>(Arrays.asList(ToolTypes.getToolType(ToolTypeID.NONE), ToolTypes.getToolType(ToolTypeID.WOOD_SHOVEL)));
    }

    @Override
    public Set<ToolType> getBestTools(int blockStateIndex) {
        return new HashSet<>(Arrays.asList(ToolTypes.getToolType(ToolTypeID.NONE), ToolTypes.getToolType(ToolTypeID.WOOD_SHOVEL)));
    }

}
