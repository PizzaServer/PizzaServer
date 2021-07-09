package io.github.willqi.pizzaserver.nbt.tags;

public class NBTByte extends NBTTag {

    public static final int ID = 1;

    private final byte b;


    public NBTByte(byte b) {
        this.b = b;
    }

    public NBTByte(String name, byte b) {
        super(name);
        this.b = b;
    }

    public byte getValue() {
        return this.b;
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public int hashCode() {
        return 31 * this.b * this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTByte) {
            NBTByte nbtByte = (NBTByte)obj;
            return nbtByte.getValue() == this.getValue() && nbtByte.getName().equals(this.getName());
        }
        return false;
    }
}
