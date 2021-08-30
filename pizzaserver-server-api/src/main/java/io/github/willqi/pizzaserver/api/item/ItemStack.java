package io.github.willqi.pizzaserver.api.item;

import io.github.willqi.pizzaserver.api.item.types.BaseItemType;
import io.github.willqi.pizzaserver.api.item.types.BlockItemType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTList;
import io.github.willqi.pizzaserver.nbt.tags.NBTTag;

import java.util.*;

public class ItemStack implements Cloneable {

    public static int ID = 1;

    private int networkId;
    private BaseItemType itemType;
    private int count;
    private int damage;
    private NBTCompound compound = new NBTCompound();

    private Set<BaseBlockType> blocksCanBreak;
    private Set<BaseBlockType> blocksCanPlaceOn = Collections.emptySet();


    public ItemStack(String itemId) {
        this(ItemRegistry.getItemType(itemId));
    }

    public ItemStack(String itemId, int count) {
        this(ItemRegistry.getItemType(itemId), count);
    }

    public ItemStack(String  itemId, int count, int damage) {
        this(ItemRegistry.getItemType(itemId), count, damage);
    }

    public ItemStack(BaseItemType itemType) {
        this(itemType, 1);
    }

    public ItemStack(BaseItemType itemType, int count) {
        this(itemType, count, 0);
    }

    public ItemStack(BaseItemType itemType, int count, int damage) {
        this(itemType, count, damage, itemType.getItemId().equals(BlockTypeID.AIR) ? 0 : -1);
    }

    public ItemStack(BaseItemType itemType, int count, int damage, int networkId) {
        this.itemType = count <= 0 ? ItemRegistry.getItemType(BlockTypeID.AIR) : itemType;
        this.networkId = this.itemType.getItemId().equals(BlockTypeID.AIR) ? 0 : networkId;
        this.count = this.itemType.getItemId().equals(BlockTypeID.AIR) ? 0 : count;
        this.damage = damage;

        this.blocksCanBreak = itemType.getOnlyBlocksCanBreak();
        if (itemType instanceof BlockItemType) {
            this.blocksCanPlaceOn = ((BlockItemType)itemType).getBlockType().getPlaceableOnlyOn();
        }
    }

    public BaseItemType getItemType() {
        return this.itemType;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        if (!this.itemType.getItemId().equals(BlockTypeID.AIR)) {
            if (count <= 0) {
                this.count = 0;
                this.networkId = 0;
                this.itemType = ItemRegistry.getItemType(BlockTypeID.AIR);
            } else {
                this.count = count;
            }
        }
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean hasCustomName() {
        return this.getCompoundTag().containsKey("display") &&
                this.getCompoundTag().get("display") instanceof NBTCompound &&
                this.getCompoundTag().getCompound("display").containsKey("Name") &&
                this.getCompoundTag().getCompound("display").get("Name") instanceof String;
    }

    public String getCustomName() {
        if (!this.hasCustomName()) {
            return null;
        }
        return this.getCompoundTag().getCompound("display").getString("Name");
    }

    public void setCustomName(String customName) {
        if (customName != null) {
            // Set custom name
            if (!(this.getCompoundTag().containsKey("display")) || !(this.getCompoundTag().get("display") instanceof NBTCompound)) {
                this.getCompoundTag().putCompound("display", new NBTCompound());
            }
            this.getCompoundTag().getCompound("display").putString("Name", customName);
        } else if (this.hasCustomName()) {
            // delete custom name
            NBTCompound displayCompound = this.getCompoundTag().getCompound("display");
            displayCompound.remove("Name");
            if (displayCompound.size() == 0) {  // Check if we can delete the compound
                this.getCompoundTag().remove("display");
            }
        }
    }

    public boolean hasLore() {
        return this.getCompoundTag().containsKey("display") &&
                this.getCompoundTag().get("display") instanceof NBTCompound &&
                this.getCompoundTag().getCompound("display").containsKey("Lore") &&
                this.getCompoundTag().getCompound("display").get("Lore") instanceof NBTList &&
                this.getCompoundTag().getCompound("display").getList("Lore").getChildrenTypeId() == NBTTag.STRING_TAG_ID;
    }

    /**
     * Returns the id of this stack as represented over the network
     * An id of -1 means that this stack has not been assigned a network id
     * @return stack id
     */
    public int getNetworkId() {
        return this.networkId;
    }

    /**
     * Create a copy of this ItemStack but with a new network id assigned
     * @return new ItemStack with a new network id assigned
     */
    public ItemStack newNetworkStack() {
        int networkId = this.getItemType().getItemId().equals(BlockTypeID.AIR) ? 0 : ItemStack.ID++;
        return new ItemStack(this.getItemType(), this.getCount(), this.getDamage(), networkId);
    }

    public List<String> getLore() {
        if (this.hasLore()) {
            NBTList<String> lore = this.getCompoundTag().getCompound("display").getList("Lore");
            return Arrays.asList(lore.getContents());
        } else {
            return Collections.emptyList();
        }
    }

    public void setLore(List<String> lore) {
        if (lore != null) {
            // Set lore
            if (!(this.getCompoundTag().containsKey("display")) || !(this.getCompoundTag().get("display") instanceof NBTCompound)) {
                this.getCompoundTag().putCompound("display", new NBTCompound());
            }
            NBTList<String> loreList = new NBTList<>(NBTTag.STRING_TAG_ID);
            loreList.setContents(lore.toArray(new String[0]));
            this.getCompoundTag().getCompound("display").putList("Lore", loreList);
        } else if (this.hasLore()) {
            // Delete lore
            NBTCompound displayCompound = this.getCompoundTag().getCompound("display");
            displayCompound.remove("Lore");
            if (displayCompound.size() == 0) {  // Check if we can delete the compound
                this.getCompoundTag().remove("display");
            }
        }
    }

    public NBTCompound getCompoundTag() {
        return this.compound;
    }

    public void setCompoundTag(NBTCompound compound) {
        this.compound = compound;
    }

    public Set<BaseBlockType> getBlocksCanBreak() {
        return Collections.unmodifiableSet(this.blocksCanBreak);
    }

    public void setBlocksCanBreak(Set<BaseBlockType> blocksCanBreak) {
        this.blocksCanBreak = blocksCanBreak;
    }

    /**
     * Only applicable for ItemStacks that have an item type that can place blocks
     * @return all of the blocks that this item can be placed on
     */
    public Set<BaseBlockType> getBlocksCanPlaceOn() {
        return Collections.unmodifiableSet(this.blocksCanPlaceOn);
    }

    /**
     * Only applicable for ItemStacks that have an item type that can place blocks
     * @param blocksCanPlaceOn the blocks that this item can be placed on
     */
    public void setBlocksCanPlaceOn(Set<BaseBlockType> blocksCanPlaceOn) {
        this.blocksCanPlaceOn = blocksCanPlaceOn;
    }

    @Override
    public int hashCode() {
        return this.getItemType().hashCode() * 73;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStack) {
            ItemStack otherItemStack = (ItemStack)obj;;

            return otherItemStack.getItemType().equals(this.getItemType()) &&
                    otherItemStack.getDamage() == this.getDamage() &&
                    otherItemStack.getCount() == this.getCount() &&
                    otherItemStack.getCompoundTag().equals(this.getCompoundTag());
        }
        return false;
    }

    @Override
    public ItemStack clone() {
        try {
            ItemStack clone = (ItemStack)super.clone();
            clone.setCompoundTag(this.getCompoundTag().clone());
            clone.setBlocksCanBreak(new HashSet<>(this.getBlocksCanBreak()));
            clone.setBlocksCanPlaceOn(new HashSet<>(this.getBlocksCanPlaceOn()));
            return clone;
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError();
        }
    }


    /**
     * Ensures that the ItemStack provided will exist
     * If the ItemStack provided is null, it will return an air ItemStack
     * @param itemStack nullable item stack
     * @return item stack
     */
    public static ItemStack ensureItemStackExists(ItemStack itemStack) {
        return itemStack == null ? ItemRegistry.getItem(BlockTypeID.AIR) : itemStack;
    }

}
