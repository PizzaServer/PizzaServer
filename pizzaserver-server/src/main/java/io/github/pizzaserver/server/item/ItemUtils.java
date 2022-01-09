package io.github.pizzaserver.server.item;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;

public class ItemUtils {

    private ItemUtils() {}


    /**
     * Serialize the item stack to be network ready.
     * @param version Minecraft version to serialize against
     * @return serialized data
     */
    public static ItemData serializeForNetwork(ItemStack itemStack, MinecraftVersion version) {
        return ItemData.builder()
                .id(version.getItemRuntimeId(itemStack.getItemType().getItemId()))
                .netId(itemStack.getNetworkId())
                .count(itemStack.getCount())
                .damage(itemStack.getMeta())
                .canBreak(itemStack.getBlocksCanBreak().stream().map(Block::getBlockId).toArray(String[]::new))
                .canPlace(itemStack.getBlocksCanPlaceOn().stream().map(Block::getBlockId).toArray(String[]::new))
                .tag(itemStack.getNBT())
                .usingNetId(true)
                .build();
    }

    public static NbtMap serializeWithSlotForDisk(ItemStack itemStack) {
        return NbtMap.builder()
                .putString("Name", itemStack.getItemType().getItemId())
                .putShort("Damage", (short) itemStack.getMeta())
                .putByte("Count", (byte) itemStack.getCount())
                .putCompound("tag", itemStack.getNBT())
                .build();
    }

    public static ItemStack deserializeDiskNBTItem(NbtMap itemNBT) {
        String itemId = itemNBT.getString("Name");
        int meta = itemNBT.getShort("Damage");
        int count = itemNBT.getByte("Count");
        NbtMap nbtTag = itemNBT.getCompound("tag");

        ItemStack itemStack = new ItemStack(itemId, meta, count);
        itemStack.setNBT(nbtTag);
        return itemStack;
    }

    public static ItemStack deserializeNetworkItem(ItemData itemData, MinecraftVersion version) {
        ItemStack itemStack = new ItemStack(version.getItemName(itemData.getId()), itemData.getCount(), itemData.getDamage(), itemData.getNetId());
        itemStack.setNBT(itemData.getTag());
        return itemStack;
    }

}
