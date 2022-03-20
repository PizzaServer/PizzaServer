package io.github.pizzaserver.api.recipe;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.recipe.type.Recipe;

import java.util.UUID;

public interface RecipeRegistry {

    void register(Recipe recipe);

    default boolean unregister(Recipe recipe) {
        return this.unregister(recipe.getUUID());
    }

    boolean unregister(UUID recipeId);

    void clear();

    static RecipeRegistry getInstance() {
        return Server.getInstance().getRecipeRegistry();
    }

}
