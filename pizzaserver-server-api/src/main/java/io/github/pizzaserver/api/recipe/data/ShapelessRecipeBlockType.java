package io.github.pizzaserver.api.recipe.data;

public enum ShapelessRecipeBlockType {
    CRAFTING_TABLE("crafting_table"),
    CARTOGRAPHY_TABLE("cartography_table"),
    SMITHING_TABLE("smithing_table"),
    STONECUTTER("stonecutter");

    private final String recipeBlockId;

    ShapelessRecipeBlockType(String recipeBlockId) {
        this.recipeBlockId = recipeBlockId;
    }

    public String getRecipeBlockId() {
        return this.recipeBlockId;
    }


    public static ShapelessRecipeBlockType fromRecipeBlock(String recipeBlockId) {
        return switch (recipeBlockId) {
            case "crafting_table" -> CRAFTING_TABLE;
            case "cartography_table" -> CARTOGRAPHY_TABLE;
            case "smithing_table" -> SMITHING_TABLE;
            case "stonecutter" -> STONECUTTER;
            default -> null;
        };
    }
}
