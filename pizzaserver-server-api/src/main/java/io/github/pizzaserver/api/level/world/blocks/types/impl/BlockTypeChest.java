package io.github.pizzaserver.api.level.world.blocks.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.pizzaserver.api.level.world.blocks.types.BlockTypeID;

import java.util.HashMap;

public class BlockTypeChest extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NbtMap, Integer>() {
        {
            for (int i = 0; i < 6; i++) {
                this.put(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .build(), i);
            }
        }
    });


    @Override
    public String getBlockId() {
        return BlockTypeID.CHEST;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Chest";
    }

    @Override
    public float getToughness(int blockStateIndex) {
        return 2.5f;
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

}
