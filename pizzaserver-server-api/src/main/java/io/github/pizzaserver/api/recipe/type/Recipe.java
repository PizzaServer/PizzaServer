package io.github.pizzaserver.api.recipe.type;

import java.util.UUID;

public abstract class Recipe {

    private static int NETWORK_ID = 1;

    private final UUID id;
    private final int networkId;


    public Recipe() {
        this.id = UUID.randomUUID();
        this.networkId = NETWORK_ID++;
    }

    public UUID getUUID() {
        return this.id;
    }

    public abstract RecipeType getType();

    public int getNetworkId() {
        return this.networkId;
    }

}
