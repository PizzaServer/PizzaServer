package io.github.pizzaserver.api.item;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.item.types.ItemType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface ItemRegistry {

    void register(ItemType itemType);

    ItemType getItemType(String itemId);

    boolean hasItemType(String itemId);

    Set<ItemType> getCustomTypes();

    ItemStack getItem(String itemId);

    ItemStack getItem(String itemId, int amount);

    ItemStack getItem(String itemId, int amount, int damage);

    static ItemRegistry getInstance() {
        return Server.getInstance().getItemRegistry();
    }

}
