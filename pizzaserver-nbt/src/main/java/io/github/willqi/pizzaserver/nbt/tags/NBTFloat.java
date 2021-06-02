package io.github.willqi.pizzaserver.nbt.tags;

public class NBTFloat extends NBTTag {

    public static final int ID = 5;

    private final float value;


    public NBTFloat(float value) {
        this.value = value;
    }

    public NBTFloat(String name, float value) {
        super(name);
        this.value = value;
    }

    public float getValue() {
        return this.value;
    }

    @Override
    public int getId() {
        return ID;
    }

}
