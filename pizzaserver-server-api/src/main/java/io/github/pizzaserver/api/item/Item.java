package io.github.pizzaserver.api.item;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.behavior.ItemBehavior;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.item.descriptors.ArmorItem;

import java.util.*;

public interface Item extends Cloneable {

    String getItemId();

    String getName();

    /**
     * Returns the id of this stack as represented over the network.
     * An id of -1 means that this stack has not been assigned a network id
     * @return stack id
     */
    int getNetworkId();

    int getCount();

    void setCount(int count);

    int getMaxStackSize();

    /**
     * Returns if this item is separated by its meta.
     * (e.g. durability)
     *
     * @return if this item should be stacked by meta
     */
    boolean isStackedByMeta();

    int getMeta();

    void setMeta(int meta);

    /**
     * Return the amount of damage this item does to an entity.
     * @return damage
     */
    int getDamage();

    Optional<String> getCustomName();

    void setCustomName(String customName);

    List<String> getLore();

    void setLore(List<String> lore);

    NbtMap getNBT();

    void setNBT(NbtMap nbt);

    boolean isAllowedInOffHand();

    boolean canUseOnLiquid();

    Set<String> getBlocksCanBreak();

    void setBlocksCanBreak(Set<String> blocksCanBreak);

    /**
     * Checks if this item has the same data as another item.
     * This will check the item id, nbt, and meta, but NOT the count.
     * @param otherItem the other item we are trying to check
     * @return if the two stacks have the same data
     */
    boolean hasSameDataAs(Item otherItem);

    /**
     * Checks if an item looks the same compared to this item.
     * @param otherItem the other item
     * @return if they are visually the same
     */
    boolean visuallySameAs(Item otherItem);

    /**
     * Get the amount of ticks this item is worth as fuel.
     * @return amount of ticks or -1 if this item is not fuel.
     */
    int getFuelTicks();

    /**
     * Checks if this ItemStack is air or if there is no items in this stack.
     * @return if the ItemStack is air or if there is no items in this stack.
     */
    boolean isEmpty();

    boolean isAir();

    /**
     * Create a copy of this item but with a new network id assigned.
     * @return new item with a new network id assigned
     */
    Item newNetworkCopy();

    /**
     * Create a copy of this item but with a new network id assigned.
     * @param networkId network id to assign this stack
     * @return new item with a new network id assigned
     */
    Item newNetworkCopy(int networkId);

    ItemBehavior<Item> getBehavior();

    Item clone();

    /**
     * Ensures that the item provided will exist.
     * If the item provided is null, it will return an air stack
     * @param otherItem other item
     * @return item or air
     */
    static Item getAirIfNull(Item otherItem) {
        return otherItem == null ? new ItemBlock(BlockID.AIR, 0) : otherItem;
    }

}
