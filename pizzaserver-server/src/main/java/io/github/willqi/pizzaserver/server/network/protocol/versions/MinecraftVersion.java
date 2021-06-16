package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.network.protocol.data.ItemState;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public abstract class MinecraftVersion {

    private final static Gson GSON = new Gson();


    private final NBTCompound biomesDefinitions;
    private final ItemState[] itemStates;

    public MinecraftVersion() {

        // Parse biome_definitions.nbt
        try (NBTInputStream biomesNBTStream = new NBTInputStream(
                new VarIntDataInputStream(
                        Server.getInstance().getClass().getResourceAsStream("/protocol/v" + this.getProtocol() + "/biome_definitions.nbt")
                )
        )) {
            this.biomesDefinitions = biomesNBTStream.readCompound();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read v" + this.getProtocol() + "'s biome_definitions.nbt file", exception);
        }

        // Parse runtime_item_states.json
        try (Reader itemStatesReader = new InputStreamReader(
                Server.getInstance().getClass().getResourceAsStream("/protocol/v" + this.getProtocol() + "/runtime_item_states.json")
        )) {
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
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read v" + this.getProtocol() + "'s runtime_item_states.json file", exception);
        }

    }


    public abstract int getProtocol();

    public abstract String getVersionString();

    public abstract PacketRegistry getPacketRegistry();

    public ItemState[] getItemStates() {
        return this.itemStates;
    }

    public NBTCompound getBiomeDefinitions() {
        return this.biomesDefinitions;
    }

}
