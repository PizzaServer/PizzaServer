package io.github.pizzaserver.api.recipe.data;

public enum ShapedRecipeBlockType {
    CRAFTING_TABLE("crafting_table");


    private final String recipeBlockId;

    ShapedRecipeBlockType(String recipeBlockId) {
        this.recipeBlockId = recipeBlockId;
    }

    public String getRecipeBlockId() {
        return this.recipeBlockId;
    }

    public static ShapedRecipeBlockType fromRecipeBlock(String recipeBlockId) {
        return switch (recipeBlockId) {
            case "crafting_table" -> CRAFTING_TABLE;
            default -> null;
        };
    }

}
