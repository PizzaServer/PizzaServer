package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.descriptors.Liquid;
import io.github.pizzaserver.api.item.impl.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockWater extends BaseBlock implements Liquid {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int depth = 0; depth < 16; depth++) {
                this.add(NbtMap.builder()
                        .putInt("liquid_depth", depth)
                        .build());
            }
        }
    };


    public BlockWater() {}

    public BlockWater(int depth) {
        this.setLiquidDepth(depth);
    }

    public void setLiquidDepth(int depth) {
        this.setBlockState(Math.max(0, Math.min(15, depth)));
    }

    public int getLiquidDepth() {
        return this.getBlockState();
    }

    @Override
    public String getBlockId() {
        return BlockID.WATER;
    }

    @Override
    public String getName() {
        return "Water";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
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
    public boolean hasOxygen() {
        return false;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

    @Override
    public boolean canBeIgnited() {
        return false;
    }

}
