package io.github.pizzaserver.server.network.protocol.versions;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.nukkitx.nbt.*;
import com.nukkitx.protocol.bedrock.data.BlockPropertyData;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.network.protocol.exceptions.ProtocolException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class BaseMinecraftVersion implements MinecraftVersion {

    protected static final Gson GSON = new Gson();

    protected NbtMap biomesDefinitions;
    protected NbtMap availableEntities;
    protected final BiMap<BlockStateData, Integer> blockStates = HashBiMap.create();
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
        BlockStateData key = new BlockStateData(name, state);
        return this.blockStates.get(key);
    }

    @Override
    public Block getBlockFromRuntimeId(int blockRuntimeId) {
        if (!this.blockStates.inverse().containsKey(blockRuntimeId)) {
            throw new ProtocolException(this, "No such block state exists for runtime id: " + blockRuntimeId);
        }

        BlockStateData blockStateData = this.blockStates.inverse().get(blockRuntimeId);

        if (BlockRegistry.getInstance().hasBlockType(blockStateData.getBlockId())) {
            BlockType blockType = BlockRegistry.getInstance().getBlockType(blockStateData.getBlockId());
            return blockType.create(blockType.getBlockStateIndex(blockStateData.getNBT()));
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
    public NbtMap getNetworkBlockEntityNBT(NbtMap diskBlockEntityNBT) {
        String blockEntityId = diskBlockEntityNBT.getString("id");
        BlockEntityType blockEntityType = ImplServer.getInstance().getBlockEntityRegistry().getBlockEntityType(blockEntityId);
        return blockEntityType.serializeForNetwork(diskBlockEntityNBT);
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

    protected static class BlockStateData {

        private final String blockId;
        private final NbtMap nbtData;


        public BlockStateData(String blockId, NbtMap nbtData) {
            this.blockId = blockId;
            this.nbtData = nbtData;
        }

        public String getBlockId() {
            return this.blockId;
        }

        public NbtMap getNBT() {
            return this.nbtData;
        }

        @Override
        public int hashCode() {
            return this.nbtData.hashCode() * 43 + this.getBlockId().hashCode() * 43;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof BlockStateData) {
                BlockStateData otherStateData = (BlockStateData) obj;
                return otherStateData.getBlockId().equals(this.getBlockId())
                        && otherStateData.getNBT().equals(this.getNBT());
            }
            return false;
        }

    }

}
