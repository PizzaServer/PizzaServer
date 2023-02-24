package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.recipe.data.RecipeBlockType;

public interface CraftingInventory extends Inventory {

    int getGridHeight();

    int getGridWidth();

    RecipeBlockType getRecipeBlockType();

}
