package io.github.willqi.pizzaserver.nbt.tags;

import java.util.Arrays;

public class NBTByteArray extends NBTTag {

    public static final int ID = 7;

    private final byte[] data;


    public NBTByteArray(byte[] data) {
        this.data = data;
    }

    public NBTByteArray(String name, byte[] data) {
        super(name);
        this.data = data;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public int hashCode() {
        return 31 * Arrays.hashCode(this.data) * this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTByteArray) {
            NBTByteArray nbtByteArray = (NBTByteArray)obj;
            return Arrays.equals(nbtByteArray.getData(), this.getData()) && nbtByteArray.getName().equals(this.getName());
        }
        return false;
    }
}
