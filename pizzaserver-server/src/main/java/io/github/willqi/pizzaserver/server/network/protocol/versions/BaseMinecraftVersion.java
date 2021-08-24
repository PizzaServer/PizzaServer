package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.network.protocol.data.ItemState;
import io.github.willqi.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.ImplServer;
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
    protected static final Map<Tuple<String, NBTCompound>, Integer> GLOBAL_BLOCK_STATES = new HashMap<>();

    private final ImplServer server;

    private NBTCompound biomesDefinitions;
    private final Map<Integer, Integer> blockStates = new HashMap<>();
    private Set<io.github.willqi.pizzaserver.server.network.protocol.data.ItemState> itemStates;


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
        this.blockStates.clear();
        try (NBTInputStream blockStatesNBTStream = new NBTInputStream(
                new VarIntDataInputStream(this.getProtocolResourceStream("block_states.nbt"))
        )) {
            // keySet returns in ascending rather than descending so we have to reverse it
            SortedMap<String, List<NBTCompound>> sortedBlockRuntimeStates = new TreeMap<>(Collections.reverseOrder((fullBlockIdA, fullBlockIdB) -> {
                // Runtime ids are mapped by their part b first before part a (e.g. b:b goes before b:c and a:d)
                String blockIdA = fullBlockIdA.substring(fullBlockIdA.indexOf(":") + 1);
                String blockIdB = fullBlockIdB.substring(fullBlockIdB.indexOf(":") + 1);
                int blockIdComparison = blockIdB.compareTo(blockIdA);
                if (blockIdComparison != 0) {
                    return blockIdComparison;
                }
                // Compare by namespace
                String namespaceA = fullBlockIdA.substring(0, fullBlockIdA.indexOf(":"));
                String namespaceB = fullBlockIdB.substring(0, fullBlockIdB.indexOf(":"));
                return namespaceB.compareTo(namespaceA);
            }));

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

            Set<io.github.willqi.pizzaserver.server.network.protocol.data.ItemState> itemStates = new HashSet<>(jsonItemStates.size());
            for (int i = 0; i < jsonItemStates.size(); i++) {
                JsonObject jsonItemState = jsonItemStates.get(i).getAsJsonObject();

                itemStates.add(new io.github.willqi.pizzaserver.server.network.protocol.data.ItemState(
                        jsonItemState.get("name").getAsString(),
                        jsonItemState.get("id").getAsInt(),
                        false));
            }
            this.itemStates = itemStates;
        }
    }

    protected InputStream getProtocolResourceStream(String fileName) {
        return ImplServer.getInstance().getClass().getResourceAsStream("/protocol/v" + this.getProtocol() + "/" + fileName);
    }

    @Override
    public int getBlockRuntimeId(String name, NBTCompound state) {
        try {
            int blockStateLookupId = GLOBAL_BLOCK_STATES.get(new Tuple<>(name, state));
            return this.blockStates.get(blockStateLookupId);
        } catch (NullPointerException exception) {
            throw new NullPointerException("Failed to find block runtime id for a state of " + name);
        }
    }

    @Override
    public Set<ItemState> getItemStates() {
        return Collections.unmodifiableSet(this.itemStates);
    }

    @Override
    public NBTCompound getBiomeDefinitions() {
        return this.biomesDefinitions;
    }

}
