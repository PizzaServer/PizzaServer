package io.github.pizzaserver.server.item;

import com.google.gson.JsonObject;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;

public class ItemUtils {

    private ItemUtils() {}


    /**
     * Serialize the item stack to be network ready.
     * @param version Minecraft version to serialize against
     * @return serialized data
     */
    public static ItemData serializeForNetwork(Item item, MinecraftVersion version) {
        int blockRuntimeId = item instanceof ItemBlock itemBlock ? version.getBlockRuntimeId(itemBlock.getBlock().getBlockId(), itemBlock.getBlock().getNBTState()) : 0;
        return ItemData.builder()
                .id(version.getItemRuntimeId(item.getItemId()))
                .netId(item.getNetworkId())
                .blockRuntimeId(blockRuntimeId)
                .count(item.getCount())
                .damage(item.getMeta())
                .canBreak(item.getBlocksCanBreak().toArray(String[]::new))
                .canPlace(item instanceof ItemBlock blockItem ? blockItem.getBlocksCanPlaceOn().toArray(String[]::new) : new String[0])
                .tag(item.getNBT())
                .usingNetId(true)
                .build();
    }

    public static NbtMap serializeWithSlotForDisk(Item item) {
        return NbtMap.builder()
                .putString("Name", item.getItemId())
                .putShort("Damage", (short) item.getMeta())
                .putByte("Count", (byte) item.getCount())
                .putCompound("tag", item.getNBT())
                .build();
    }

    public static Item deserializeDiskNBTItem(NbtMap itemNBT) {
        String itemId = itemNBT.getString("Name");
        int meta = itemNBT.getShort("Damage");
        int count = itemNBT.getByte("Count");
        NbtMap nbtTag = itemNBT.getCompound("tag");

        Item itemStack = ItemRegistry.getInstance().getItem(itemId, count, meta);
        itemStack.setNBT(nbtTag);
        return itemStack;
    }

    public static Item deserializeNetworkItem(ItemData itemData, MinecraftVersion version) {
        Item itemStack = ItemRegistry.getInstance().getItem(version.getItemName(itemData.getId()), itemData.getCount(), itemData.getDamage())
                .newNetworkCopy(itemData.getNetId());
        itemStack.setNBT(itemData.getTag());
        return itemStack;
    }

    public static Item fromJSON(JsonObject itemJSON, MinecraftVersion version) {
        String itemId = itemJSON.get("id").getAsString();
        int count = itemJSON.has("count") ? itemJSON.get("count").getAsInt() : 1;
        int meta = itemJSON.has("damage") ? itemJSON.get("damage").getAsInt() : 0;
        int blockRuntimeId = itemJSON.has("blockRuntimeId") ? itemJSON.get("blockRuntimeId").getAsInt() : 0;

        if (!ItemRegistry.getInstance().hasItem(itemId)) {
            // TODO: throw exception after all blocks/items implemented.
            return null;
        }

        if (blockRuntimeId != 0) {
            Block block = version.getBlockFromRuntimeId(blockRuntimeId);
            if (block == null) {
                // TODO: throw exception after all blocks/items implemented.
                return null;
            }

            if (itemJSON.has("damage")) {
                return new ItemBlock(block, count, meta);
            } else {
                return new ItemBlock(block, count);
            }
        }

        return ItemRegistry.getInstance().getItem(itemId, count, meta);
    }

}
