package io.github.willqi.pizzaserver.server.item;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.Collection;
import java.util.HashSet;

public abstract class Item {

    private int damage;
    private int count;

    private NBTCompound tag;

    private Collection<ItemID> canBreak = new HashSet<>();


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

    public Collection<ItemID> getBlocksCanBreak() {
        return this.canBreak;
    }

    public void setBlocksCanBreak(Collection<ItemID> blocks) {
        this.canBreak = blocks;
    }

}
