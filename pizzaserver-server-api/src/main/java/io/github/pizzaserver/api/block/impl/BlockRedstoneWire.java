package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;

import java.util.ArrayList;
import java.util.List;

public class BlockRedstoneWire extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int i = 0; i < 16; i++) {
                this.add(NbtMap.builder()
                        .putInt("redstone_signal", i)
                        .build());
            }
        }
    };

    @Override
    public String getBlockId() {
        return BlockID.REDSTONE_WIRE;
    }

    @Override
    public String getName() {
        return "Redstone Dust";
    }

    @Override
    public float getHardness() {
        return 0;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

}
