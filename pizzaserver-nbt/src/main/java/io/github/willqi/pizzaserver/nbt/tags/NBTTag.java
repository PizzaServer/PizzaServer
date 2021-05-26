package io.github.willqi.pizzaserver.nbt.tags;

public abstract class NBTTag {

    public static final int END_ID = 0;

    protected final String name;


    public NBTTag(String name) {
        this.name = name;
    }

    public NBTTag() {
        this.name = "";
    }


    public abstract int getId();

    public String getName() {
        return this.name;
    }

}
