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

    @Override
    public int hashCode() {
        return (31 * (int)this.value) + (31 * this.name.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTFloat) {
            NBTFloat nbtFloat = (NBTFloat)obj;
            return nbtFloat.getValue() == this.getValue() && nbtFloat.getName().equals(this.getName());
        }
        return false;
    }
}
