package io.github.pizzaserver.server.network.protocol.version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nukkitx.nbt.*;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.data.BlockPropertyData;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v475.Bedrock_v475;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.pizzaserver.api.item.CustomItem;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.descriptors.*;
import io.github.pizzaserver.server.network.utils.MinecraftNamespaceComparator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class V475MinecraftVersion extends BaseMinecraftVersion {

    public static final int PROTOCOL = 475;
    public static final String VERSION = "1.18";

    public V475MinecraftVersion() throws IOException { }


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
        return Bedrock_v475.V475_CODEC;
    }

    @Override
    protected void loadBiomeDefinitions() throws IOException {
        try (NBTInputStream inputStream = NbtUtils.createNetworkReader(this.getProtocolResourceStream(
                "biome_definitions.nbt"))) {
            this.biomesDefinitions = (NbtMap) inputStream.readTag();
        }
    }

    @Override
    protected void loadBlockStates() throws IOException {
        try (InputStream blockStatesFileStream = this.getProtocolResourceStream("block_states.nbt"); NBTInputStream blockStatesNBTStream = NbtUtils.createNetworkReader(
                blockStatesFileStream)) {
            // keySet returns in ascending rather than descending so we have to reverse it
            SortedMap<String, List<NbtMap>> sortedBlockRuntimeStates = new TreeMap<>(Collections.reverseOrder(
                    MinecraftNamespaceComparator::compare));

            // Parse block states
            while (blockStatesFileStream.available() > 0) {
                NbtMap blockState = (NbtMap) blockStatesNBTStream.readTag();
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

    protected BlockPropertyData getBlockPropertyData(Block block) {
        NbtMapBuilder componentsNBT = NbtMap.builder()
                                            .putCompound("minecraft:block_light_absorption",
                                                         NbtMap.builder()
                                                               .putInt("value", block.getLightAbsorption())
                                                               .build())
                                            .putCompound("minecraft:block_light_emission",
                                                         NbtMap.builder()
                                                               .putFloat("emission", block.getLightEmission())
                                                               .build())
                                            .putCompound("minecraft:friction",
                                                         NbtMap.builder()
                                                               .putFloat("value", block.getFriction())
                                                               .build())
                                            .putCompound("minecraft:rotation",
                                                         NbtMap.builder()
                                                               .putFloat("x", 0)
                                                               .putFloat("y", 0)
                                                               .putFloat("z", 0)
                                                               .build());
        if (block.getGeometry().isPresent()) {
            componentsNBT.putCompound("minecraft:geometry",
                                      NbtMap.builder().putString("value", block.getGeometry().get()).build());
        }

        return new BlockPropertyData(block.getBlockId(),
                                     NbtMap.builder().putCompound("components", componentsNBT.build()).build());
    }

    @Override
    protected void loadRuntimeItems() throws IOException {
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
                this.itemEntries.add(new StartGamePacket.ItemEntry(itemId, (short) runtimeId, false));
            }
            this.itemRuntimeIds.put("minecraft:air",
                                    0);    // A void item is equal to 0 and this reduces data sent over the network

            // Register custom items
            for (CustomItem customItem : ItemRegistry.getInstance().getCustomItems()) {
                int runtimeId = customItemIdStart++;
                this.itemRuntimeIds.put(customItem.getItemId(), runtimeId);
                this.itemEntries.add(new StartGamePacket.ItemEntry(customItem.getItemId(), (short) runtimeId, true));
            }

            //Register custom block items
            int customBlockIdStart = 1000;
            // Block item runtime ids are decided by the order they are sent via the StartGamePacket in the block properties
            // Block properties are sent sorted by their namespace according to Minecraft's namespace sorting.
            // So we will sort it the same way here
            SortedSet<Block> sortedCustomBlocks = new TreeSet<>((blockTypeA, blockTypeB) -> MinecraftNamespaceComparator.compare(
                    blockTypeA.getBlockId(),
                    blockTypeB.getBlockId()));
            sortedCustomBlocks.addAll(BlockRegistry.getInstance().getCustomBlocks());
            for (Block customBlock : sortedCustomBlocks) {
                this.itemRuntimeIds.put(customBlock.getBlockId(),
                                        255 - customBlockIdStart++);  // (255 - index) = item runtime id
            }
        }
    }

    @Override
    protected void loadEntitiesNBT() {
        List<NbtMap> entities = new ArrayList<>();
        int rId = 0;    // TODO: what is the purpose of this?
        for (EntityDefinition definition : EntityRegistry.getInstance().getDefinitions()) {
            entities.add(NbtMap.builder()
                               .putString("bid", "")
                               .putBoolean("hasspawnegg", definition.hasSpawnEgg())
                               .putString("id", definition.getId())
                               .putInt("rid", rId++)
                               .putBoolean("summonable", definition.isSummonable())
                               .build());
        }
        this.availableEntities = NbtMap.builder().putList("idlist", NbtType.COMPOUND, entities).build();
    }

    @Override
    protected void loadItemComponents() {
        this.itemComponents.clear();
        for (CustomItem customItem : ItemRegistry.getInstance().getCustomItems()) {
            this.itemComponents.add(new ComponentItemData(customItem.getItemId(),
                                                          this.getItemComponentNBT(customItem)));
        }
    }

    protected NbtMap getItemComponentNBT(CustomItem item) {
        NbtMapBuilder container = NbtMap.builder();
        container.putInt("id", this.getItemRuntimeId(item.getItemId())).putString("name", item.getItemId());

        NbtMapBuilder components = NbtMap.builder();

        // Write non-required components if present
        if (item instanceof ArmorItemComponent armorItemComponent) {
            components.putCompound("minecraft:armor",
                                   NbtMap.builder().putInt("protection", armorItemComponent.getProtection()).build());
        }
        if (item instanceof CooldownItemComponent cooldownItemComponent) {
            components.putCompound("minecraft:cooldown",
                                   NbtMap.builder()
                                         .putString("category", cooldownItemComponent.getCooldownCategory())
                                         .putFloat("duration", (cooldownItemComponent.getCooldownTicks() * 20) / 20f)
                                         .build());
        }
        if (item instanceof DurableItemComponent durableItemComponent) {
            components.putCompound("minecraft:durability",
                                   NbtMap.builder()
                                         .putInt("max_durability", durableItemComponent.getMaxDurability())
                                         .build());
        }
        if (item instanceof FoodItemComponent foodItemComponent) {
            components.putCompound("minecraft:food",
                                   NbtMap.builder()
                                         .putBoolean("can_always_eat", foodItemComponent.canAlwaysBeEaten())
                                         .build());
        }
        if (item instanceof PlantableItemComponent) {
            components.putCompound("minecraft:block_placer", NbtMap.EMPTY);
        }

        NbtMap itemProperties = NbtMap.builder()
                                      .putCompound("minecraft:icon",
                                                   NbtMap.builder().putString("texture", item.getIconName()).build())
                                      .putBoolean("allow_off_hand", item.isAllowedInOffHand())
                                      .putInt("creative_category", 2)
                                      .putInt("damage", item.getDamage())
                                      .putBoolean("foil", item.hasFoil())
                                      .putBoolean("hand_equipped", item.isHandEquipped())
                                      .putBoolean("liquid_clipped", item.canUseOnLiquid())
                                      .putInt("max_stack_size", item.getMaxStackSize())
                                      .putFloat("mining_speed",
                                                0)  // Block breaking is handled server-side. Doing this gives greater block break control in the item type class
                                      .putBoolean("mirrored_art", item.isMirroredArt())
                                      .putBoolean("stacked_by_data", item.isStackedByMeta())
                                      .putInt("use_animation",
                                              item instanceof FoodItemComponent foodItemComponent ? foodItemComponent.getUseAnimationType()
                                                                                                                     .ordinal() : 0)
                                      .putInt("use_duration",
                                              item instanceof FoodItemComponent foodItemComponent ? foodItemComponent.getUseDurationTicks() : 0)
                                      .build();
        components.putCompound("item_properties", itemProperties);

        container.putCompound("components", components.build());
        return container.build();
    }
}
