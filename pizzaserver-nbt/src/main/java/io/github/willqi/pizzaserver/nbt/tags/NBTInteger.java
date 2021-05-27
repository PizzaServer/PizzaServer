package io.github.willqi.pizzaserver.nbt.tags;

public class NBTInteger extends NBTTag {

    public static final int ID = 3;

    private final int value;


    public NBTInteger(int value) {
        this.value = value;
    }

    public NBTInteger(String name, int value) {
        super(name);
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public int getId() {
        return ID;
    }

}
