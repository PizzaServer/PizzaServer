package io.github.pizzaserver.api.recipe.type;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.recipe.data.RecipeBlockType;
import io.github.pizzaserver.api.recipe.data.RecipeType;
import io.github.pizzaserver.commons.utils.Check;

public class FurnaceRecipe extends Recipe {

    private final Item input;
    private final Item output;

    public FurnaceRecipe(RecipeBlockType blockType, Item input, Item output) {
        super(blockType);
        Check.notNull(input, "input");

        this.input = input;
        this.output = output;
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
