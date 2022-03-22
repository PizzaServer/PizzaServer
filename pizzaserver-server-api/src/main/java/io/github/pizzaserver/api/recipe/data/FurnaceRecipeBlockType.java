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

    public static FurnaceRecipeBlockType fromRecipeBlock(String recipeBlockId) {
        return switch (recipeBlockId) {
            case "blast_furnace" -> BLAST_FURNACE;
            case "campfire" -> CAMPFIRE;
            case "furnace" -> FURNACE;
            case "smoker" -> SMOKER;
            case "soul_campfire" -> SOUL_CAMPFIRE;
            default -> null;
        };
    }
}
