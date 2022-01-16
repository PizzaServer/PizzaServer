package io.github.pizzaserver.server.item;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;

public class ItemUtils {

    private ItemUtils() { }


    /**
     * Serialize the item stack to be network ready.
     * @param version Minecraft version to serialize against
     * @return serialized data
     */
    public static ItemData serializeForNetwork(Item item, MinecraftVersion version) {
        return ItemData.builder()
                       .id(version.getItemRuntimeId(item.getItemId()))
                       .netId(item.getNetworkId())
                       .count(item.getCount())
                       .damage(item.getMeta())
                       .canBreak(item.getBlocksCanBreak().toArray(String[]::new))
                       .canPlace(item instanceof ItemBlock blockItem ? blockItem.getBlocksCanPlaceOn()
                                                                                .toArray(String[]::new) : new String[0])
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
        Item itemStack = ItemRegistry.getInstance()
                                     .getItem(version.getItemName(itemData.getId()),
                                              itemData.getCount(),
                                              itemData.getDamage())
                                     .newNetworkCopy(itemData.getNetId());
        itemStack.setNBT(itemData.getTag());
        return itemStack;
    }
}
