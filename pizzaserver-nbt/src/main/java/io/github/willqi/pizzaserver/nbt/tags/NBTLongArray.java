package io.github.willqi.pizzaserver.nbt.tags;

import java.util.Arrays;
import java.util.Iterator;

public class NBTLongArray extends NBTTag implements Iterable<Long> {

    public static final int ID = 12;

    private final long[] data;


    public NBTLongArray(long[] data) {
        this.data = data;
    }

    public NBTLongArray(String name, long[] data) {
        super(name);
        this.data = data;
    }

    public long[] getData() {
        return this.data;
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public Iterator<Long> iterator() {
        return Arrays.stream(this.data).iterator();
    }

    @Override
    public int hashCode() {
        return (31 * Arrays.hashCode(this.data)) + (31 * this.name.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTLongArray) {
            NBTLongArray nbtLongArray = (NBTLongArray)obj;
            return Arrays.equals(nbtLongArray.getData(), this.getData()) && nbtLongArray.getName().equals(this.getName());
        }
        return false;
    }
}
