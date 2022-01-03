package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;

import java.util.HashMap;

public class BlockTypeBedrock extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
        {
            this.put(NbtMap.builder()
                    .putByte("infiniburn_bit", (byte) 0)
                    .build(), 0);
            this.put(NbtMap.builder()
                    .putByte("infiniburn_bit", (byte) 1)
                    .build(), 1);
        }
    });

    @Override
    public String getBlockId() {
        return BlockTypeID.BEDROCK;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Bedrock";
    }

    @Override
    public float getHardness() {
        return -1;
    }

    @Override
    public float getBlastResistance() {
        return -1;
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStateNBTs() {
        return BLOCK_STATES;
    }

}
