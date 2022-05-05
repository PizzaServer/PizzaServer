package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.blockentity.type.BlockEntitySmoker;
import io.github.pizzaserver.api.recipe.data.RecipeBlockType;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Set;

public class ImplBlockEntitySmoker extends ImplBlockEntityFurnace implements BlockEntitySmoker {

    public static final Set<String> BLOCK_IDS = Set.of(BlockID.SMOKER, BlockID.LIT_SMOKER);

    public ImplBlockEntitySmoker(BlockLocation location) {
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
    protected RecipeBlockType getBlockRecipeType() {
        return RecipeBlockType.SMOKER;
    }

}