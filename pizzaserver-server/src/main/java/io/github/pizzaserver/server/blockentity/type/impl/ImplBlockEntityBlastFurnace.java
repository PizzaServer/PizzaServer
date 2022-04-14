package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.blockentity.type.BlockEntityBlastFurnace;
import io.github.pizzaserver.api.recipe.data.FurnaceRecipeBlockType;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Set;

public class ImplBlockEntityBlastFurnace extends ImplBlockEntityFurnace implements BlockEntityBlastFurnace {

    public static final Set<String> BLOCK_IDS = Set.of(BlockID.BLAST_FURNACE, BlockID.LIT_BLAST_FURNACE);

    public ImplBlockEntityBlastFurnace(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

    @Override
    protected float getBurnRate() {
        return 2;
    }

    @Override
    protected FurnaceRecipeBlockType getBlockRecipeType() {
        return FurnaceRecipeBlockType.BLAST_FURNACE;
    }

}
