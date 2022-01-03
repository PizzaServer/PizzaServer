package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;

import java.util.HashMap;

public class BlockTypeWater extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
        {
            for (int depth = 0; depth < 16; depth++) {
                NbtMap state = NbtMap.builder()
                        .putInt("liquid_depth", depth)
                        .build();
                this.put(state, depth);
            }
        }
    });


    @Override
    public String getBlockId() {
        return BlockTypeID.WATER;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Water";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStateNBTs() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 0;
    }

    @Override
    public boolean hasOxygen() {
        return false;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean isLiquid() {
        return true;
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

}
