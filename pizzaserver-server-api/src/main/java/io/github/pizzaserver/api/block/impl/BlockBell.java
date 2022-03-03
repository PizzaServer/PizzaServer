package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.ArrayList;
import java.util.List;

public class BlockBell extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            String[] attachmentTypes = new String[]{ "standing", "hanging", "side", "multiple" };

            for (String attachmentType : attachmentTypes) {
                for (int direction = 0; direction < 4; direction++) {
                    this.add(NbtMap.builder()
                            .putString("attachment", attachmentType)
                            .putInt("direction", direction)
                            .putByte("toggle_bit", (byte) 0)
                            .build());
                    this.add(NbtMap.builder()
                            .putString("attachment", attachmentType)
                            .putInt("direction", direction)
                            .putByte("toggle_bit", (byte) 1)
                            .build());
                }
            }
        }
    };


    @Override
    public String getBlockId() {
        return BlockID.BELL;
    }

    @Override
    public String getName() {
        return "Bell";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance() {
        return 5;
    }

    @Override
    public float getHardness() {
        return 5;
    }

    @Override
    public boolean canBeIgnited() {
        return false;
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
