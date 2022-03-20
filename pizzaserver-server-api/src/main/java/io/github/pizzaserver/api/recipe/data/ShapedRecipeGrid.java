package io.github.pizzaserver.api.recipe.data;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.commons.utils.Check;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the arrangement of items for a shaped recipe.
 */
public class ShapedRecipeGrid {

    private final int height;
    private final int width;

    private final Item[][] grid;
    private final Item[] output;


    protected ShapedRecipeGrid(Item[][] grid, Item[] output) {
        this.height = grid.length;
        this.width = grid[0].length;

        this.grid = grid;
        this.output = output;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public Item getItem(int x, int y) {
        Check.inclusiveBounds(x, 0, this.getWidth() - 1, "x");
        Check.inclusiveBounds(y, 0, this.getHeight() - 1, "y");

        return Item.getAirIfNull(this.grid[y][x]);
    }

    public Item[] getOutput() {
        return this.output;
    }


    public static class Builder {

        private final Item[][] grid;
        private final List<Item> output = new ArrayList<>();


        public Builder(int width, int height) {
            Check.inclusiveBounds(width, 1, 3, "width");
            Check.inclusiveBounds(height, 1, 3, "height");

            this.grid = new Item[height][width];
        }

        public Builder setSlot(int x, int y, Item item) {
            Check.inclusiveBounds(x, 0, this.grid[0].length - 1, "x");
            Check.inclusiveBounds(y, 0, this.grid.length - 1, "y");

            this.grid[y][x] = item;
            return this;
        }

        public Builder addOutput(Item item) {
            this.output.add(item);
            return this;
        }

        public ShapedRecipeGrid build() {
            return new ShapedRecipeGrid(this.grid, this.output.toArray(new Item[0]));
        }

    }

}
