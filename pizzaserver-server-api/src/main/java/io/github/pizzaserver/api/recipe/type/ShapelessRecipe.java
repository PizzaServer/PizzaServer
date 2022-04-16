package io.github.pizzaserver.api.recipe.type;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.recipe.data.RecipeBlockType;
import io.github.pizzaserver.api.recipe.data.RecipeType;

public class ShapelessRecipe extends Recipe {

    private final Item[] ingredients;
    private final Item[] output;

    public ShapelessRecipe(RecipeBlockType blockType, Item[] ingredients, Item[] output) {
        super(blockType);

        if (ingredients.length > 9 || ingredients.length == 0) {
            throw new IllegalArgumentException("The amount of items for a shapeless recipe must be within 1-9.");
        }

        this.ingredients = ingredients;
        this.output = output;
    }

    public Item[] getIngredients() {
        Item[] ingredients = new Item[this.ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = Item.getAirIfNull(this.ingredients[i]).clone();
        }


        return ingredients;
    }

    public Item[] getOutput() {
        Item[] output = new Item[this.output.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = Item.getAirIfNull(this.output[i]).clone();
        }

        return output;
    }

    @Override
    public RecipeType getType() {
        return RecipeType.SHAPELESS;
    }

}
