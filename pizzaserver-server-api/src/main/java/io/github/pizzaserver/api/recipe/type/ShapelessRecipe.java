package io.github.pizzaserver.api.recipe.type;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.recipe.data.ShapelessRecipeBlockType;
import io.github.pizzaserver.commons.utils.Check;

public class ShapelessRecipe extends Recipe {

    private final ShapelessRecipeBlockType blockType;
    private final Item[] input;
    private final Item[] output;

    public ShapelessRecipe(ShapelessRecipeBlockType blockType, Item[] input, Item[] output) {
        Check.nullParam(blockType, "blockType");

        if (input.length > 9 || input.length == 0) {
            throw new IllegalArgumentException("The amount of items for a shapeless recipe must be within 1-9.");
        }

        this.blockType = blockType;
        this.input = input;
        this.output = output;
    }

    public Item[] getInput() {
        return this.input;
    }

    public Item[] getOutput() {
        return this.output;
    }

    /**
     * Returns the block that should be used to create this recipe.
     * @return block required for this recipe
     */
    public ShapelessRecipeBlockType getBlockType() {
        return this.blockType;
    }

    @Override
    public RecipeType getType() {
        return RecipeType.SHAPELESS;
    }

}
