package io.github.pizzaserver.api.item;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.nbt.NbtType;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.behavior.ItemBehavior;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.item.descriptors.ArmorItemComponent;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;

import java.util.*;

public abstract class Item implements Cloneable {

    private static int ID = 1;

    private final String itemId;

    private int networkId;
    private int meta;
    private int count;
    private NbtMap nbt = NbtMap.EMPTY;

    private Set<String> blocksCanBreak = Collections.emptySet();


    public Item(String itemId, int count) {
        this(itemId, count, 0);
    }

    public Item(String itemId, int count, int meta) {
        if (itemId.equals(BlockID.AIR)) {
            this.networkId = 0;
        } else {
            this.networkId = ID++;
        }
        this.itemId = itemId;
        this.count = count;
        this.meta = meta;
    }

    public String getItemId() {
        return this.itemId;
    }

    public abstract String getName();

    /**
     * Returns the id of this stack as represented over the network.
     * An id of -1 means that this stack has not been assigned a network id
     *
     * @return stack id
     */
    public int getNetworkId() {
        return this.networkId;
    }

    public int getCount() {
        if (this.isAir()) {
            return 0;
        }

        return this.count;
    }

    public void setCount(int count) {
        this.count = Math.max(count, 0);
    }

    public int getMaxStackSize() {
        return 64;
    }

    /**
     * Returns if this item is separated by its meta.
     * (e.g. durability)
     *
     * @return if this item should be stacked by meta
     */
    public boolean isStackedByMeta() {
        return false;
    }

    public int getMeta() {
        return this.meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }

    /**
     * Return the amount of damage this item does to an entity.
     *
     * @return damage
     */
    public int getDamage() {
        return 1;
    }

    public Optional<String> getCustomName() {
        String customName = this.getNBT().getCompound("display").getString("Name");
        if (customName.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(customName);
    }

    public void setCustomName(String customName) {
        NbtMap displayNBT;
        if (customName != null) {
            displayNBT = this.getNBT().getCompound("display").toBuilder().putString("Name", customName).build();
        } else {
            NbtMapBuilder displayNBTBuilder = this.getNBT().getCompound("display").toBuilder();
            displayNBTBuilder.remove("Name");
            displayNBT = displayNBTBuilder.build();
        }
        this.setNBT(this.getNBT().toBuilder().putCompound("display", displayNBT).build());
    }

    public List<String> getLore() {
        return this.getNBT().getCompound("display").getList("Lore", NbtType.STRING);
    }

    public void setLore(List<String> lore) {
        NbtMap displayNBT = this.getNBT()
                                .getCompound("display")
                                .toBuilder()
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

    public boolean isAllowedInOffHand() {
        return false;
    }

    public boolean canUseOnLiquid() {
        return false;
    }

    public Set<String> getBlocksCanBreak() {
        return Collections.unmodifiableSet(this.blocksCanBreak);
    }

    public void setBlocksCanBreak(Set<String> blocksCanBreak) {
        this.blocksCanBreak = new HashSet<>(blocksCanBreak);
    }

    /**
     * Checks if this item has the same data as another item.
     * This will check the item id, nbt, and meta, but NOT the count.
     *
     * @param otherItem the other item we are trying to check
     * @return if the two stacks have the same data
     */
    public boolean hasSameDataAs(Item otherItem) {
        return (otherItem.getItemId().equals(this.getItemId()) && otherItem.getNBT().equals(this.getNBT())
                && otherItem.getMeta() == this.getMeta());
    }

    /**
     * Checks if an item looks the same compared to this item.
     *
     * @param otherItem the other item
     * @return if they are visually the same
     */
    public boolean visuallySameAs(Item otherItem) {
        return otherItem.getNBT().equals(this.getNBT()) && (otherItem.getMeta() == this.getMeta()
                || (otherItem instanceof DurableItemComponent)) && otherItem.getItemId().equals(this.getItemId());
    }

    /**
     * Checks if this ItemStack is air or if there is no items in this stack.
     *
     * @return if the ItemStack is air or if there is no items in this stack.
     */
    public boolean isEmpty() {
        return this.isAir() || this.getCount() == 0;
    }

    public boolean isAir() {
        return this.getItemId().equals(BlockID.AIR);
    }

    /**
     * Create a copy of this item but with a new network id assigned.
     *
     * @return new item with a new network id assigned
     */
    public Item newNetworkCopy() {
        if (this.getItemId().equals(BlockID.AIR)) {
            return this.newNetworkCopy(0);
        }

        return this.newNetworkCopy(ID++);
    }

    /**
     * Create a copy of this item but with a new network id assigned.
     *
     * @param networkId network id to assign this stack
     * @return new item with a new network id assigned
     */
    public Item newNetworkCopy(int networkId) {
        Item clone = this.clone();
        clone.networkId = networkId;

        return clone;
    }

    @SuppressWarnings("unchecked")
    public ItemBehavior<Item> getBehavior() {
        return (ItemBehavior<Item>) ItemRegistry.getInstance().getItemBehavior(this);
    }

    @Override
    public Item clone() {
        try {
            Item item = (Item) super.clone();
            item.setNBT(this.getNBT().toBuilder().build());
            return item;
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError("Clone threw an exception", exception);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item otherItem) {
            return otherItem.getItemId().equals(this.getItemId()) && otherItem.getMeta() == this.getMeta()
                    && otherItem.getCount() == this.getCount() && otherItem.getNBT().equals(this.getNBT());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.getItemId().hashCode() * 73) + (this.getMeta() * 73) + (this.getCount() * 73) + (this.getNBT()
                                                                                                          .hashCode());
    }

    /**
     * Ensures that the item provided will exist.
     * If the item provided is null, it will return an air stack
     *
     * @param otherItem other item
     * @return item or air
     */
    public static Item getAirIfNull(Item otherItem) {
        return otherItem == null ? new ItemBlock(BlockID.AIR, 0) : otherItem;
    }

    public static boolean canBePlacedInSlot(Item item, ContainerSlotType containerSlotType, int slot) {
        return switch (containerSlotType) {
            case ARMOR -> item instanceof ArmorItemComponent && slot == ((ArmorItemComponent) item).getArmorSlot()
                                                                                                   .ordinal();
            case OFFHAND -> item.isAllowedInOffHand();
            default -> true;
        };
    }
}
