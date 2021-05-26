package io.github.willqi.pizzaserver.nbt.tags;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NBTCompound extends NBTTag {

    public static final byte ID = 10;

    private final Map<String, NBTTag> data = new HashMap<>();


    public NBTCompound(String name) {
        super(name);
    }

    public NBTCompound() {}


    @Override
    public int getId() {
        return ID;
    }

    public NBTCompound getCompound(String name) {
        return (NBTCompound)this.data.get(name);
    }

    public NBTCompound put(String name, NBTTag tag) {
        this.data.put(name, tag);
        return this;
    }

    public NBTTag get(String name) {
        return this.data.get(name);
    }

    public Set<String> keySet() {
        return this.data.keySet();
    }



}
