package io.github.willqi.pizzaserver.nbt.tags;

import io.github.willqi.pizzaserver.nbt.exceptions.NBTLimitException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NBTCompound extends NBTTag implements NBTContainer {

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
        if (tag instanceof NBTContainer) {
            NBTContainer container = (NBTContainer)tag;
            container.setDepth(this.getDepth() + 1);
            if (container.getDepth() > 512) {
                throw new NBTLimitException("Reached maximum depth of 512.");
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

    @Override
    public int getDepth() {
        return this.depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
    }

}
