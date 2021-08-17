package io.github.willqi.pizzaserver.api.item;

import io.github.willqi.pizzaserver.api.item.types.BaseItemType;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemRegistry {

    private static final Map<String, BaseItemType> types = new HashMap<>();

    private static final Set<BaseItemType> customItemTypes = new HashSet<>();


    public static void register(BaseItemType itemType) {
        if (types.containsKey(itemType.getItemId())) {
            throw new IllegalArgumentException("Item id " + itemType.getItemId() + " was already registered.");
        }
        if (!itemType.getItemId().startsWith("minecraft:")) {
            customItemTypes.add(itemType);
        }

        types.put(itemType.getItemId(), itemType);
    }

    public static BaseItemType getItemType(String itemId) {
        if (!types.containsKey(itemId)) {
            throw new NullPointerException("Could not find a item type by the id of " + itemId);
        }
        return types.get(itemId);
    }

    public static boolean hasItemType(String itemId) {
        return types.containsKey(itemId);
    }

    public static Set<BaseItemType> getCustomTypes() {
        return customItemTypes;
    }

    public static ItemStack getItem(String itemId) {
        BaseItemType itemType = getItemType(itemId);
        return itemType.create();
    }

    public static ItemStack getItem(String itemId, int amount) {
        BaseItemType itemType = getItemType(itemId);
        return itemType.create(amount);
    }

    public static ItemStack getItem(String itemId, int amount, int damage) {
        BaseItemType itemType = getItemType(itemId);
        return itemType.create(amount, damage);
    }

}
