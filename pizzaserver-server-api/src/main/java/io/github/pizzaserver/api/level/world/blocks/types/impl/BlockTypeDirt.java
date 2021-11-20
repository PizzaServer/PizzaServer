package io.github.pizzaserver.api.level.world.blocks.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.ToolTypeID;
import io.github.pizzaserver.api.item.ToolTypeRegistry;
import io.github.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.pizzaserver.api.level.world.blocks.types.BlockTypeID;

import java.util.*;

public class BlockTypeDirt extends BaseBlockType {

    private static final HashBiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NbtMap, Integer>() {
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
    public String getName() {
        return "Dirt";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getToughness() {
        return 0.5f;
    }

    @Override
    public Set<ToolType> getCorrectTools() {
        return new HashSet<>(Arrays.asList(ToolTypeRegistry.getToolType(ToolTypeID.NONE), ToolTypeRegistry.getToolType(ToolTypeID.WOOD_SHOVEL)));
    }

    @Override
    public Set<ToolType> getBestTools() {
        return new HashSet<>(Arrays.asList(ToolTypeRegistry.getToolType(ToolTypeID.NONE), ToolTypeRegistry.getToolType(ToolTypeID.WOOD_SHOVEL)));
    }

}
