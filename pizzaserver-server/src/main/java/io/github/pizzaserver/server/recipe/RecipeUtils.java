package io.github.pizzaserver.server.recipe;

import com.nukkitx.protocol.bedrock.data.inventory.CraftingData;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;
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

}
