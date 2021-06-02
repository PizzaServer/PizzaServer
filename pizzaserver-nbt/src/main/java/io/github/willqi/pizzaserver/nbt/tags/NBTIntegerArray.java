package io.github.willqi.pizzaserver.nbt.tags;

public class NBTIntegerArray extends NBTTag {

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
}
