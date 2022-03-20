package io.github.pizzaserver.server.recipe;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.api.recipe.RecipeRegistry;
import io.github.pizzaserver.api.utils.ServerState;

import java.util.Set;
import java.util.UUID;

public class ImplRecipeRegistry implements RecipeRegistry {

    private final BiMap<UUID, Recipe> recipes = HashBiMap.create();

    @Override
    public void register(Recipe recipe) {
        this.recipes.put(recipe.getUUID(), recipe);
    }

    @Override
    public boolean unregister(UUID recipeId) {
        return this.recipes.remove(recipeId) != null;
    }

    @Override
    public void clear() {
        this.recipes.clear();
    }

    public Set<Recipe> getRecipes() {
        return this.recipes.values();
    }

}
