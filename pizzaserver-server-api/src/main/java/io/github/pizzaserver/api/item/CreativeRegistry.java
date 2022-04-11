package io.github.pizzaserver.api.item;

import io.github.pizzaserver.api.Server;

import java.util.List;
import java.util.Optional;

public interface CreativeRegistry {

    /**
     * Retrieve all items that should show up in the creative mode inventory.
     * @return all items that will show up in the creative mode inventory.
     */
    List<Item> getItems();

    /**
     * Register an item to the creative mode inventory.
     * @param item the item to register to the creative mode inventory
     * @return creative mode network id assigned to item
     */
    int register(Item item);

    /**
     * Unregister a creative mode item given the item.
     * @param item the item
     */
    default void unregister(Item item) {
        int networkId = this.getNetworkIdByItem(item);
        if (networkId != -1) {
            this.unregister(networkId);
        }
    }

    /**
     * Unregister a creative item from the inventory given its network id.
     * @param networkId creative item network id
     */
    void unregister(int networkId);

    /**
     * Remove all creative items from the registry.
     */
    void clear();

    /**
     * Check if an item is registered in the creative inventory.
     * @param item the creative item
     * @return if the creative inventory has the item
     */
    default boolean hasItem(Item item) {
        return this.getNetworkIdByItem(item) != -1;
    }

    /**
     * Retrieve the creative network id of an item.
     * @param item the item
     * @return the network id or -1 if none are found.
     */
    int getNetworkIdByItem(Item item);

    /**
     * Retrieve a creative mode item by its network id assigned.
     * @param networkId the network id
     * @return the item if any exists
     */
    Optional<Item> getItemByNetworkId(int networkId);

    static CreativeRegistry getInstance() {
        return Server.getInstance().getCreativeRegistry();
    }

}
