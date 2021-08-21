package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.types.BaseItemType;
import io.github.willqi.pizzaserver.api.item.types.BlockItemType;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.packets.StartGamePacket;
import io.github.willqi.pizzaserver.server.network.utils.MinecraftNamespaceComparator;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public abstract class BaseMinecraftVersion implements MinecraftVersion {

    private final static Gson GSON = new Gson();

    private final ImplServer server;

    protected NBTCompound biomesDefinitions;
    protected final Map<Tuple<String, NBTCompound>, Integer> blockStates = new HashMap<>();

    protected final Map<String, Integer> itemRuntimeIds = new HashMap<>();
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
            SortedMap<String, List<NBTCompound>> blockStates = new TreeMap<>(Collections.reverseOrder(MinecraftNamespaceComparator::compare));

            // Parse block states
            while (blockStatesNBTStream.available() > 0) {
                NBTCompound blockState = blockStatesNBTStream.readCompound();

                String name = blockState.getString("name");
                if (!blockStates.containsKey(name)) {
                    blockStates.put(name, new ArrayList<>());
                }

                NBTCompound states = blockState.getCompound("states");
                blockStates.get(name).add(states);
            }

            // Add custom block states
            for (BaseBlockType blockType : BlockRegistry.getCustomTypes()) {
                blockStates.put(blockType.getBlockId(), new ArrayList<>(blockType.getBlockStates().keySet()));
            }

            // Construct runtime ids
            int runtimeId = 0;
            for (String blockId : blockStates.keySet()) {
                for (NBTCompound states : blockStates.get(blockId)) {
                    this.blockStates.put(new Tuple<>(blockId, states), runtimeId++);
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
            for (BaseItemType itemType : ItemRegistry.getCustomTypes()) {
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
            SortedSet<BaseBlockType> sortedCustomBlockTypes = new TreeSet<>((blockTypeA, blockTypeB) -> MinecraftNamespaceComparator.compare(blockTypeA.getBlockId(), blockTypeB.getBlockId()));
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
        try {
            return this.blockStates.get(new Tuple<>(name, state));
        } catch (NullPointerException exception) {
            throw new NullPointerException("Failed to find block runtime id for a state of " + name);
        }
    }

    @Override
    public int getItemRuntimeId(String itemName) {
        try {
            return this.itemRuntimeIds.get(itemName);
        } catch (NullPointerException exception) {
            throw new NullPointerException("Failed to find item runtime id for " + itemName);
        }
    }

    public NBTCompound getBiomeDefinitions() {
        return this.biomesDefinitions;
    }

    public Set<StartGamePacket.ItemState> getItemStates() {
        return Collections.unmodifiableSet(this.itemStates);
    }

}
