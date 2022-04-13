package io.github.pizzaserver.api.network.protocol.version;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.data.BlockPropertyData;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.item.Item;

import java.util.List;

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

    NbtMap getBiomeDefinitions();

    NbtMap getEntityIdentifiers();

    List<StartGamePacket.ItemEntry> getItemEntries();

    /**
     * Retrieves all BASE creative items that should be loaded.
     * Modifications to the creative inventory can still be made via the CreativeRegistry.
     * This method only returns the items that are by DEFAULT in the creative inventory.
     * @return default creative items
     */
    List<Item> getDefaultCreativeItems();

    List<BlockPropertyData> getCustomBlockProperties();

    List<ComponentItemData> getItemComponents();

}
