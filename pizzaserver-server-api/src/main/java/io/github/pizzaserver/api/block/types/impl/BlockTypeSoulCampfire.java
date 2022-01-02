package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.HashMap;

public class BlockTypeSoulCampfire extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
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
        return BlockTypeID.SOUL_CAMPFIRE;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Soul Campfire";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStateNBTs() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance(int blockStateIndex) {
        return 2f;
    }

    @Override
    public float getHardness(int blockStateIndex) {
        return 2;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.AXE;
    }

}
