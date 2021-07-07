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

    @Override
    public int hashCode() {
        return 31 * this.value.hashCode() * this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTString) {
            NBTString nbtString = (NBTString)obj;
            return nbtString.getValue().equals(this.getValue()) && nbtString.getName().equals(this.getName());
        }
        return false;
    }
}
