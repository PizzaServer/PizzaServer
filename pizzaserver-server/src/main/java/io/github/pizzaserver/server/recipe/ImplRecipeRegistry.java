package io.github.pizzaserver.server.recipe;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.api.recipe.RecipeRegistry;
import io.github.pizzaserver.api.utils.ServerState;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ImplRecipeRegistry implements RecipeRegistry {

    private final BiMap<Integer, Recipe> recipes = HashBiMap.create();

    @Override
    public void register(Recipe recipe) {
        this.recipes.put(recipe.getNetworkId(), recipe);
    }

    @Override
    public boolean unregister(int recipeId) {
        return this.recipes.remove(recipeId) != null;
    }

    @Override
    public void clear() {
        this.recipes.clear();
    }

    public Optional<Recipe> getByNetworkId(int networkId) {
        return Optional.ofNullable(this.recipes.getOrDefault(networkId, null));
    }

    public Set<Recipe> getRecipes() {
        return this.recipes.values();
    }

}
