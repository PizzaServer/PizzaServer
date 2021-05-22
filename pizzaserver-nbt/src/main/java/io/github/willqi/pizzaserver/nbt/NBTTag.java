package io.github.willqi.pizzaserver.nbt;

public abstract class NBTTag {

    protected final String name;

    public NBTTag(String name) {
        this.name = name;
    }

    public NBTTag() {
        this.name = null;
    }

    public String getName() {
        return this.name;
    }

}
