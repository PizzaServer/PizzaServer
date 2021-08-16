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

    @Override
    public int hashCode() {
        return (31 * this.value) + (31 * this.name.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTInteger) {
            NBTInteger nbtInteger = (NBTInteger)obj;
            return nbtInteger.getValue() == this.getValue() && nbtInteger.getName().equals(this.getName());
        }
        return false;
    }
}
