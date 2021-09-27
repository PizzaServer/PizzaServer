package io.github.willqi.pizzaserver.nbt.tags;

import io.github.willqi.pizzaserver.nbt.exceptions.NBTLimitException;

import java.util.Arrays;
import java.util.Iterator;

public class NBTList<T> extends NBTContainer implements Iterable<T>, Cloneable {

    private T[] list = (T[]) new Object[0];
    private final int childrenTypeId;
    private int depth;


    public NBTList(int childrenTypeId) {
        this.childrenTypeId = childrenTypeId;
    }

    public NBTList(String name, int childrenTypeId) {
        super(name);
        this.childrenTypeId = childrenTypeId;
    }

    public void setContents(T[] list) {
        this.list = list;
        for (Object tag : list) {
            if (tag instanceof NBTContainer) {
                ((NBTContainer) tag).setDepth(this.getDepth() + 1);
            }
        }
    }

    public T[] getContents() {
        return this.list;
    }

    public int getChildrenTypeId() {
        return this.childrenTypeId;
    }

    @Override
    public int getDepth() {
        return this.depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
        if (this.depth > NBTContainer.MAX_DEPTH) {
            throw new NBTLimitException("Reached maximum depth of " + NBTContainer.MAX_DEPTH);
        }
        for (Object childTag : this.list) {
            if (childTag instanceof NBTContainer) {
                ((NBTContainer) childTag).setDepth(this.getDepth() + 1);
            }
        }
    }

    @Override
    public int hashCode() {
        return (31 * Arrays.hashCode(this.list)) + (31 * this.getName().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTList) {
            NBTList<T> nbtList = (NBTList<T>) obj;
            return Arrays.equals(nbtList.getContents(), this.getContents())
                    && nbtList.getName().equals(this.getName())
                    && nbtList.getChildrenTypeId() == this.getChildrenTypeId();
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.stream(this.list).iterator();
    }

    @Override
    public NBTList<T> clone() {
        // Clone elements in array
        Object[] elements = new Object[this.getContents().length];
        for (int i = 0; i < elements.length; i++) {
            Object element = this.getContents()[i];
            if (element instanceof NBTCompound) {
                elements[i] = ((NBTCompound) element).clone();
            } else if (element instanceof NBTList) {
                elements[i] = ((NBTList<T>) element).clone();
            } else {
                elements[i] = element;
            }
        }

        NBTList<T> clone = new NBTList<>(this.childrenTypeId);
        clone.setDepth(this.getDepth());
        clone.setContents((T[]) elements);
        return clone;
    }
}
