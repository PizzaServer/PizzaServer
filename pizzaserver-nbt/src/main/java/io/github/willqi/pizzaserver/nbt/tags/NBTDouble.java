package io.github.willqi.pizzaserver.nbt.tags;

public class NBTDouble extends NBTTag {

    public static final int ID = 6;

    private final double value;


    public NBTDouble(double value) {
        this.value = value;
    }

    public NBTDouble(String name, double value) {
        super(name);
        this.value = value;
    }

    public double getValue() {
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
        if (obj instanceof NBTDouble) {
            NBTDouble nbtDouble = (NBTDouble)obj;
            return nbtDouble.getValue() == this.getValue() && nbtDouble.getName().equals(this.getName());
        }
        return false;
    }
}
