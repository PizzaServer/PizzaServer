package io.github.willqi.pizzaserver.nbt.tags;

import io.github.willqi.pizzaserver.nbt.exceptions.NBTLimitException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NBTCompound extends NBTTag {

    public static final byte ID = 10;

    private final Map<String, NBTTag> data = new HashMap<>();
    private int depth;


    public NBTCompound() {}

    public NBTCompound(String name) {
        super(name);
    }


    @Override
    public int getId() {
        return ID;
    }

    public NBTCompound getCompound(String name) {
        return (NBTCompound)this.data.get(name);
    }

    public NBTCompound put(String name, NBTTag tag) throws NBTLimitException {
        if (tag instanceof NBTCompound) {
            ((NBTCompound)tag).depth = this.depth + 1;
            if (((NBTCompound)tag).depth > 512) {
                throw new NBTLimitException("Reached maximum depth of 512 in NBTCompound.");
            }
        }
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
