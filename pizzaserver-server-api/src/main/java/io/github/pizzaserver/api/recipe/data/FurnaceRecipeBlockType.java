package io.github.pizzaserver.api.recipe.data;

public enum FurnaceRecipeBlockType {
    BLAST_FURNACE("blast_furnace"),
    CAMPFIRE("campfire"),
    FURNACE("furnace"),
    SMOKER("smoker"),
    SOUL_CAMPFIRE("soul_campfire");


    private final String recipeBlockId;

    FurnaceRecipeBlockType(String recipeBlockId) {
        this.recipeBlockId = recipeBlockId;
    }

    public String getRecipeBlockId() {
        return this.recipeBlockId;
    }
}
