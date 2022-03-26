package io.github.pizzaserver.server.network.data.inventory.actions;

import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.CraftRecipeStackRequestActionData;
import io.github.pizzaserver.api.recipe.RecipeRegistry;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.server.player.ImplPlayer;
import io.github.pizzaserver.server.recipe.ImplRecipeRegistry;

import java.util.Optional;

public class CraftRecipeRequestActionDataWrapper extends StackRequestActionWrapper<CraftRecipeStackRequestActionData> {

    private final int recipeId;

    public CraftRecipeRequestActionDataWrapper(ImplPlayer player, CraftRecipeStackRequestActionData action) {
        super(player);
        this.recipeId = action.getRecipeNetworkId();
    }

    public Optional<Recipe> getRecipe() {
        return ((ImplRecipeRegistry) RecipeRegistry.getInstance()).getByNetworkId(this.recipeId);
    }

}
