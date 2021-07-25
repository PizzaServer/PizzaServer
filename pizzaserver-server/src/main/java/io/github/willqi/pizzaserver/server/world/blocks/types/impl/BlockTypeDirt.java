package io.github.willqi.pizzaserver.server.world.blocks.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.commons.data.storage.IdentityKey;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTString;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockTypeID;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlockTypeDirt extends BlockType {

    private static final HashBiMap<NBTCompound, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NBTCompound, Integer>(){
        {
            List<String> dirtTypes = Arrays.asList("normal", "coarse");
            int stateIndex = 0;
            for (String dirtType : dirtTypes) {
                NBTCompound state = new NBTCompound("states");
                state.put("dirt_type", new NBTString(dirtType));
                this.put(state, stateIndex++);
            }
        }
    });


    @Override
    public IdentityKey<BlockTypeDirt> getBlockId() {
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

}
