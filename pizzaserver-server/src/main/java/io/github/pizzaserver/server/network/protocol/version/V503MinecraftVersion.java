package io.github.pizzaserver.server.network.protocol.version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nukkitx.blockstateupdater.BlockStateUpdaters;
import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v503.Bedrock_v503;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.server.network.utils.MinecraftNamespaceComparator;

import java.io.*;
import java.util.*;

public class V503MinecraftVersion extends V486MinecraftVersion {

    public static final int PROTOCOL = 503;
    public static final String VERSION = "1.18.30";

    public V503MinecraftVersion() throws IOException {}

    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public BedrockPacketCodec getPacketCodec() {
        return Bedrock_v503.V503_CODEC;
    }

    @Override
    protected void loadBlockStates() throws IOException {
        try (InputStream blockStatesFileStream = this.getProtocolResourceStream("block_states.nbt");
             NBTInputStream blockStatesNBTStream = NbtUtils.createNetworkReader(blockStatesFileStream)) {
            // keySet returns in ascending rather than descending so we have to reverse it
            SortedMap<String, List<NbtMap>> sortedBlockRuntimeStates =
                    new TreeMap<>(MinecraftNamespaceComparator::compareFNV);

            // Parse block states
            while (blockStatesFileStream.available() > 0) {
                NbtMap blockState = BlockStateUpdaters.updateBlockState((NbtMap) blockStatesNBTStream.readTag(), 0);
                String name = blockState.getString("name");
                if (!sortedBlockRuntimeStates.containsKey(name)) {
                    sortedBlockRuntimeStates.put(name, new ArrayList<>());
                }

                NbtMap states = blockState.getCompound("states");
                sortedBlockRuntimeStates.get(name).add(states);
            }

            // Add custom block states
            for (Block block : BlockRegistry.getInstance().getCustomBlocks()) {
                sortedBlockRuntimeStates.put(block.getBlockId(), block.getNBTStates());
                this.customBlockProperties.add(this.getBlockPropertyData(block));
            }

            // Block runtime ids are determined by the order of the sorted block runtime states.
            int runtimeId = 0;
            for (String blockId : sortedBlockRuntimeStates.keySet()) {
                for (NbtMap states : sortedBlockRuntimeStates.get(blockId)) {
                    BlockStateData blockStateLookupKey = new BlockStateData(blockId, states);
                    this.blockStates.put(blockStateLookupKey, runtimeId++);
                }
            }
        }
    }

    @Override
    protected void loadDefaultCreativeItems() throws IOException {
        try (Reader creativeItemsReader = new InputStreamReader(this.getProtocolResourceStream("creative_items.json"))) {
            JsonArray jsonCreativeItems = GSON.fromJson(creativeItemsReader, JsonObject.class).getAsJsonArray("items");

            for (JsonElement jsonCreativeItem : jsonCreativeItems) {
                JsonObject creativeJSONObj = jsonCreativeItem.getAsJsonObject();

                String id = creativeJSONObj.get("id").getAsString();
                int meta = creativeJSONObj.has("damage") ? creativeJSONObj.get("damage").getAsInt() : 0;

                if (!ItemRegistry.getInstance().hasItem(id)) {
                    // TODO: debug log this as this is a missing item!
                    continue;
                }

                Item item;
                if (creativeJSONObj.has("block_state_b64")) {
                    byte[] nbtData = Base64.getDecoder().decode(creativeJSONObj.get("block_state_b64").getAsString());

                    NbtMap blockNBT;
                    try (NBTInputStream nbtInputStream = NbtUtils.createReaderLE(new ByteArrayInputStream(nbtData))) {
                        blockNBT = ((NbtMap) nbtInputStream.readTag());
                    }
                    String blockId = blockNBT.getString("name");
                    NbtMap blockState = blockNBT.getCompound("states");

                    Block block = this.getBlockFromRuntimeId(this.getBlockRuntimeId(blockId, blockState));
                    if (block == null) {
                        // TODO: debug log this to as this is a missing block!
                        continue;
                    }

                    item = new ItemBlock(block, 1);
                } else {
                    if (creativeJSONObj.has("damage")) {
                        item = ItemRegistry.getInstance().getItem(id, 1, meta);
                    } else {
                        item = ItemRegistry.getInstance().getItem(id, 1, meta);
                    }
                }

                this.creativeItems.add(item);
            }
        }
    }

}
