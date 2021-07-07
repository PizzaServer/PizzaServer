package io.github.willqi.pizzaserver.nbt.tags;

import java.util.Arrays;
import java.util.Iterator;

public class NBTIntegerArray extends NBTTag implements Iterable<Integer> {

    public static final int ID = 11;

    private final int[] data;


    public NBTIntegerArray(int[] data) {
        this.data = data;
    }

    public NBTIntegerArray(String name, int[] data) {
        super(name);
        this.data = data;
    }

    public int[] getData() {
        return this.data;
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public Iterator<Integer> iterator() {
        return Arrays.stream(this.data).iterator();
    }

    @Override
    public int hashCode() {
        return 31 * Arrays.hashCode(this.data) * this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTIntegerArray) {
            NBTIntegerArray nbtIntegerArray = (NBTIntegerArray)obj;
            return Arrays.equals(nbtIntegerArray.getData(), this.getData()) && nbtIntegerArray.getName().equals(this.getName());
        }
        return false;
    }

}
