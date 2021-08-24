package io.github.willqi.pizzaserver.api.item;

import io.github.willqi.pizzaserver.api.item.types.BaseItemType;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public class ItemStack {

    private final BaseItemType itemType;
    private int count;
    private int damage;
    private NBTCompound compound;


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
        this.itemType = itemType;
        this.count = count;
        this.damage = damage;
    }

    public BaseItemType getItemType() {
        return this.itemType;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public NBTCompound getCompoundTag() {
        return this.compound;
    }

    public void setCompoundTag(NBTCompound compound) {
        this.compound = compound;
    }

}