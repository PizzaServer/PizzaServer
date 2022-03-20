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

}
