package io.github.pizzaserver.server.network.data.inventory.actions;

import io.github.pizzaserver.api.recipe.RecipeRegistry;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.server.player.ImplPlayer;
import io.github.pizzaserver.server.recipe.ImplRecipeRegistry;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftRecipeAction;

import java.util.Optional;

public class CraftRecipeRequestActionDataWrapper extends StackRequestActionWrapper<CraftRecipeAction> {

    private final int recipeId;

    public CraftRecipeRequestActionDataWrapper(ImplPlayer player, CraftRecipeAction action) {
        super(player);
        this.recipeId = action.getRecipeNetworkId();
    }

    public Optional<Recipe> getRecipe() {
        return ((ImplRecipeRegistry) RecipeRegistry.getInstance()).getByNetworkId(this.recipeId);
    }

}
