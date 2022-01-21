package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.FurnaceType;

public class BlockBlastFurnace extends BlockFurnace {

    public BlockBlastFurnace() {
        this(FurnaceType.UNLIT);
    }

    public BlockBlastFurnace(FurnaceType type) {
        this.setLit(type == FurnaceType.LIT);
    }

    @Override
    public String getBlockId() {
        if (this.isLit()) {
            return BlockID.LIT_BLAST_FURNACE;
        } else {
            return BlockID.BLAST_FURNACE;
        }
    }

    @Override
    public String getName() {
        if (this.isLit()) {
            return "Lit Blast Furnace";
        } else {
            return "Blast Furnace";
        }
    }

}
