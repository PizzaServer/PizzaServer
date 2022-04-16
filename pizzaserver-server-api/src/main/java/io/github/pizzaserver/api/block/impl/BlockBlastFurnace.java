package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.LitType;
import io.github.pizzaserver.api.blockentity.type.BlockEntityBlastFurnace;
import io.github.pizzaserver.api.blockentity.type.BlockEntityFurnace;

public class BlockBlastFurnace extends BlockFurnace {

    public BlockBlastFurnace() {
        this(LitType.UNLIT);
    }

    public BlockBlastFurnace(LitType type) {
        this.setLit(type == LitType.LIT);
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

    @Override
    public BlockEntityBlastFurnace getBlockEntity() {
        return (BlockEntityBlastFurnace) super.getBlockEntity();
    }

}
