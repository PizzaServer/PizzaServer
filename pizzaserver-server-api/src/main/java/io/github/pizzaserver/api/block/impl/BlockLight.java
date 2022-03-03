package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;

import java.util.ArrayList;
import java.util.List;

public class BlockLight extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int i = 0; i < 16; i++) {
                this.add(NbtMap.builder()
                        .putInt("block_light_level", i)
                        .build());
            }
        }
    };


    public int getLightLevel() {
        return this.getBlockState();
    }

    public void setLightLevel(int level) {
        this.setBlockState(Math.max(0, Math.min(level, 15)));
    }

    @Override
    public String getBlockId() {
        return BlockID.LIGHT_BLOCK;
    }

    @Override
    public String getName() {
        return "Light Block";
    }

    @Override
    public int getLightEmission() {
        return this.getLightLevel();
    }

    @Override
    public float getHardness() {
        return -1;
    }

    @Override
    public float getBlastResistance() {
        return -1;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public int getStackMeta() {
        return this.getBlockState();
    }

    @Override
    public void updateFromStackMeta(int meta) {
        if (meta >= 0 && meta <= 15) {
            this.setLightLevel(meta);
        }
    }

    @Override
    public boolean canBeIgnited() {
        return false;
    }

}
