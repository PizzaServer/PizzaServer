package io.github.pizzaserver.api.recipe;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.recipe.type.Recipe;

import java.util.Set;

public interface RecipeRegistry {

    void register(Recipe recipe);

    default boolean unregister(Recipe recipe) {
        return this.unregister(recipe.getNetworkId());
    }

    boolean unregister(int recipeId);

    Set<Recipe> getRecipes();

    static RecipeRegistry getInstance() {
        return Server.getInstance().getRecipeRegistry();
    }

}
