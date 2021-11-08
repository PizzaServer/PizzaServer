package io.github.willqi.pizzaserver.api.level.world.blocks.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.HashMap;

public class BlockTypeWater extends BaseBlockType {

    private static final HashBiMap<NBTCompound, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NBTCompound, Integer>() {
        {
            for (int depth = 0; depth < 16; depth++) {
                NBTCompound state = new NBTCompound("states")
                        .putInteger("liquid_depth", depth);
                this.put(state, depth);
            }
        }
    });


    @Override
    public String getBlockId() {
        return BlockTypeID.WATER;
    }

    @Override
    public String getName() {
        return "Water";
    }

    @Override
    public BiMap<NBTCompound, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public boolean hasOxygen() {
        return false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
