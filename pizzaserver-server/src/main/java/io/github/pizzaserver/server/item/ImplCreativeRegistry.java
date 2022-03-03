package io.github.pizzaserver.server.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.item.CreativeRegistry;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;

import java.util.*;

public class ImplCreativeRegistry implements CreativeRegistry {

    private final BiMap<Integer, Item> items = HashBiMap.create();
    private int nextCreativeNetworkId = 1;

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(this.items.values());
    }

    @Override
    public int register(Item item) {
        if (!ItemRegistry.getInstance().hasItem(item.getItemId())) {
            throw new IllegalArgumentException("Cannot register creative item without registering it to the ItemRegistry first");
        }
        if (this.items.inverse().containsKey(item)) {
            Server.getInstance().getLogger().debug("Attempted to register creative item " + item.getItemId() + " " + item.getMeta() + " twice.");
            return this.getNetworkIdByItem(item);
        }

        int networkId = this.nextCreativeNetworkId++;

        Item creativeItem = item.newNetworkCopy(networkId);
        this.items.put(networkId, creativeItem);

        return networkId;
    }

    @Override
    public void unregister(int networkId) {
        this.items.remove(networkId);
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public int getNetworkIdByItem(Item item) {
        return this.items.inverse().getOrDefault(item, -1);
    }

    @Override
    public Optional<Item> getItemByNetworkId(int networkId) {
        if (this.items.containsKey(networkId)) {
            Item creativeItem = this.items.get(networkId).newNetworkCopy();
            return Optional.of(creativeItem);
        } else {
            return Optional.empty();
        }
    }

}
