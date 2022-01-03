package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.HashMap;

public class BlockTypeSand extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
        {
            this.put(NbtMap.builder()
                    .putString("sand_type", "normal")
                    .build(), 0);
            this.put(NbtMap.builder()
                    .putString("sand_type", "red")
                    .build(), 1);
        }
    });

    @Override
    public String getBlockId() {
        return BlockTypeID.SAND;
    }

    @Override
    public String getName(int blockStateIndex) {
        if (blockStateIndex == 0) {
            return "Sand";
        } else {
            return "Red Sand";
        }
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStateNBTs() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public float getBlastResistance() {
        return 0.5f;
    }

    @Override
    public boolean hasGravity() {
        return true;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ItemStack toStack(int blockStateIndex) {
        return new ItemStack(this.getBlockId(), 1, blockStateIndex);
    }

}
