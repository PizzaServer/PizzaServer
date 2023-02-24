package io.github.pizzaserver.api.recipe.type;

import io.github.pizzaserver.api.recipe.data.RecipeBlockType;
import io.github.pizzaserver.api.recipe.data.RecipeType;
import io.github.pizzaserver.api.recipe.data.ShapedRecipeGrid;

public class ShapedRecipe extends Recipe {

    private final ShapedRecipeGrid grid;

    public ShapedRecipe(RecipeBlockType blockType, ShapedRecipeGrid shapedRecipeGrid) {
        super(blockType);

        this.grid = shapedRecipeGrid;
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
