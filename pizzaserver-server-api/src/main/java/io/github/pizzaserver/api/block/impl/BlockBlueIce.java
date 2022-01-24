package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockBlueIce extends BlockIce {

    @Override
    public String getBlockId() {
        return BlockID.BLUE_ICE;
    }

    @Override
    public String getName() {
        return "Blue Ice";
    }

    @Override
    public float getHardness() {
        return 2.8f;
    }

    @Override
    public float getBlastResistance() {
        return 2.8f;
    }

    @Override
    public float getFriction() {
        return 0.99f;
    }

    @Override
    public int getLightEmission() {
        return 4;
    }

}
