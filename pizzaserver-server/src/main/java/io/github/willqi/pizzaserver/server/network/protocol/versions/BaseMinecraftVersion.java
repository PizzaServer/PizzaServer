package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.types.BlockItemType;
import io.github.willqi.pizzaserver.api.item.types.ItemType;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.api.network.protocol.packets.StartGamePacket;
import io.github.willqi.pizzaserver.api.network.protocol.utils.MinecraftNamespaceComparator;
import io.github.willqi.pizzaserver.server.network.protocol.exceptions.ProtocolException;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public abstract class BaseMinecraftVersion implements MinecraftVersion {

    private static final Gson GSON = new Gson();

    // GLOBAL_BLOCK_STATES stores all possible block states and their indexes
    // the purpose of this is to reduce memory usage
    // when loading the block states for each version
    // as many of them have duplicate id and NBT
    // by storing the integer rather than the tuple into
    // this.blockStates, we can reduce the amount of memory allocated
    // for the tuple and its properties since multiple versions
    // no longer have to recreate a tuple that would use more space
    // than an integer
    protected static final BiMap<Tuple<String, NBTCompound>, Integer> GLOBAL_BLOCK_STATES = HashBiMap.create();

    private final ImplServer server;

    protected NBTCompound biomesDefinitions;
    protected final BiMap<Integer, Integer> blockStates = HashBiMap.create();
    protected final BiMap<String, Integer> itemRuntimeIds = HashBiMap.create();
    protected final Set<StartGamePacket.ItemState> itemStates = new HashSet<>();


    public BaseMinecraftVersion(ImplServer server) throws IOException {
        this.server = server;
        this.loadBiomeDefinitions();
        this.loadBlockStates();
        this.loadRuntimeItems();
    }

    public abstract BasePacketRegistry getPacketRegistry();

    public BasePacketBuffer createPacketBuffer() {
        return this.createPacketBuffer(256);
    }

    public abstract BasePacketBuffer createPacketBuffer(ByteBuf buf);

    public abstract BasePacketBuffer createPacketBuffer(int initialCapacity);

    public ImplServer getServer() {
        return this.server;
    }

    public void loadBiomeDefinitions() throws IOException {
        try (NBTInputStream biomesNBTStream = new NBTInputStream(
                new VarIntDataInputStream(this.getProtocolResourceStream("biome_definitions.nbt"))
        )) {
            this.biomesDefinitions = biomesNBTStream.readCompound();
        }
    }

    public void loadBlockStates() throws IOException {
        try (NBTInputStream blockStatesNBTStream = new NBTInputStream(
                new VarIntDataInputStream(this.getProtocolResourceStream("block_states.nbt"))
        )) {
            // keySet returns in ascending rather than descending so we have to reverse it
            SortedMap<String, List<NBTCompound>> sortedBlockRuntimeStates =
                    new TreeMap<>(Collections.reverseOrder(MinecraftNamespaceComparator::compare));

            // Parse block states
            while (blockStatesNBTStream.available() > 0) {
                NBTCompound blockState = blockStatesNBTStream.readCompound();

                String name = blockState.getString("name");
                if (!sortedBlockRuntimeStates.containsKey(name)) {
                    sortedBlockRuntimeStates.put(name, new ArrayList<>());
                }

                NBTCompound states = blockState.getCompound("states");
                sortedBlockRuntimeStates.get(name).add(states);
            }

            // Add custom block states
            for (BaseBlockType blockType : BlockRegistry.getCustomTypes()) {
                sortedBlockRuntimeStates.put(blockType.getBlockId(), new ArrayList<>(blockType.getBlockStates().keySet()));
            }

            // Block runtime ids are determined by the order of the sorted block runtime states.
            int runtimeId = 0;
            for (String blockId : sortedBlockRuntimeStates.keySet()) {
                for (NBTCompound states : sortedBlockRuntimeStates.get(blockId)) {
                    Tuple<String, NBTCompound> blockStateLookupKey = new Tuple<>(blockId, states);
                    if (!GLOBAL_BLOCK_STATES.containsKey(blockStateLookupKey)) {
                        GLOBAL_BLOCK_STATES.put(blockStateLookupKey, GLOBAL_BLOCK_STATES.size());
                    }
                    int blockStateLookupId = GLOBAL_BLOCK_STATES.get(blockStateLookupKey);

                    this.blockStates.put(blockStateLookupId, runtimeId++);
                }
            }
        }
    }

    public void loadRuntimeItems() throws IOException {
        try (Reader itemStatesReader = new InputStreamReader(this.getProtocolResourceStream("runtime_item_states.json"))) {
            JsonArray jsonItemStates = GSON.fromJson(itemStatesReader, JsonArray.class);

            int customItemIdStart = 0;  // Custom items can be assigned any id as long as it does not conflict with an existing item

            // Register Vanilla items
            for (JsonElement element : jsonItemStates) {
                JsonObject itemState = element.getAsJsonObject();

                String itemId = itemState.get("name").getAsString();
                int runtimeId = itemState.get("id").getAsInt();
                customItemIdStart = Math.max(customItemIdStart, runtimeId + 1);

                this.itemRuntimeIds.put(itemId, runtimeId);
                this.itemStates.add(new StartGamePacket.ItemState(itemId, runtimeId, false));
            }
            this.itemRuntimeIds.put("minecraft:air", 0);    // A void item is equal to 0 and this reduces data sent over the network

            // Register custom items
            for (ItemType itemType : ItemRegistry.getCustomTypes()) {
                if (!(itemType instanceof BlockItemType)) { // We register item representations of custom blocks later
                    int runtimeId = customItemIdStart++;
                    this.itemRuntimeIds.put(itemType.getItemId(), runtimeId);
                    this.itemStates.add(new StartGamePacket.ItemState(itemType.getItemId(), runtimeId, true));
                }
            }

            //Register custom block items
            int customBlockIdStart = 1000;
            // Block item runtime ids are decided by the order they are sent via the StartGamePacket in the block properties
            // Block properties are sent sorted by their namespace according to Minecraft's namespace sorting.
            // So we will sort it the same way here
            SortedSet<BaseBlockType> sortedCustomBlockTypes =
                    new TreeSet<>((blockTypeA, blockTypeB) -> MinecraftNamespaceComparator.compare(blockTypeA.getBlockId(), blockTypeB.getBlockId()));
            sortedCustomBlockTypes.addAll(BlockRegistry.getCustomTypes());
            for (BaseBlockType customBlockType : sortedCustomBlockTypes) {
                this.itemRuntimeIds.put(customBlockType.getBlockId(), 255 - customBlockIdStart++);  // (255 - index) = item runtime id
            }
        }
    }

    protected InputStream getProtocolResourceStream(String fileName) {
        return ImplServer.getInstance().getClass().getResourceAsStream("/protocol/v" + this.getProtocol() + "/" + fileName);
    }

    @Override
    public int getBlockRuntimeId(String name, NBTCompound state) {
        Tuple<String, NBTCompound> key = new Tuple<>(name, state);

        if (!GLOBAL_BLOCK_STATES.containsKey(key)) {
            throw new ProtocolException(this, "No such block runtime id exists for: " + name);
        }

        int blockStateLookupId = GLOBAL_BLOCK_STATES.get(new Tuple<>(name, state));
        return this.blockStates.get(blockStateLookupId);
    }

    @Override
    public Block getBlockFromRuntimeId(int blockRuntimeId) {
        if (!this.blockStates.inverse().containsKey(blockRuntimeId)) {
            throw new ProtocolException(this, "No such block state exists for runtime id: " + blockRuntimeId);
        }

        int blockStateLookupId = this.blockStates.inverse().get(blockRuntimeId);
        Tuple<String, NBTCompound> blockData = GLOBAL_BLOCK_STATES.inverse().get(blockStateLookupId);

        if (BlockRegistry.hasBlockType(blockData.getObjectA())) {
            BlockType blockType = BlockRegistry.getBlockType(blockData.getObjectA());
            return blockType.create(blockType.getBlockStateIndex(blockData.getObjectB()));
        } else {
            return null;
        }
    }

    @Override
    public int getItemRuntimeId(String itemName) {
        if (this.itemRuntimeIds.containsKey(itemName)) {
            return this.itemRuntimeIds.get(itemName);
        } else {
            throw new ProtocolException(this, "Attempted to retrieve runtime id for non-existent item: " + itemName);
        }
    }

    @Override
    public String getItemName(int runtimeId) {
        if (this.itemRuntimeIds.inverse().containsKey(runtimeId)) {
            return this.itemRuntimeIds.inverse().get(runtimeId);
        } else {
            throw new ProtocolException(this, "Attempted to retrieve item name for non-existent runtime id: " + runtimeId);
        }
    }

    public NBTCompound getBiomeDefinitions() {
        return this.biomesDefinitions;
    }

    public Set<StartGamePacket.ItemState> getItemStates() {
        return Collections.unmodifiableSet(this.itemStates);
    }

}
