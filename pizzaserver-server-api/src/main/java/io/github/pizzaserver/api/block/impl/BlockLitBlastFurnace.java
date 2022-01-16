package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockLitBlastFurnace extends BlockBlastFurnace {

    @Override
    public String getBlockId() {
        return BlockID.LIT_BLAST_FURNACE;
    }

    @Override
    public String getName() {
        return "Lit Blast Furnace";
    }
}
