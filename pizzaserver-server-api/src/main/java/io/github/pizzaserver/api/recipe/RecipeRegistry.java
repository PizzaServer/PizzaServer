package io.github.pizzaserver.api.recipe;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.recipe.type.Recipe;

public interface RecipeRegistry {

    void register(Recipe recipe);

    default boolean unregister(Recipe recipe) {
        return this.unregister(recipe.getNetworkId());
    }

    boolean unregister(int recipeId);

    void clear();

    static RecipeRegistry getInstance() {
        return Server.getInstance().getRecipeRegistry();
    }

}
