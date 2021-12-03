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

public class BlockTypeFurnace extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<NbtMap, Integer>() {
        {
            for (int direction = 0; direction < 6; direction++) {
                this.put(NbtMap.builder()
                        .putInt("facing_direction", direction)
                        .build(), direction);
            }
        }
    });

    @Override
    public String getBlockId() {
        return BlockTypeID.FURNACE;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Furnace";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance(int blockStateIndex) {
        return 3.5f;
    }

    @Override
    public float getToughness(int blockStateIndex) {
        return 3.5f;
    }

    @Override
    public Set<ToolType> getCorrectTools(int blockStateIndex) {
        return Collections.singleton(ToolTypes.getToolType(ToolTypeID.WOOD_PICKAXE));
    }

    @Override
    public Set<ToolType> getBestTools(int blockStateIndex) {
        return Collections.singleton(ToolTypes.getToolType(ToolTypeID.WOOD_PICKAXE));
    }

}
