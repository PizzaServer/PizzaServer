package io.github.pizzaserver.server.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nukkitx.protocol.bedrock.data.inventory.CraftingData;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;
import io.github.pizzaserver.api.recipe.data.FurnaceRecipeBlockType;
import io.github.pizzaserver.api.recipe.data.ShapedRecipeBlockType;
import io.github.pizzaserver.api.recipe.data.ShapedRecipeGrid;
import io.github.pizzaserver.api.recipe.data.ShapelessRecipeBlockType;
import io.github.pizzaserver.api.recipe.type.FurnaceRecipe;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.api.recipe.type.ShapedRecipe;
import io.github.pizzaserver.api.recipe.type.ShapelessRecipe;
import io.github.pizzaserver.server.item.ItemUtils;

import java.util.*;

public class RecipeUtils {

    public static CraftingData serializeForNetwork(Recipe recipe, MinecraftVersion version) {
        switch (recipe.getType()) {
            case SHAPED -> {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;

                List<ItemData> output = new ArrayList<>();
                for (Item outputItem : shapedRecipe.getGrid().getOutput()) {
                    output.add(ItemUtils.serializeForNetwork(outputItem, version));
                }

                List<ItemData> input = new ArrayList<>();
                for (int y = 0; y < shapedRecipe.getGrid().getHeight(); y++) {
                    for (int x = 0; x < shapedRecipe.getGrid().getWidth(); x++) {
                        input.add(ItemUtils.serializeForNetwork(shapedRecipe.getGrid().getItem(x, y), version));
                    }
                }

                return CraftingData.fromShaped(shapedRecipe.getUUID().toString(),
                        shapedRecipe.getGrid().getWidth(),
                        shapedRecipe.getGrid().getHeight(),
                        input,
                        output,
                        shapedRecipe.getUUID(),
                        shapedRecipe.getBlockType().getRecipeBlockId(),
                        0,
                        shapedRecipe.getNetworkId());
            }
            case SHAPELESS -> {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;

                return CraftingData.fromShapeless(shapelessRecipe.getUUID().toString(),
                        Arrays.stream(shapelessRecipe.getInput()).map(item -> ItemUtils.serializeForNetwork(item, version)).toList(),
                        Arrays.stream(shapelessRecipe.getOutput()).map(item -> ItemUtils.serializeForNetwork(item, version)).toList(),
                        shapelessRecipe.getUUID(),
                        shapelessRecipe.getBlockType().getRecipeBlockId(),
                        0,
                        shapelessRecipe.getNetworkId());
            }
            case FURNACE -> {
                FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;

                return CraftingData.fromFurnaceData(version.getItemRuntimeId(furnaceRecipe.getInput().getItemId()),
                        furnaceRecipe.getInput().getMeta(),
                        ItemUtils.serializeForNetwork(furnaceRecipe.getOutput(), version),
                        furnaceRecipe.getBlockType().getRecipeBlockId());
            }
            default -> {
                throw new UnsupportedOperationException("Missing recipe handler for type: " + recipe.getType());
            }
        }
    }

    public static Recipe deserializeFromJSON(JsonObject recipeJSON, MinecraftVersion version) {
        int recipeType = recipeJSON.get("type").getAsInt();

        return switch (recipeType) {
            case 0, 5 -> handleShapelessRecipeJSON(recipeJSON, version);
            case 1 -> handleShapedRecipeJSON(recipeJSON, version);
            case 3 -> handleFurnaceRecipeJSON(recipeJSON, version);
            case 4 -> null;
            default -> throw new IllegalArgumentException("Unexpected recipe type: " + recipeType);
        };
    }

    private static ShapelessRecipe handleShapelessRecipeJSON(JsonObject recipeJSON, MinecraftVersion version) {
        ShapelessRecipeBlockType blockType = ShapelessRecipeBlockType.fromRecipeBlock(recipeJSON.get("block").getAsString());

        List<Item> input = new ArrayList<>();
        for (JsonElement itemElement : recipeJSON.get("input").getAsJsonArray()) {
            Item inputItem = ItemUtils.fromJSON(itemElement.getAsJsonObject(), version);
            if (inputItem == null) {
                continue;
            }

            input.add(inputItem);
        }

        List<Item> output = new ArrayList<>();
        for (JsonElement itemElement : recipeJSON.get("output").getAsJsonArray()) {
            Item outputItem = ItemUtils.fromJSON(itemElement.getAsJsonObject(), version);
            if (outputItem == null) {
                continue;
            }

            output.add(outputItem);
        }

        if (input.size() == 0 || output.size() == 0) {
            return null;   // TODO: throw exception after all blocks/items implemented.
        }

        return new ShapelessRecipe(blockType, input.toArray(new Item[0]), output.toArray(new Item[0]));
    }

    private static ShapedRecipe handleShapedRecipeJSON(JsonObject recipeJSON, MinecraftVersion version) {
        ShapedRecipeBlockType blockType = ShapedRecipeBlockType.fromRecipeBlock(recipeJSON.get("block").getAsString());

        Map<Character, Item> itemInputLookup = new HashMap<>();
        JsonObject inputJSONDictionary = recipeJSON.get("input").getAsJsonObject();
        for (String lookupSymbol : inputJSONDictionary.keySet()) {
            JsonObject itemJSON = inputJSONDictionary.get(lookupSymbol).getAsJsonObject();

            Item inputItem = ItemUtils.fromJSON(itemJSON, version);
            if (inputItem == null) {
                // TODO: throw exception after all blocks/items implemented.
                continue;
            }

            itemInputLookup.put(lookupSymbol.charAt(0), inputItem);
        }

        int gridHeight = recipeJSON.get("shape").getAsJsonArray().size();
        int gridWidth = recipeJSON.get("shape").getAsJsonArray().get(0).getAsString().length();

        ShapedRecipeGrid.Builder gridBuilder = new ShapedRecipeGrid.Builder(gridWidth, gridHeight);
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                char lookupSymbol = recipeJSON.get("shape").getAsJsonArray().get(y).getAsString().charAt(x);
                if (lookupSymbol != ' ') {
                    gridBuilder.setSlot(x, y, itemInputLookup.get(lookupSymbol));
                }
            }
        }

        List<Item> output = new ArrayList<>();
        for (JsonElement itemElement : recipeJSON.get("output").getAsJsonArray()) {
            Item outputItem = ItemUtils.fromJSON(itemElement.getAsJsonObject(), version);
            if (outputItem == null) {
                continue;
            }

            output.add(outputItem);
        }

        for (Item outputItem : output) {
            gridBuilder.addOutput(outputItem);
        }

        if (itemInputLookup.size() == 0 || output.size() == 0) {
            return null;   // TODO: throw exception after all blocks/items implemented.
        }

        return new ShapedRecipe(blockType, gridBuilder.build());
    }

    private static FurnaceRecipe handleFurnaceRecipeJSON(JsonObject recipeJSON, MinecraftVersion version) {
        FurnaceRecipeBlockType blockType = FurnaceRecipeBlockType.fromRecipeBlock(recipeJSON.get("block").getAsString());

        Item inputItem = ItemUtils.fromJSON(recipeJSON.get("input").getAsJsonObject(), version);
        if (inputItem == null) {
            // TODO: throw exception after all blocks/items implemented.
            return null;
        }

        Item outputItem = ItemUtils.fromJSON(recipeJSON.get("output").getAsJsonObject(), version);
        if (outputItem == null) {
            // TODO: throw exception after all blocks/items implemented.
            return null;
        }

        return new FurnaceRecipe(blockType, inputItem, outputItem);
    }

}
