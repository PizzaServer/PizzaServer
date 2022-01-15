package io.github.pizzaserver.server.item;

import io.github.pizzaserver.api.item.CustomItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.behavior.ItemBehavior;
import io.github.pizzaserver.api.item.behavior.impl.DefaultItemBehavior;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImplItemRegistry implements ItemRegistry {

    private final Map<String, Item> items = new HashMap<>();
    private final Map<Class<? extends Item>, ItemBehavior<? extends Item>> behaviors = new HashMap<>();

    private final Set<CustomItem> customItems = new HashSet<>();


    @Override
    public void register(Item item) {
        this.register(item, new DefaultItemBehavior<>());
    }

    @Override
    public void register(Item item, ItemBehavior<? extends Item> behavior) {
        if (!item.getItemId().startsWith("minecraft:")) {
            if (!(item instanceof CustomItem)) {
                throw new IllegalArgumentException("The provided item type does not extend CustomItemType");
            }
            this.customItems.add((CustomItem) item);
        }

        this.items.put(item.getItemId(), item);
        this.behaviors.put(item.getClass(), behavior);
    }

    @Override
    public boolean hasItemType(String itemId) {
        return this.items.containsKey(itemId);
    }

    @Override
    public Set<CustomItem> getCustomItems() {
        return this.customItems;
    }

    @Override
    public Item getItem(String itemId, int amount, int meta) {
        if (!this.items.containsKey(itemId)) {
            throw new NullPointerException("Could not find a item type by the id of " + itemId);
        }

        Item item = this.items.get(itemId).newNetworkCopy();
        item.setCount(amount);
        item.setMeta(meta);

        return item;
    }

    @Override
    public ItemBehavior<? extends Item> getItemBehavior(Item item) {
        if (!this.behaviors.containsKey(item.getClass())) {
            throw new NullPointerException("There is no item behavior class for the provided class. Was it registered?");
        }

        return this.behaviors.get(item.getClass());
    }

}
