package io.github.pizzaserver.server.recipe;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.api.recipe.RecipeRegistry;
import io.github.pizzaserver.api.utils.ServerState;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ImplRecipeRegistry implements RecipeRegistry {

    private final BiMap<Integer, Recipe> recipes = HashBiMap.create();

    @Override
    public void register(Recipe recipe) {
        if (Server.getInstance().getState() != ServerState.REGISTERING) {
            throw new IllegalStateException("The server is not in the REGISTERING state");
        }

        this.recipes.put(recipe.getNetworkId(), recipe);
    }

    @Override
    public boolean unregister(int recipeId) {
        if (Server.getInstance().getState() != ServerState.REGISTERING) {
            throw new IllegalStateException("The server is not in the REGISTERING state");
        }

        return this.recipes.remove(recipeId) != null;
    }

    public Optional<Recipe> getByNetworkId(int networkId) {
        return Optional.ofNullable(this.recipes.getOrDefault(networkId, null));
    }

    @Override
    public Set<Recipe> getRecipes() {
        return Collections.unmodifiableSet(this.recipes.values());
    }

}
