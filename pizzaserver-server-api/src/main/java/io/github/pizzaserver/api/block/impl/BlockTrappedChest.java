package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.utils.HorizontalDirection;

public class BlockTrappedChest extends BlockChest {

    public BlockTrappedChest() {
        this(HorizontalDirection.NORTH);
    }

    public BlockTrappedChest(HorizontalDirection direction) {
        this.setDirection(direction);
    }

    @Override
    public String getBlockId() {
        return BlockID.TRAPPED_CHEST;
    }

    @Override
    public String getName() {
        return "Trapped Chest";
    }

}
