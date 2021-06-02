package io.github.willqi.pizzaserver.nbt.tags;

public class NBTLong extends NBTTag {

    public static final int ID = 4;

    private final long value;


    public NBTLong(long value) {
        this.value = value;
    }

    public NBTLong(String name, long value) {
        super(name);
        this.value = value;
    }

    public long getValue() {
        return this.value;
    }

    @Override
    public int getId() {
        return ID;
    }

}
