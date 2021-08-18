package io.github.willqi.pizzaserver.nbt.tags;

public class NBTShort extends NBTTag {

    public static final int ID = 2;

    private final short value;


    public NBTShort(short value) {
        this.value = value;
    }

    public NBTShort(String name, short value) {
        super(name);
        this.value = value;
    }

    public short getValue() {
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
        if (obj instanceof NBTShort) {
            NBTShort nbtShort = (NBTShort)obj;
            return nbtShort.getValue() == this.getValue() && nbtShort.getName().equals(this.getName());
        }
        return false;
    }
}
