package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BlockTypeID;

public class BlockTypeBlastFurnace extends BlockTypeFurnace {

    @Override
    public String getBlockId() {
        return BlockTypeID.BLAST_FURNACE;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Blast Furnace";
    }

}
