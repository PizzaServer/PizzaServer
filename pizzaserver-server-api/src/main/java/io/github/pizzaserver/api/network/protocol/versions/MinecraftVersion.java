package io.github.pizzaserver.api.network.protocol.versions;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.data.BlockPropertyData;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.format.BlockRuntimeMapper;

import java.util.List;

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

    List<BlockPropertyData> getCustomBlockProperties();

    List<ComponentItemData> getItemComponents();

}
