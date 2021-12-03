package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.ToolTypes;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.ToolTypeID;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class BlockTypeCampfire extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NbtMap, Integer>() {
        {
            int blockStateIndex = 0;
            for (int direction = 0; direction < 4; direction++) {
                this.put(NbtMap.builder()
                        .putInt("direction", direction)
                        .putByte("extinguished", (byte) 0)
                        .build(), blockStateIndex++);
                this.put(NbtMap.builder()
                        .putInt("direction", direction)
                        .putByte("extinguished", (byte) 1)
                        .build(), blockStateIndex++);
            }
        }
    });

    @Override
    public String getBlockId() {
        return BlockTypeID.CAMPFIRE;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Campfire";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance(int blockStateIndex) {
        return 2f;
    }

    @Override
    public float getToughness(int blockStateIndex) {
        return 2;
    }

    @Override
    public Set<ToolType> getBestTools(int blockStateIndex) {
        return Collections.singleton(ToolTypes.getToolType(ToolTypeID.WOOD_AXE));
    }

}
