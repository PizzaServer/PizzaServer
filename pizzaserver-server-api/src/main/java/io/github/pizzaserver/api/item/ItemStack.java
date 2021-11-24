package io.github.pizzaserver.api.item;

import com.nukkitx.nbt.NbtList;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import io.github.pizzaserver.api.level.world.blocks.BlockState;
import io.github.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.pizzaserver.api.item.types.BlockItemType;
import io.github.pizzaserver.api.item.types.ItemType;
import io.github.pizzaserver.api.item.types.components.DurableItemComponent;
import io.github.pizzaserver.api.network.protocol.versions.MinecraftVersion;

import java.util.*;

public class ItemStack implements Cloneable {

    public static int ID = 1;

    private int networkId;
    protected ItemType itemType;
    protected int count;
    protected int meta;
    protected NbtMap compound = NbtMap.EMPTY;

    protected Set<BlockState> blocksCanBreak;
    protected Set<BlockState> blocksCanPlaceOn = Collections.emptySet();


    public ItemStack(String itemId) {
        this(ItemRegistry.getItemType(itemId));
    }

    public ItemStack(String itemId, int count) {
        this(ItemRegistry.getItemType(itemId), count);
    }

    public ItemStack(String itemId, int count, int meta) {
        this(ItemRegistry.getItemType(itemId), count, meta);
    }

    public ItemStack(String itemId, int count, int meta, int networkId) {
        this(ItemRegistry.getItemType(itemId), count, meta, networkId);
    }

    public ItemStack(ItemType itemType) {
        this(itemType, 1);
    }

    public ItemStack(ItemType itemType, int count) {
        this(itemType, count, 0);
    }

    public ItemStack(ItemType itemType, int count, int meta) {
        this(itemType, count, meta, itemType.getItemId().equals(BlockTypeID.AIR) ? 0 : -1);
    }

    public ItemStack(ItemType itemType, int count, int meta, int networkId) {
        this.itemType = count <= 0 ? ItemRegistry.getItemType(BlockTypeID.AIR) : itemType;
        this.networkId = this.isEmpty() ? 0 : networkId;
        this.count = this.isEmpty() ? 0 : count;
        this.meta = meta;

        this.blocksCanBreak = itemType.getOnlyBlocksCanBreak();
        if (itemType instanceof BlockItemType) {
            this.blocksCanPlaceOn = ((BlockItemType) itemType).getBlockType().getPlaceableOnlyOn(this.meta);
        }
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        if (!this.isEmpty()) {
            if (count <= 0) {
                this.count = 0;
                this.networkId = 0;
                this.itemType = ItemRegistry.getItemType(BlockTypeID.AIR);
            } else {
                this.count = count;
            }
        }
    }

    public int getMeta() {
        return this.meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }

    public boolean hasCustomName() {
        return this.getCompoundTag().containsKey("display")
                && this.getCompoundTag().get("display") instanceof NbtMap
                && this.getCompoundTag().getCompound("display").containsKey("Name")
                && this.getCompoundTag().getCompound("display").get("Name") instanceof String;
    }

    public Optional<String> getCustomName() {
        if (!this.hasCustomName()) {
            return Optional.empty();
        }
        return Optional.of(this.getCompoundTag().getCompound("display").getString("Name"));
    }

    public void setCustomName(String customName) {
        if (customName != null) {
            // Set custom name
            if (!(this.getCompoundTag().containsKey("display")) || !(this.getCompoundTag().get("display") instanceof NbtMap)) {
                this.getCompoundTag().put("display", NbtMap.EMPTY);
            }
            this.getCompoundTag().getCompound("display").put("Name", customName);
        } else if (this.hasCustomName()) {
            // delete custom name
            NbtMap displayCompound = this.getCompoundTag().getCompound("display");
            displayCompound.remove("Name");
            if (displayCompound.size() == 0) {  // Check if we can delete the compound
                this.getCompoundTag().remove("display");
            }
        }
    }

    public boolean hasLore() {
        return this.getCompoundTag().containsKey("display")
                && this.getCompoundTag().get("display") instanceof NbtMap
                && this.getCompoundTag().getCompound("display").containsKey("Lore")
                && this.getCompoundTag().getCompound("display").get("Lore") instanceof NbtList;
    }

    /**
     * Returns the id of this stack as represented over the network.
     * An id of -1 means that this stack has not been assigned a network id
     * @return stack id
     */
    public int getNetworkId() {
        return this.networkId;
    }

    /**
     * Create a copy of this ItemStack but with a new network id assigned.
     * @return new ItemStack with a new network id assigned
     */
    public ItemStack newNetworkStack() {
        int networkId = this.isEmpty() ? 0 : ItemStack.ID++;
        ItemStack newStack = new ItemStack(this.getItemType(), this.getCount(), this.getMeta(), networkId);
        newStack.setCompoundTag(this.getCompoundTag());
        newStack.setBlocksCanBreak(this.getBlocksCanBreak());
        newStack.setBlocksCanPlaceOn(this.getBlocksCanPlaceOn());
        return newStack;
    }

    public List<String> getLore() {
        if (this.hasLore()) {
            return this.getCompoundTag().getCompound("display").getList("Lore", NbtType.STRING);
        } else {
            return Collections.emptyList();
        }
    }

    public void setLore(List<String> lore) {
        if (lore != null) {
            // Set lore
            if (!(this.getCompoundTag().containsKey("display")) || !(this.getCompoundTag().get("display") instanceof NbtMap)) {
                this.getCompoundTag().toBuilder().putCompound("display", NbtMap.EMPTY).build();
            }
            this.getCompoundTag().getCompound("display").toBuilder().putList("Lore", NbtType.STRING, lore).build();
        } else if (this.hasLore()) {
            // Delete lore
            NbtMap displayCompound = this.getCompoundTag().getCompound("display");
            displayCompound.remove("Lore");
            if (displayCompound.size() == 0) {  // Check if we can delete the compound
                this.getCompoundTag().remove("display");
            }
        }
    }

    public NbtMap getCompoundTag() {
        return this.compound;
    }

    public void setCompoundTag(NbtMap compound) {
        this.compound = compound;
    }

    public Set<BlockState> getBlocksCanBreak() {
        return Collections.unmodifiableSet(this.blocksCanBreak);
    }

    public void setBlocksCanBreak(Set<BlockState> blocksCanBreak) {
        this.blocksCanBreak = blocksCanBreak;
    }

    /**
     * Only applicable for ItemStacks that have an item type that can place blocks.
     * @return all of the blocks that this item can be placed on
     */
    public Set<BlockState> getBlocksCanPlaceOn() {
        return Collections.unmodifiableSet(this.blocksCanPlaceOn);
    }

    /**
     * Only applicable for ItemStacks that have an item type that can place blocks.
     * @param blocksCanPlaceOn the blocks that this item can be placed on
     */
    public void setBlocksCanPlaceOn(Set<BlockState> blocksCanPlaceOn) {
        this.blocksCanPlaceOn = blocksCanPlaceOn;
    }

    /**
     * Checks if this ItemStack has the same data as another ItemStack.
     * @param otherStack the other ItemStack we are trying to check
     * @return if the two stacks have the same data
     */
    public boolean hasSameDataAs(ItemStack otherStack) {
        return (otherStack.getItemType().equals(this.getItemType())
                && otherStack.getCompoundTag().equals(this.getCompoundTag())
                && otherStack.getMeta() == this.getMeta()) || otherStack.getItemType().getItemId().equals(BlockTypeID.AIR);
    }

    /**
     * Checks if this ItemStack is air.
     * @return if the ItemStack is air
     */
    public boolean isEmpty() {
        return this.getItemType().getItemId().equals(BlockTypeID.AIR);
    }

    /**
     * Checks if an ItemStack looks the same compared to this ItemStack.
     * @param otherItemStack the other ItemStack
     * @return if they are visually the same
     */
    public boolean visuallyEquals(ItemStack otherItemStack) {
        return otherItemStack.getCompoundTag().equals(this.getCompoundTag())
                && (otherItemStack.getMeta() == this.getMeta() || (otherItemStack.getItemType() instanceof DurableItemComponent))
                && otherItemStack.getItemType().equals(this.getItemType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStack) {
            ItemStack otherItemStack = (ItemStack) obj;

            return otherItemStack.getItemType().equals(this.getItemType())
                    && otherItemStack.getMeta() == this.getMeta()
                    && otherItemStack.getCount() == this.getCount()
                    && otherItemStack.getCompoundTag().equals(this.getCompoundTag());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getItemType().hashCode() * 73;
    }

    @Override
    public ItemStack clone() {
        try {
            ItemStack clone = (ItemStack) super.clone();
            clone.setCompoundTag(this.getCompoundTag().toBuilder().build());
            clone.setBlocksCanBreak(new HashSet<>(this.getBlocksCanBreak()));
            clone.setBlocksCanPlaceOn(new HashSet<>(this.getBlocksCanPlaceOn()));
            return clone;
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError();
        }
    }

    /**
     * Serialize the item stack to be network ready.
     * @param version Minecraft version to serialize against
     * @return serialized data
     */
    public ItemData serialize(MinecraftVersion version) {
        return ItemData.builder()
                .id(version.getItemRuntimeId(this.getItemType().getItemId()))
                .netId(this.getNetworkId())
                .count(this.getCount())
                .damage(this.getMeta())
                .canBreak(this.getBlocksCanBreak().stream().map(BlockState::getBlockId).toArray(String[]::new))
                .canPlace(this.getBlocksCanPlaceOn().stream().map(BlockState::getBlockId).toArray(String[]::new))
                .tag(this.getCompoundTag())
                .usingNetId(true)
                .build();
    }


    /**
     * Ensures that the ItemStack provided will exist.
     * If the ItemStack provided is null, it will return an air ItemStack
     * @param itemStack nullable item stack
     * @return item stack
     */
    public static ItemStack ensureItemStackExists(ItemStack itemStack) {
        return itemStack == null ? ItemRegistry.getItem(BlockTypeID.AIR) : itemStack;
    }

    public static ItemStack deserialize(ItemData itemData, MinecraftVersion version) {
        ItemStack itemStack = new ItemStack(version.getItemName(itemData.getId()), itemData.getCount(), itemData.getDamage(), itemData.getNetId());
        itemStack.setCompoundTag(itemData.getTag());
        return itemStack;
    }

}
