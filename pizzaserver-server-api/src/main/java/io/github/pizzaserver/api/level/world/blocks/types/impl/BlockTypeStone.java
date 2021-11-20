package io.github.pizzaserver.api.level.world.blocks.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.item.ToolTypeRegistry;
import io.github.pizzaserver.api.item.data.ToolTypeID;
import io.github.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.level.world.blocks.types.BlockTypeID;

import java.util.*;

public class BlockTypeStone extends BaseBlockType {

    private static final HashBiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NbtMap, Integer>() {
        {
            List<String> stoneTypes = Arrays.asList("stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth");
            int stateIndex = 0;
            for (String stoneType : stoneTypes) {
                NbtMap state = NbtMap.builder()
                        .putString("stone_type", stoneType)
                        .build();
                this.put(state, stateIndex++);
            }
        }
    });


    @Override
    public String getBlockId() {
        return BlockTypeID.STONE;
    }

    @Override
    public String getName() {
        return "Stone";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getToughness() {
        return 1.5f;
    }

    @Override
    public Set<ToolType> getCorrectTools() {
        return Collections.singleton(ToolTypeRegistry.getToolType(ToolTypeID.WOOD_PICKAXE));
    }

    @Override
    public Set<ToolType> getBestTools() {
        return Collections.singleton(ToolTypeRegistry.getToolType(ToolTypeID.WOOD_PICKAXE));
    }

}
