package io.github.pizzaserver.api.block.types.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.HashMap;

public class BlockTypeBell extends BaseBlockType {

    private static final BiMap<NbtMap, Integer> BLOCK_STATES = HashBiMap.create(new HashMap<>() {
        {
            String[] attachmentTypes = new String[]{ "standing", "hanging", "side", "multiple" };

            int blockStateIndex = 0;
            for (String attachmentType : attachmentTypes) {
                for (int direction = 0; direction < 4; direction++) {
                    this.put(NbtMap.builder()
                            .putString("attachment", attachmentType)
                            .putInt("direction", direction)
                            .putByte("toggle_bit", (byte) 0)
                            .build(), blockStateIndex++);
                    this.put(NbtMap.builder()
                            .putString("attachment", attachmentType)
                            .putInt("direction", direction)
                            .putByte("toggle_bit", (byte) 1)
                            .build(), blockStateIndex++);
                }
            }
        }
    });


    @Override
    public String getBlockId() {
        return BlockTypeID.BELL;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Bell";
    }

    @Override
    public BiMap<NbtMap, Integer> getBlockStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance(int blockStateIndex) {
        return 5;
    }

    @Override
    public float getHardness(int blockStateIndex) {
        return 5;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

}
