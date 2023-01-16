package io.github.pizzaserver.api.recipe.type;

import io.github.pizzaserver.api.recipe.data.RecipeBlockType;
import io.github.pizzaserver.api.recipe.data.RecipeType;
import io.github.pizzaserver.commons.utils.Check;

import java.util.UUID;

public abstract class Recipe {

    private static int NETWORK_ID = 1;

    private final UUID id;
    private final int networkId;

    private final RecipeBlockType blockType;


    public Recipe(RecipeBlockType blockType) {
        Check.notNull(blockType, "blockType");

        this.blockType = blockType;
        this.id = UUID.randomUUID();
        this.networkId = NETWORK_ID++;
    }

    public abstract RecipeType getType();

    public UUID getUUID() {
        return this.id;
    }

    /**
     * Returns the block that should be used to create this recipe.
     * @return block required for this recipe
     */
    public RecipeBlockType getBlockType() {
        return this.blockType;
    }

    public int getNetworkId() {
        return this.networkId;
    }

}
