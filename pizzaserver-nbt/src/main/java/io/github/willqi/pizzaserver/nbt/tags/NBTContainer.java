package io.github.willqi.pizzaserver.nbt.tags;

/**
 * An NBT tag that has the ability to have child tags.
 * These tag types have a inherent restriction of only being allowed to go 512 children deep.
 */
public abstract class NBTContainer {

    protected String name;


    public NBTContainer(String name) {
        this.name = name;
    }

    public NBTContainer() {
        this.name = "";
    }

    abstract int getDepth();

    abstract void setDepth(int depth);

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
