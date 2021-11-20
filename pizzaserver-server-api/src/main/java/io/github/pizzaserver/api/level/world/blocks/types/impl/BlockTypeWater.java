package io.github.pizzaserver.api.level.world.blocks.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.pizzaserver.api.level.world.blocks.types.BlockTypeID;

import java.util.HashMap;

public class BlockTypeWater extends BaseBlockType {

    private static final HashBiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NbtMap, Integer>() {
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
    public String getName() {
        return "Water";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStates() {
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

    @Override
    public boolean isLiquid() {
        return true;
    }

}
