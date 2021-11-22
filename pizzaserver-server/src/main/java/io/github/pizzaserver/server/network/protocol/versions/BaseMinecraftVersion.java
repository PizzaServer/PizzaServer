package io.github.pizzaserver.server.network.protocol.versions;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.nukkitx.nbt.*;
import com.nukkitx.protocol.bedrock.data.BlockPropertyData;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.pizzaserver.api.level.world.blocks.types.BlockType;
import io.github.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.pizzaserver.commons.utils.Tuple;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.network.protocol.exceptions.ProtocolException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class BaseMinecraftVersion implements MinecraftVersion {

    protected static final Gson GSON = new Gson();

    // GLOBAL_BLOCK_STATES stores all possible block states and their indexes
    // the purpose of this is to reduce memory usage
    // when loading the block states for each version
    // as many of them have duplicate id and NBT
    // by storing the integer rather than the tuple into
    // this.blockStates, we can reduce the amount of memory allocated
    // for the tuple and its properties since multiple versions
    // no longer have to recreate a tuple that would use more space
    // than an integer
    protected static final BiMap<Tuple<String, NbtMap>, Integer> GLOBAL_BLOCK_STATES = HashBiMap.create();

    protected NbtMap biomesDefinitions;
    protected NbtMap availableEntities;
    protected final BiMap<Integer, Integer> blockStates = HashBiMap.create();
    protected final List<BlockPropertyData> customBlockProperties = new ArrayList<>();
    protected final BiMap<String, Integer> itemRuntimeIds = HashBiMap.create();
    protected final List<StartGamePacket.ItemEntry> itemEntries = new ArrayList<>();
    protected final List<ComponentItemData> itemComponents = new ArrayList<>();


    public BaseMinecraftVersion() throws IOException {
        this.loadBiomeDefinitions();
        this.loadBlockStates();
        this.loadRuntimeItems();
        this.loadEntitiesNBT();
        this.loadItemComponents();
    }

    protected abstract void loadBiomeDefinitions() throws IOException;

    protected abstract void loadBlockStates() throws IOException;

    protected abstract void loadRuntimeItems() throws IOException;

    protected abstract void loadEntitiesNBT();

    protected abstract void loadItemComponents();

    protected InputStream getProtocolResourceStream(String fileName) {
        return Server.getInstance().getClass().getResourceAsStream("/protocol/v" + this.getProtocol() + "/" + fileName);
    }

    @Override
    public int getBlockRuntimeId(String name, NbtMap state) {
        Tuple<String, NbtMap> key = new Tuple<>(name, state);

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
        Tuple<String, NbtMap> blockData = GLOBAL_BLOCK_STATES.inverse().get(blockStateLookupId);

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

    @Override
    public NbtMap getBiomeDefinitions() {
        return this.biomesDefinitions;
    }

    @Override
    public NbtMap getEntityIdentifiers() {
        return this.availableEntities;
    }

    @Override
    public List<StartGamePacket.ItemEntry> getItemEntries() {
        return Collections.unmodifiableList(this.itemEntries);
    }

    @Override
    public List<BlockPropertyData> getCustomBlockProperties() {
        return this.customBlockProperties;
    }

    @Override
    public List<ComponentItemData> getItemComponents() {
        return Collections.unmodifiableList(this.itemComponents);
    }

}
