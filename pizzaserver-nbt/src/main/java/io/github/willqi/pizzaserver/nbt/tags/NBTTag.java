package io.github.willqi.pizzaserver.nbt.tags;

public abstract class NBTTag {

    protected String name;


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

    public void setName(String name) {
        this.name = name;
    }

}
