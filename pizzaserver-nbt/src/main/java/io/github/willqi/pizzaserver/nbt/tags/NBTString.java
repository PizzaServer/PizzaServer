package io.github.willqi.pizzaserver.nbt.tags;

public class NBTString extends NBTTag {

    public static final int ID = 8;

    private final String value;


    public NBTString(String value) {
        this.value = value;
    }

    public NBTString(String name, String value) {
        super(name);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int getId() {
        return ID;
    }

}
