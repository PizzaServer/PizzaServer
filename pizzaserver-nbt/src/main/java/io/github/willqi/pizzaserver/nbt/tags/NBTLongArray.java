package io.github.willqi.pizzaserver.nbt.tags;

public class NBTLongArray extends NBTTag {

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
}
