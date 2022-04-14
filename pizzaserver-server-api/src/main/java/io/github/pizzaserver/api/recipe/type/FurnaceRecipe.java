package io.github.pizzaserver.api.recipe.type;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.recipe.data.FurnaceRecipeBlockType;
import io.github.pizzaserver.commons.utils.Check;

public class FurnaceRecipe extends Recipe {

    private final FurnaceRecipeBlockType blockType;
    private final Item input;
    private final Item output;

    public FurnaceRecipe(FurnaceRecipeBlockType blockType, Item input, Item output) {
        Check.nullParam(blockType, "blockType");
        Check.nullParam(input, "input");

        this.blockType = blockType;
        this.input = input;
        this.output = output;
    }

    public FurnaceRecipeBlockType getBlockType() {
        return this.blockType;
    }

    public Item getInput() {
        return this.input.clone();
    }

    public Item getOutput() {
        return this.output.clone();
    }

    @Override
    public RecipeType getType() {
        return RecipeType.FURNACE;
    }

}
