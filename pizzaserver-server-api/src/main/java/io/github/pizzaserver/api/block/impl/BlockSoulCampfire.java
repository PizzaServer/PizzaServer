package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

import java.util.ArrayList;
import java.util.List;

public class BlockSoulCampfire extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int direction = 0; direction < 4; direction++) {
                this.add(NbtMap.builder()
                        .putInt("direction", direction)
                        .putByte("extinguished", (byte) 0)
                        .build());
                this.add(NbtMap.builder()
                        .putInt("direction", direction)
                        .putByte("extinguished", (byte) 1)
                        .build());
            }
        }
    };

    @Override
    public String getBlockId() {
        return BlockID.SOUL_CAMPFIRE;
    }

    @Override
    public String getName() {
        return "Soul Campfire";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getBlastResistance() {
        return 2f;
    }

    @Override
    public float getHardness() {
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
