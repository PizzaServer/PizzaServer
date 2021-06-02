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


}
