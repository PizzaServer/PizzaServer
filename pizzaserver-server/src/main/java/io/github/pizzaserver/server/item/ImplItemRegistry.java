package io.github.pizzaserver.server.item;

import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.types.CustomItemType;
import io.github.pizzaserver.api.item.types.ItemType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImplItemRegistry implements ItemRegistry {

    private final Map<String, ItemType> types = new HashMap<>();

    private final Set<CustomItemType> customItemTypes = new HashSet<>();


    @Override
    public void register(ItemType itemType) {
        if (!itemType.getItemId().startsWith("minecraft:")) {
            if (!(itemType instanceof CustomItemType)) {
                throw new IllegalArgumentException("The provided item type does not extend CustomItemType");
            }
            this.customItemTypes.add((CustomItemType) itemType);
        }

        this.types.put(itemType.getItemId(), itemType);
    }

    @Override
    public ItemType getItemType(String itemId) {
        if (!this.types.containsKey(itemId)) {
            throw new NullPointerException("Could not find a item type by the id of " + itemId);
        }
        return this.types.get(itemId);
    }

    @Override
    public boolean hasItemType(String itemId) {
        return this.types.containsKey(itemId);
    }

    @Override
    public Set<CustomItemType> getCustomTypes() {
        return this.customItemTypes;
    }

    @Override
    public ItemStack getItem(String itemId) {
        ItemType itemType = this.getItemType(itemId);
        return itemType.create();
    }

    @Override
    public ItemStack getItem(String itemId, int amount) {
        ItemType itemType = this.getItemType(itemId);
        return itemType.create(amount);
    }

    @Override
    public ItemStack getItem(String itemId, int amount, int damage) {
        ItemType itemType = this.getItemType(itemId);
        return itemType.create(amount, damage);
    }

}
