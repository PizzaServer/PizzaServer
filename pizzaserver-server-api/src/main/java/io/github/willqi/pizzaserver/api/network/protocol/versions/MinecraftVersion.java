package io.github.willqi.pizzaserver.api.network.protocol.versions;

import io.github.willqi.pizzaserver.format.BlockRuntimeMapper;

/**
 * Represents a specific Minecraft version
 */
public interface MinecraftVersion extends BlockRuntimeMapper {

    int getProtocol();

    /**
     * The game version
     * @return the game version
     */
    String getVersion();

    /**
     * Resolves the runtime id of an item given its item id
     * @param itemName item id
     * @return item runtime id
     */
    int getItemRuntimeId(String itemName);

    /**
     * Resolve a item name by its runtime id
     * @param runtimeId runtime id
     * @return the item name or null if no name could be found
     */
    String getItemName(int runtimeId);

}
