package io.github.pizzaserver.api.item;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.behavior.ItemBehavior;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;

import java.util.*;

public abstract class BaseItem implements Item {

    private static int ID = 1;

    private final String itemId;

    private int networkId;
    private int meta;
    private int count;
    private NbtMap nbt = NbtMap.EMPTY;

    private Set<String> blocksCanBreak = Collections.emptySet();


    public BaseItem(String itemId, int count) {
        this(itemId, count, 0);
    }

    public BaseItem(String itemId, int count, int meta) {
        if (itemId.equals(BlockID.AIR)) {
            this.networkId = 0;
        } else {
            this.networkId = ID++;
        }
        this.itemId = itemId;
        this.count = count;
        this.meta = meta;
    }

    @Override
    public String getItemId() {
        return this.itemId;
    }

    @Override
    public int getNetworkId() {
        return this.networkId;
    }

    @Override
    public int getCount() {
        if (this.isAir()) {
            return 0;
        }

        return this.count;
    }

    @Override
    public void setCount(int count) {
        this.count = Math.max(count, 0);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean isStackedByMeta() {
        return false;
    }

    @Override
    public int getMeta() {
        return this.meta;
    }

    @Override
    public void setMeta(int meta) {
        this.meta = meta;
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public Optional<String> getCustomName() {
        String customName = this.getNBT().getCompound("display").getString("Name");
        if (customName.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(customName);
    }

    @Override
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

    @Override
    public List<String> getLore() {
        return this.getNBT().getCompound("display").getList("Lore", NbtType.STRING);
    }

    @Override
    public void setLore(List<String> lore) {
        NbtMap displayNBT = this.getNBT().getCompound("display").toBuilder()
                .putList("Lore", NbtType.STRING, lore)
                .build();
        this.setNBT(this.getNBT().toBuilder().putCompound("display", displayNBT).build());
    }

    @Override
    public NbtMap getNBT() {
        return this.nbt;
    }

    @Override
    public void setNBT(NbtMap nbt) {
        this.nbt = nbt;
    }

    @Override
    public boolean isAllowedInOffHand() {
        return false;
    }

    @Override
    public boolean canUseOnLiquid() {
        return false;
    }

    @Override
    public Set<String> getBlocksCanBreak() {
        return Collections.unmodifiableSet(this.blocksCanBreak);
    }

    @Override
    public void setBlocksCanBreak(Set<String> blocksCanBreak) {
        this.blocksCanBreak = new HashSet<>(blocksCanBreak);
    }

    @Override
    public boolean hasSameDataAs(Item otherItem) {
        return (otherItem.getItemId().equals(this.getItemId())
                && otherItem.getNBT().equals(this.getNBT())
                && otherItem.getMeta() == this.getMeta());
    }

    @Override
    public boolean visuallySameAs(Item otherItem) {
        return otherItem.getNBT().equals(this.getNBT())
                && (otherItem.getMeta() == this.getMeta() || (otherItem instanceof DurableItem))
                && otherItem.getItemId().equals(this.getItemId());
    }

    @Override
    public int getFuelTicks() {
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return this.isAir() || this.getCount() == 0;
    }

    @Override
    public boolean isAir() {
        return this.getItemId().equals(BlockID.AIR);
    }

    @Override
    public Item newNetworkCopy() {
        if (this.getItemId().equals(BlockID.AIR)) {
            return this.newNetworkCopy(0);
        }

        return this.newNetworkCopy(ID++);
    }

    @Override
    public Item newNetworkCopy(int networkId) {
        Item clone = this.clone();
        ((BaseItem) clone).networkId = networkId;

        return clone;
    }

    @Override
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
            return otherItem.getItemId().equals(this.getItemId())
                    && otherItem.getMeta() == this.getMeta()
                    && otherItem.getCount() == this.getCount()
                    && otherItem.getNBT().equals(this.getNBT());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.getItemId().hashCode() * 73)
                + (this.getMeta() * 73)
                + (this.getCount() * 73)
                + (this.getNBT().hashCode());
    }

}
