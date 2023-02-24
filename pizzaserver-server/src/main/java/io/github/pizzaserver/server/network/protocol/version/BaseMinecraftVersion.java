package io.github.pizzaserver.server.network.protocol.version;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.nukkitx.blockstateupdater.BlockStateUpdaters;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.protocol.bedrock.data.BlockPropertyData;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.data.inventory.CraftingData;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.item.CreativeRegistry;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;
import io.github.pizzaserver.api.recipe.RecipeRegistry;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.api.utils.ServerState;
import io.github.pizzaserver.format.MinecraftSerializationHandler;
import io.github.pizzaserver.server.blockentity.handler.BlockEntityHandler;
import io.github.pizzaserver.server.item.ImplItemRegistry;
import io.github.pizzaserver.server.item.ItemUtils;
import io.github.pizzaserver.server.network.protocol.exception.ProtocolException;
import io.github.pizzaserver.server.player.ImplPlayer;
import io.github.pizzaserver.server.recipe.RecipeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseMinecraftVersion implements MinecraftVersion, MinecraftSerializationHandler {

    protected static final Gson GSON = new Gson();

    protected NbtMap biomesDefinitions;
    protected NbtMap availableEntities;
    protected final BiMap<BlockStateData, Integer> blockStates = HashBiMap.create();
    protected final BiMap<String, Integer> itemRuntimeIds = HashBiMap.create();
    protected final List<StartGamePacket.ItemEntry> itemEntries = new ArrayList<>();
    protected final List<Item> creativeItems = new ArrayList<>();
    protected final List<Recipe> defaultRecipes = new ArrayList<>();

    protected final List<CraftingData> networkRecipes = new ArrayList<>();
    protected final List<ItemData> networkCreativeItems = new ArrayList<>();
    protected final List<ComponentItemData> itemComponents = new ArrayList<>();
    protected final List<BlockPropertyData> customBlockProperties = new ArrayList<>();


    public BaseMinecraftVersion() throws IOException {
        this.loadBlockStates();
        this.loadRuntimeItems();
        this.loadBiomeDefinitions();
        this.loadEntitiesNBT();
        this.loadDefaultCreativeItems();
        this.loadDefaultRecipes();
    }

    protected abstract void loadBiomeDefinitions() throws IOException;

    protected abstract void loadBlockStates() throws IOException;

    protected abstract void loadRuntimeItems() throws IOException;

    protected abstract void loadDefaultCreativeItems() throws IOException;

    protected abstract void loadDefaultRecipes() throws IOException;

    protected abstract void loadEntitiesNBT();

    /**
     * Convert a block to its property data format to be sent over the network.
     * Used for sending custom block properties.
     * @param block the block to convert
     * @return BlockPropertyData
     */

    protected abstract BlockPropertyData getBlockPropertyData(Block block);

    /**
     * Convert an item to its item nbt component format to be sent over the network.
     * Used for sending custom item properties.
     * @param item the item to convert
     * @return nbt map with the item components
     */
    protected abstract NbtMap getItemComponentNBT(Item item);

    /**
     * Builds all caches that should be run after registration. (e.g. block property/item components/crafting/creative data)
     */
    public void rebuildCaches() {
        this.customBlockProperties.clear();
        for (Block block : BlockRegistry.getInstance().getCustomBlocks()) {
            this.customBlockProperties.add(this.getBlockPropertyData(block));
        }

        this.itemComponents.clear();
        for (Item customItem : ((ImplItemRegistry) ItemRegistry.getInstance()).getCustomItems()) {
            this.itemComponents.add(new ComponentItemData(customItem.getItemId(), this.getItemComponentNBT(customItem)));
        }

        this.networkRecipes.clear();
        for (Recipe recipe : RecipeRegistry.getInstance().getRecipes()) {
            this.networkRecipes.add(RecipeUtils.serializeForNetwork(recipe, this));
        }

        this.networkCreativeItems.clear();
        for (Item item : CreativeRegistry.getInstance().getItems()) {
            this.networkCreativeItems.add(ItemUtils.serializeForNetwork(item, this));
        }
    }

    protected InputStream getProtocolResourceStream(String fileName) {
        return Server.getInstance().getClass().getResourceAsStream("/protocol/v" + this.getProtocol() + "/" + fileName);
    }

    @Override
    public int getBlockRuntimeId(String name, NbtMap state) {
        NbtMap updatedBlockNBT = getUpdatedBlockNBT(name, state);
        BlockStateData key = new BlockStateData(updatedBlockNBT.getString("name"), updatedBlockNBT.getCompound("states"));

        if (!this.blockStates.containsKey(key)) {
            throw new ProtocolException(this, "No such block state exists for block id: " + name + " " + state);
        }

        return this.blockStates.get(key);
    }

    @Override
    public Block getBlockFromRuntimeId(int blockRuntimeId) {
        if (!this.blockStates.containsValue(blockRuntimeId)) {
            throw new ProtocolException(this, "No such block state exists for runtime id: " + blockRuntimeId);
        }

        BlockStateData blockStateData = this.blockStates.inverse().get(blockRuntimeId);

        if (BlockRegistry.getInstance().hasBlock(blockStateData.getBlockId())) {
            Block block = BlockRegistry.getInstance().getBlock(blockStateData.getBlockId());
            block.setBlockState(blockStateData.getNBT());
            return block;
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
        if (runtimeId == 0) {
            return BlockID.AIR;
        }

        if (this.itemRuntimeIds.inverse().containsKey(runtimeId)) {
            return this.itemRuntimeIds.inverse().get(runtimeId);
        } else {
            throw new ProtocolException(this, "Attempted to retrieve item name for non-existent runtime id: " + runtimeId);
        }
    }

    @Override
    public NbtMap getNetworkBlockEntityNBT(NbtMap diskBlockEntityNBT) {
        return BlockEntityHandler.toNetworkNBT(diskBlockEntityNBT);
    }

    public NbtMap getBiomeDefinitions() {
        return this.biomesDefinitions;
    }

    public NbtMap getEntityIdentifiers() {
        return this.availableEntities;
    }

    public List<StartGamePacket.ItemEntry> getItemEntries() {
        return Collections.unmodifiableList(this.itemEntries);
    }

    public List<Item> getDefaultCreativeItems() {
        return Collections.unmodifiableList(this.creativeItems);
    }

    public List<Recipe> getDefaultRecipes() {
        return this.defaultRecipes;
    }

    public List<ItemData> getCreativeData() {
        return this.networkCreativeItems;
    }

    public List<CraftingData> getCraftingData() {
        return this.networkRecipes;
    }

    public List<BlockPropertyData> getCustomBlockProperties() {
        return this.customBlockProperties;
    }

    public List<ComponentItemData> getItemComponents() {
        return Collections.unmodifiableList(this.itemComponents);
    }

    /**
     * Upgrades block nbt to the latest version.
     * @param id block id
     * @param states block state nbt
     * @return block nbt wrapper with the id and states
     */
    protected static NbtMap getUpdatedBlockNBT(String id, NbtMap states) {
        NbtMap blockStateWithWrapper = NbtMap.builder()
                .putString("name", id)
                .putCompound("states", states)
                .build();

        NbtMapBuilder blockStateBuilder = BlockStateUpdaters.updateBlockState(blockStateWithWrapper, 0)
                .toBuilder();
        blockStateBuilder.remove("version");

        return blockStateBuilder.build();
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
            if (obj instanceof BlockStateData otherStateData) {
                return otherStateData.getBlockId().equals(this.getBlockId())
                        && otherStateData.getNBT().equals(this.getNBT());
            }
            return false;
        }

    }

}
