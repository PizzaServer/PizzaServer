package io.github.willqi.pizzaserver.nbt.tags;

public class NBTByteArray extends NBTTag implements NBTContainer {

    public static final int ID = 7;

    private final byte[] data;
    private int depth;


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
    public int getDepth() {
        return this.depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
    }

}
