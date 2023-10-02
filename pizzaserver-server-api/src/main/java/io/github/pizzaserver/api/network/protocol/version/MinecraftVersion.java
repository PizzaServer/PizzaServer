package io.github.pizzaserver.api.network.protocol.version;

import io.github.pizzaserver.api.block.Block;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec;

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

    BedrockCodec getPacketCodec();

    /**
     * Resolves the runtime id of an item given its item id.
     * @param itemName item id
     * @return item runtime id
     */
    int getItemRuntimeId(String itemName);

    ItemDefinition getItemDefinition(String itemName);

    /**
     * Resolve an item name by its runtime id.
     * @param runtimeId runtime id
     * @return the item name
     */
    String getItemName(int runtimeId);

    BlockDefinition getBlockDefinition(String name, NbtMap state);

    /**
     * Resolve a block by its runtime id.
     * @param blockRuntimeId runtime id
     * @return the block
     */
    Block getBlockFromRuntimeId(int blockRuntimeId);

    int getBlockRuntimeId(String blockId, NbtMap blockState);
}
