package io.github.willqi.pizzaserver.server.item;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public abstract class Item {

    private int damage;
    private int count;

    private NBTCompound tag;

    private ItemID[] canBreak = new ItemID[0];


    public abstract ItemID getId();

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public NBTCompound getTag() {
        return this.tag;
    }

    public void setTag(NBTCompound tag) {
        this.tag = tag;
    }

    public ItemID[] getBlocksCanBreak() {
        return this.canBreak;
    }

    public void setBlocksCanBreak(ItemID[] blocks) {
        this.canBreak = blocks;
    }

}
