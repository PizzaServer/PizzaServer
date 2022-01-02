package io.github.pizzaserver.api.item;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.BlockState;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.types.BlockItemType;
import io.github.pizzaserver.api.item.types.ItemType;
import io.github.pizzaserver.api.item.types.component.DurableItemComponent;

import java.util.*;

public class ItemStack implements Cloneable {

    public static int ID = 1;

    private int networkId;
    protected ItemType itemType;
    protected int count;
    protected int meta;
    protected NbtMap nbt = NbtMap.EMPTY;

    protected Set<BlockState> blocksCanBreak;
    protected Set<BlockState> blocksCanPlaceOn = Collections.emptySet();


    public ItemStack(String itemId) {
        this(ItemRegistry.getInstance().getItemType(itemId));
    }

    public ItemStack(String itemId, int count) {
        this(ItemRegistry.getInstance().getItemType(itemId), count);
    }

    public ItemStack(String itemId, int count, int meta) {
        this(ItemRegistry.getInstance().getItemType(itemId), count, meta);
    }

    public ItemStack(String itemId, int count, int meta, int networkId) {
        this(ItemRegistry.getInstance().getItemType(itemId), count, meta, networkId);
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
        this.itemType = count <= 0 ? ItemRegistry.getInstance().getItemType(BlockTypeID.AIR) : itemType;
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
                this.itemType = ItemRegistry.getInstance().getItemType(BlockTypeID.AIR);
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

    public String getCustomName() {
        return this.getNBT().getCompound("display").getString("Name");
    }

    public void setCustomName(String customName) {
        NbtMap displayNBT;
        if (customName != null) {
            displayNBT = this.getNBT().getCompound("display").toBuilder()
                    .putString("Name", customName)
                    .build();
        } else {
            NbtMapBuilder displayNBTBuilder = this.getNBT().getCompound("display").toBuilder();
            displayNBTBuilder.remove("Name");
            displayNBT = displayNBTBuilder.build();
        }
        this.setNBT(this.getNBT().toBuilder().putCompound("display", displayNBT).build());
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
        newStack.setNBT(this.getNBT());
        newStack.setBlocksCanBreak(this.getBlocksCanBreak());
        newStack.setBlocksCanPlaceOn(this.getBlocksCanPlaceOn());
        return newStack;
    }

    public List<String> getLore() {
        return this.getNBT().getCompound("display").getList("Lore", NbtType.STRING);
    }

    public void setLore(List<String> lore) {
        NbtMap displayNBT = this.getNBT().getCompound("display").toBuilder()
                .putList("Lore", NbtType.STRING, lore)
                .build();
        this.setNBT(this.getNBT().toBuilder().putCompound("display", displayNBT).build());
    }

    public NbtMap getNBT() {
        return this.nbt;
    }

    public void setNBT(NbtMap nbt) {
        this.nbt = nbt;
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
                && otherStack.getNBT().equals(this.getNBT())
                && otherStack.getMeta() == this.getMeta());
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
        return otherItemStack.getNBT().equals(this.getNBT())
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
                    && otherItemStack.getNBT().equals(this.getNBT());
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
            clone.setNBT(this.getNBT().toBuilder().build());
            clone.setBlocksCanBreak(new HashSet<>(this.getBlocksCanBreak()));
            clone.setBlocksCanPlaceOn(new HashSet<>(this.getBlocksCanPlaceOn()));
            return clone;
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError();
        }
    }

    /**
     * Ensures that the ItemStack provided will exist.
     * If the ItemStack provided is null, it will return an air ItemStack
     * @param itemStack nullable item stack
     * @return item stack
     */
    public static ItemStack ensureItemStackExists(ItemStack itemStack) {
        return itemStack == null ? ItemRegistry.getInstance().getItem(BlockTypeID.AIR) : itemStack;
    }

}
