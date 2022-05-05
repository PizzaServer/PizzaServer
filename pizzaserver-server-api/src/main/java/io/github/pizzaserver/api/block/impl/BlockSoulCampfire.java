package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.blockentity.type.BlockEntitySoulCampfire;

public class BlockSoulCampfire extends BlockCampfire {

    @Override
    public String getBlockId() {
        return BlockID.SOUL_CAMPFIRE;
    }

    @Override
    public String getName() {
        return "Soul Campfire";
    }

    @Override
    public BlockEntitySoulCampfire getBlockEntity() {
        return (BlockEntitySoulCampfire) super.getBlockEntity();
    }

}
