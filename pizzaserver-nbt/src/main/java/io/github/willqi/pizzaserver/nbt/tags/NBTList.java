package io.github.willqi.pizzaserver.nbt.tags;

import io.github.willqi.pizzaserver.nbt.exceptions.NBTLimitException;

public class NBTList<T extends NBTTag> extends NBTTag implements NBTContainer {

    public static final int ID = 9;

    private T[] list = (T[])new NBTTag[0];
    private int depth;


    public NBTList() {

    }

    public NBTList(String name) {
        super(name);
    }

    public void setContents(T[] list) {
        this.list = list;
        for (T tag : list) {
            if (tag instanceof NBTContainer) {
                ((NBTContainer)tag).setDepth(this.getDepth() + 1);
            }
        }
    }

    public T[] getContents() {
        return this.list;
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public int getDepth() {
        return this.depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
        if (this.depth > 512) {
            throw new NBTLimitException("Reached maximum depth of 512.");
        }
        for (T childTag : this.list) {
            if (childTag instanceof NBTContainer) {
                ((NBTContainer)childTag).setDepth(this.getDepth() + 1);
            }
        }
    }
}
