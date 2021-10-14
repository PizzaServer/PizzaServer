package io.github.willqi.pizzaserver.api.item;

import io.github.willqi.pizzaserver.api.item.types.ItemType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemRegistry {

    private static final Map<String, ItemType> types = new HashMap<>();

    private static final Set<ItemType> customItemTypes = new HashSet<>();


    public static void register(ItemType itemType) {
        if (!itemType.getItemId().startsWith("minecraft:")) {
            customItemTypes.add(itemType);
        }

        types.put(itemType.getItemId(), itemType);
    }

    public static ItemType getItemType(String itemId) {
        if (!types.containsKey(itemId)) {
            throw new NullPointerException("Could not find a item type by the id of " + itemId);
        }
        return types.get(itemId);
    }

    public static boolean hasItemType(String itemId) {
        return types.containsKey(itemId);
    }

    public static Set<ItemType> getCustomTypes() {
        return customItemTypes;
    }

    public static ItemStack getItem(String itemId) {
        ItemType itemType = getItemType(itemId);
        return itemType.create();
    }

    public static ItemStack getItem(String itemId, int amount) {
        ItemType itemType = getItemType(itemId);
        return itemType.create(amount);
    }

    public static ItemStack getItem(String itemId, int amount, int damage) {
        ItemType itemType = getItemType(itemId);
        return itemType.create(amount, damage);
    }

}
