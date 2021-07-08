package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.mcworld.BlockRuntimeMapper;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.network.protocol.data.ItemState;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public abstract class MinecraftVersion implements BlockRuntimeMapper {

    private final static Gson GSON = new Gson();

    private NBTCompound biomesDefinitions;
    private final Map<Tuple<String, NBTCompound>, Integer> blockStates = new HashMap<>();
    private ItemState[] itemStates;


    public MinecraftVersion() throws IOException {
        this.loadBiomeDefinitions();
        this.loadBlockStates();
        this.loadRuntimeItems();
    }

    public abstract int getProtocol();

    public abstract String getVersionString();

    public abstract PacketRegistry getPacketRegistry();

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
            int runtimeId = 0;
            while (blockStatesNBTStream.available() > 0) {
                NBTCompound blockState = blockStatesNBTStream.readCompound();

                String name = blockState.getString("name").getValue();
                NBTCompound states = blockState.getCompound("states");

                this.blockStates.put(new Tuple<>(name, states), runtimeId++);
            }
        }
    }

    public void loadRuntimeItems() throws IOException {
        try (Reader itemStatesReader = new InputStreamReader(this.getProtocolResourceStream("runtime_item_states.json"))) {
            JsonArray jsonItemStates = GSON.fromJson(itemStatesReader, JsonArray.class);

            ItemState[] itemStates = new ItemState[jsonItemStates.size()];
            for (int i = 0; i < itemStates.length; i++) {
                JsonObject jsonItemState = jsonItemStates.get(i).getAsJsonObject();

                itemStates[i] = new ItemState(
                        jsonItemState.get("name").getAsString(),
                        jsonItemState.get("id").getAsInt(),
                        false);
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

    public ItemState[] getItemStates() {
        return this.itemStates;
    }

    public NBTCompound getBiomeDefinitions() {
        return this.biomesDefinitions;
    }
}
