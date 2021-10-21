package io.github.willqi.pizzaserver.api.network.protocol.versions;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.format.BlockRuntimeMapper;

/**
 * Represents a specific Minecraft version.
 */
public interface MinecraftVersion extends BlockRuntimeMapper {

    int getProtocol();

    /**
     * The game version.
     * @return the game version
     */
    String getVersion();

    /**
     * Resolves the runtime id of an item given its item id.
     * @param itemName item id
     * @return item runtime id
     */
    int getItemRuntimeId(String itemName);

    /**
     * Resolve an item name by its runtime id.
     * @param runtimeId runtime id
     * @return the item name
     */
    String getItemName(int runtimeId);

    /**
     * Resolve a block by its runtime id.
     * @param blockRuntimeId runtime id
     * @return the block
     */
    Block getBlockFromRuntimeId(int blockRuntimeId);

}
