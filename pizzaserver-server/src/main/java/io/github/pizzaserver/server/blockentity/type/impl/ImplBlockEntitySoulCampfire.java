package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockSoulCampfire;
import io.github.pizzaserver.api.blockentity.type.BlockEntitySoulCampfire;
import io.github.pizzaserver.api.recipe.data.RecipeBlockType;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntitySoulCampfire extends ImplBlockEntityCampfire implements BlockEntitySoulCampfire {

    public static final Set<String> BLOCK_IDS = Collections.singleton(BlockID.SOUL_CAMPFIRE);

    public ImplBlockEntitySoulCampfire(BlockLocation location) {
        super(location);
    }

    @Override
    public BlockSoulCampfire getBlock() {
        return (BlockSoulCampfire) super.getBlock();
    }

    @Override
    protected RecipeBlockType getRecipeBlockType() {
        return RecipeBlockType.SOUL_CAMPFIRE;
    }

}
