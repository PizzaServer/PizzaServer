package io.github.pizzaserver.server.item;

import com.google.gson.JsonObject;
import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;
import io.github.pizzaserver.server.network.protocol.version.BaseMinecraftVersion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemUtils {

    private ItemUtils() {}


    /**
     * Serialize the item stack to be network ready.
     * @param version Minecraft version to serialize against
     * @return serialized data
     */
    public static ItemData serializeForNetwork(Item item, MinecraftVersion version) {
        Item nonAirItem = Item.getAirIfNull(item);
        int blockRuntimeId = nonAirItem instanceof ItemBlock itemBlock ? ((BaseMinecraftVersion) version).getBlockRuntimeId(itemBlock.getBlock().getBlockId(), itemBlock.getBlock().getNBTState()) : 0;

        return ItemData.builder()
                .id(version.getItemRuntimeId(nonAirItem.getItemId()))
                .netId(nonAirItem.getNetworkId())
                .blockRuntimeId(blockRuntimeId)
                .count(nonAirItem.getCount())
                .damage(nonAirItem.getMeta())
                .canBreak(nonAirItem.getBlocksCanBreak().toArray(String[]::new))
                .canPlace(nonAirItem instanceof ItemBlock blockItem ? blockItem.getBlocksCanPlaceOn().toArray(String[]::new) : new String[0])
                .tag(nonAirItem.getNBT())
                .usingNetId(true)
                .build();
    }

    public static NbtMap serializeForDisk(Item item) {
        Item nonAirItem = Item.getAirIfNull(item);

        return NbtMap.builder()
                .putString("Name", nonAirItem.getItemId())
                .putShort("Damage", (short) nonAirItem.getMeta())
                .putByte("Count", (byte) nonAirItem.getCount())
                .putCompound("tag", nonAirItem.getNBT())
                .build();
    }

    public static NbtMap serializeWithSlotForDisk(Item item, int slot) {
        return serializeForDisk(item).toBuilder()
                .putByte("Slot", (byte) slot)
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

    public static Item fromJSON(JsonObject itemJSON, MinecraftVersion version) throws IOException {
        String itemId = itemJSON.get("id").getAsString();
        int count = itemJSON.has("count") ? itemJSON.get("count").getAsInt() : 1;
        int meta = itemJSON.has("damage") && itemJSON.get("damage").getAsShort() != Short.MAX_VALUE ? itemJSON.get("damage").getAsInt() : 0;
        String blockStateNBT = itemJSON.has("block_state_b64") ? itemJSON.get("block_state_b64").getAsString() : null;
        String tagNBT = itemJSON.has("nbt_b64") ? itemJSON.get("nbt_b64").getAsString() : null;

        if (!ItemRegistry.getInstance().hasItem(itemId)) {
            // TODO: throw exception after all blocks/items implemented.
            return null;
        }

        Item item;
        if (blockStateNBT != null) {
            NbtMap blockNBT = stringToNBT(blockStateNBT);
            String blockId = blockNBT.getString("name");
            NbtMap blockState = blockNBT.getCompound("states");

            Block block = version.getBlockFromRuntimeId(version.getBlockRuntimeId(blockId, blockState));
            if (block == null) {
                // TODO: debug log this to as this is a missing block!
                return null;
            }

            item = new ItemBlock(block, count);
        } else {
            item = ItemRegistry.getInstance().getItem(itemId, count, meta);
        }

        if (tagNBT != null) {
            item.setNBT(stringToNBT(tagNBT));
        }

        return item;
    }

    private static NbtMap stringToNBT(String str) throws IOException {
        byte[] nbtData = Base64.getDecoder().decode(str);
        try (NBTInputStream nbtInputStream = NbtUtils.createReaderLE(new ByteArrayInputStream(nbtData))) {
            return  ((NbtMap) nbtInputStream.readTag());
        }
    }

}
