package io.github.pizzaserver.api.item;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.item.types.CustomItemType;
import io.github.pizzaserver.api.item.types.ItemType;

import java.util.Set;

public interface ItemRegistry {

    /**
     * Register the behaviour/existence of an item type.
     *
     * @param itemType item type - if implementing a custom item, you must implement CustomItemType in addition to ItemType
     *                      otherwise an exception will occur
     */
    void register(ItemType itemType);

    ItemType getItemType(String itemId);

    boolean hasItemType(String itemId);

    Set<CustomItemType> getCustomTypes();

    ItemStack getItem(String itemId);

    ItemStack getItem(String itemId, int amount);

    ItemStack getItem(String itemId, int amount, int damage);

    static ItemRegistry getInstance() {
        return Server.getInstance().getItemRegistry();
    }

}
