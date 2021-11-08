package io.github.willqi.pizzaserver.api.level.world.blocks.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.api.item.ToolTypeRegistry;
import io.github.willqi.pizzaserver.api.item.data.ToolType;
import io.github.willqi.pizzaserver.api.item.data.ToolTypeID;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;

import java.util.*;

public class BlockTypeDirt extends BaseBlockType {

    private static final HashBiMap<NBTCompound, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NBTCompound, Integer>() {
        {
            List<String> dirtTypes = Arrays.asList("normal", "coarse");
            int stateIndex = 0;
            for (String dirtType : dirtTypes) {
                NBTCompound state = new NBTCompound("states")
                        .putString("dirt_type", dirtType);
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
    public BiMap<NBTCompound, Integer> getBlockStates() {
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
