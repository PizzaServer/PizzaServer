package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockFace;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.HashMap;

public class BlockTypeChest extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
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
    public float getHardness() {
        return 2.5f;
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStateNBTs() {
        return BLOCK_STATES;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.AXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face) {
        block.setBlockStateIndex(entity.getHorizontalDirection().opposite().getBlockStateIndex());
        return true;
    }

}
