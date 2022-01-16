package io.github.pizzaserver.api.item;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.item.behavior.ItemBehavior;

import java.util.Set;

public interface ItemRegistry {

    /**
     * Register an item.
     * @param item if implementing a custom item, you must implement CustomItem instead of Item
     */
    void register(Item item);

    /**
     * Register an item and tie it to a behavior.
     * @param item if implementing a custom item, you must implement CustomItem instead of Item
     * @param behavior behavior of item
     */
    <T extends Item> void register(T item, ItemBehavior<T> behavior);

    boolean hasItemType(String itemId);

    Set<CustomItem> getCustomItems();

    default Item getItem(String itemId) {
        return this.getItem(itemId, 1);
    }

    default Item getItem(String itemId, int amount) {
        return this.getItem(itemId, amount, 0);
    }

    Item getItem(String itemId, int amount, int meta);

    default ItemBehavior<? extends Item> getItemBehavior(String itemId) {
        return this.getItemBehavior(this.getItem(itemId));
    }

    ItemBehavior<? extends Item> getItemBehavior(Item item);

    static ItemRegistry getInstance() {
        return Server.getInstance().getItemRegistry();
    }
}
