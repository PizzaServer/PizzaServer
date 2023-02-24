package io.github.pizzaserver.api.network.protocol.version;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import io.github.pizzaserver.api.block.Block;

/**
 * Represents a specific Minecraft version.
 */
public interface MinecraftVersion {

    int getProtocol();

    /**
     * The game version.
     * @return the game version
     */
    String getVersion();

    BedrockPacketCodec getPacketCodec();

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

    int getBlockRuntimeId(String blockId, NbtMap blockState);
}
