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

}
