package io.github.pizzaserver.api.recipe.type;

import io.github.pizzaserver.api.recipe.data.ShapedRecipeBlockType;
import io.github.pizzaserver.api.recipe.data.ShapedRecipeGrid;
import io.github.pizzaserver.commons.utils.Check;

public class ShapedRecipe extends Recipe {

    private final ShapedRecipeBlockType blockType;
    private final ShapedRecipeGrid grid;

    public ShapedRecipe(ShapedRecipeBlockType blockType, ShapedRecipeGrid shapedRecipeGrid) {
        Check.nullParam(blockType, "blockType");

        this.grid = shapedRecipeGrid;
        this.blockType = blockType;
    }

    /**
     * Returns the block that should be used to create this recipe.
     * If the recipe is within 2x2, it can be crafted in your inventory without the block.
     * @return block required for this recipe
     */
    public ShapedRecipeBlockType getBlockType() {
        return this.blockType;
    }

    public ShapedRecipeGrid getGrid() {
        return this.grid;
    }

    /**
     * If the item can be crafted in your inventory without the block.
     * @return if the item can be crafted in your inventory without the block.
     */
    public boolean canBeCraftedInInventory() {
        return this.getGrid().getWidth() <= 2 && this.getGrid().getHeight() <= 2;
    }

    @Override
    public RecipeType getType() {
        return RecipeType.SHAPED;
    }

}
