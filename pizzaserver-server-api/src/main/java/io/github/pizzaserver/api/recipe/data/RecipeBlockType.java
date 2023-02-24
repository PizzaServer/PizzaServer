package io.github.pizzaserver.api.recipe.data;

public enum RecipeBlockType {
    BLAST_FURNACE("blast_furnace"),
    CAMPFIRE("campfire"),
    CARTOGRAPHY_TABLE("cartography_table"),
    CRAFTING_TABLE("crafting_table"),
    FURNACE("furnace"),
    SMITHING_TABLE("smithing_table"),
    SMOKER("smoker"),
    STONECUTTER("stonecutter"),
    SOUL_CAMPFIRE("soul_campfire"),;

    private final String recipeBlockTypeId;

    RecipeBlockType(String recipeBlockTypeId) {
        this.recipeBlockTypeId = recipeBlockTypeId;
    }

    public String getRecipeBlockTypeId() {
        return this.recipeBlockTypeId;
    }

    public static RecipeBlockType fromRecipeBlock(String recipeBlockTypeId) {
        return switch (recipeBlockTypeId) {
            case "blast_furnace" -> BLAST_FURNACE;
            case "campfire" -> CAMPFIRE;
            case "cartography_table" -> CARTOGRAPHY_TABLE;
            case "crafting_table" -> CRAFTING_TABLE;
            case "furnace" -> FURNACE;
            case "smithing_table" -> SMITHING_TABLE;
            case "smoker" -> SMOKER;
            case "stonecutter" -> STONECUTTER;
            case "soul_campfire" -> SOUL_CAMPFIRE;
            default -> null;
        };
    }
}
