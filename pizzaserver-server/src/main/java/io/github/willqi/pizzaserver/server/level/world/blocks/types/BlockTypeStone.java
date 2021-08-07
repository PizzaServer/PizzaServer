package io.github.willqi.pizzaserver.server.level.world.blocks.types;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTString;
import io.github.willqi.pizzaserver.api.item.ItemToolType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;

import java.util.*;

public class BlockTypeStone extends BaseBlockType {

    private static final HashBiMap<NBTCompound, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NBTCompound, Integer>(){
        {
            List<String> stoneTypes = Arrays.asList("stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth");
            int stateIndex = 0;
            for (String stoneType : stoneTypes) {
                NBTCompound state = new NBTCompound("states");
                state.put("stone_type", new NBTString(stoneType));
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
    public BiMap<NBTCompound, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public Set<ItemToolType> getCorrectTools() {
        return Collections.singleton(ItemToolType.WOOD_PICKAXE);
    }

}
