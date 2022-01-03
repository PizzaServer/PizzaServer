package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockFace;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;

public class BlockTypeAllow extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.ALLOW;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Allow";
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
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face) {
        return (entity instanceof Player) && ((Player) entity).getGamemode() == Gamemode.CREATIVE;
    }

}
