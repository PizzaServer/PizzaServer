package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.format.BlockRuntimeMapper;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.network.protocol.data.ItemState;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public abstract class MinecraftVersion implements BlockRuntimeMapper {

    private final static Gson GSON = new Gson();

    private final Server server;

    private NBTCompound biomesDefinitions;
    private final Map<Tuple<String, NBTCompound>, Integer> blockStates = new HashMap<>();
    private Set<ItemState> itemStates;


    public MinecraftVersion(Server server) throws IOException {
        this.server = server;
        this.loadBiomeDefinitions();
        this.loadBlockStates();
        this.loadRuntimeItems();
    }

    public abstract int getProtocol();

    public abstract String getVersionString();

    public abstract PacketRegistry getPacketRegistry();

    public Server getServer() {
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
            SortedMap<String, List<NBTCompound>> blockStates = new TreeMap<>(Collections.reverseOrder((fullBlockIdA, fullBlockIdB) -> {
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

                String name = blockState.getString("name").getValue();
                if (!blockStates.containsKey(name)) {
                    blockStates.put(name, new ArrayList<>());
                }

                NBTCompound states = blockState.getCompound("states");
                blockStates.get(name).add(states);
            }

            // Add custom block states
            for (BlockType blockType : this.getServer().getBlockRegistry().getCustomTypes()) {
                blockStates.put(blockType.getBlockId().getID(), new ArrayList<>(blockType.getBlockStates().keySet()));
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

            Set<ItemState> itemStates = new HashSet<>(jsonItemStates.size());
            for (int i = 0; i < jsonItemStates.size(); i++) {
                JsonObject jsonItemState = jsonItemStates.get(i).getAsJsonObject();

                itemStates.add(new ItemState(
                        jsonItemState.get("name").getAsString(),
                        jsonItemState.get("id").getAsInt(),
                        false));
            }
            this.itemStates = itemStates;
        }
    }

    protected InputStream getProtocolResourceStream(String fileName) {
        return Server.getInstance().getClass().getResourceAsStream("/protocol/v" + this.getProtocol() + "/" + fileName);
    }

    @Override
    public int getBlockRuntimeId(String name, NBTCompound state) {
        try {
            return this.blockStates.get(new Tuple<>(name, state));
        } catch (NullPointerException exception) {
            throw new NullPointerException("Failed to find block runtime id for a state of " + name);
        }
    }

    public Set<ItemState> getItemStates() {
        return Collections.unmodifiableSet(this.itemStates);
    }

    public NBTCompound getBiomeDefinitions() {
        return this.biomesDefinitions;
    }
}
