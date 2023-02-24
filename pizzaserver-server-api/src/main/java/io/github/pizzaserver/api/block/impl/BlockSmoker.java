package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.LitType;

public class BlockSmoker extends BlockFurnace {

    public BlockSmoker() {
        this(LitType.UNLIT);
    }

    public BlockSmoker(LitType type) {
        this.setLit(type == LitType.LIT);
    }

    @Override
    public String getBlockId() {
        if (this.isLit()) {
            return BlockID.LIT_SMOKER;
        } else {
            return BlockID.SMOKER;
        }
    }

    @Override
    public String getName() {
        if (this.isLit()) {
            return "Lit Smoker";
        } else {
            return "Smoker";
        }
    }

}
