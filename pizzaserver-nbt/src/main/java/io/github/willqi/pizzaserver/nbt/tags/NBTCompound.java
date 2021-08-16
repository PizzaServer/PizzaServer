package io.github.willqi.pizzaserver.nbt.tags;

import io.github.willqi.pizzaserver.nbt.exceptions.NBTLimitException;

import java.util.*;

public class NBTCompound extends NBTTag implements NBTContainer, Iterable<String> {

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

    public byte getByte(String name) {
        return ((NBTByte)this.data.get(name)).getValue();
    }

    public NBTCompound setByte(String name, byte value) {
        this.put(name, new NBTByte(value));
        return this;
    }

    public short getShort(String name) {
        return ((NBTShort)this.data.get(name)).getValue();
    }

    public NBTCompound setShort(String name, short value) {
        this.put(name, new NBTShort(value));
        return this;
    }

    public int getInteger(String name) {
        return ((NBTInteger)this.data.get(name)).getValue();
    }

    public NBTCompound setInteger(String name, int value) {
        this.put(name, new NBTInteger(value));
        return this;
    }

    public long getLong(String name) {
        return ((NBTLong)this.data.get(name)).getValue();
    }

    public NBTCompound setLong(String name, long value) {
        this.put(name, new NBTLong(value));
        return this;
    }

    public float getFloat(String name) {
        return ((NBTFloat)this.data.get(name)).getValue();
    }

    public NBTCompound setFloat(String name, float value) {
        this.put(name, new NBTFloat(value));
        return this;
    }

    public double getDouble(String name) {
        return ((NBTDouble)this.data.get(name)).getValue();
    }

    public NBTCompound setDouble(String name, double value) {
        this.put(name, new NBTDouble(value));
        return this;
    }

    public String getString(String name) {
        return ((NBTString)this.data.get(name)).getValue();
    }

    public NBTCompound setString(String name, String value) {
        this.put(name, new NBTString(value));
        return this;
    }

    public NBTTag[] getList(String name) {
        return ((NBTList<?>)this.data.get(name)).getContents();
    }

    public NBTCompound setList(String name, NBTList<?> list) {
        this.put(name, list);
        return this;
    }

    public NBTCompound getCompound(String name) {
        return (NBTCompound)this.data.get(name);
    }

    public NBTCompound setCompound(String name, NBTCompound compound) {
        this.put(name, compound);
        return this;
    }

    public byte[] getByteArray(String name) {
        return ((NBTByteArray)this.data.get(name)).getData();
    }

    public NBTCompound setByteArray(String name, byte[] value) {
        this.put(name, new NBTByteArray(value));
        return this;
    }

    public int[] getIntegerArray(String name) {
        return ((NBTIntegerArray)this.data.get(name)).getData();
    }

    public NBTCompound setIntegerArray(String name, int[] value) {
        this.put(name, new NBTIntegerArray(value));
        return this;
    }

    public long[] getLongArray(String name) {
        return ((NBTLongArray)this.data.get(name)).getData();
    }

    public NBTCompound setLongArray(String name, long[] value) {
        this.put(name, new NBTLongArray(value));
        return this;
    }

    public NBTTag get(String name) {
        return this.data.get(name);
    }

    public NBTCompound put(String name, NBTTag tag) throws NBTLimitException {
        tag.setName(name);
        this.data.put(name, tag);
        if (tag instanceof NBTContainer) {
            ((NBTContainer)tag).setDepth(this.getDepth() + 1);
        }
        return this;
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
        if (this.depth > 512) {
            throw new NBTLimitException("Reached maximum depth of 512.");
        }
        this.data.forEach((name, tag) -> {
            if (tag instanceof NBTContainer) {
                ((NBTContainer)tag).setDepth(this.getDepth() + 1);
            }
        });
    }

    public int size() {
        return this.data.size();
    }

    @Override
    public Iterator<String> iterator() {
        return this.data.keySet().iterator();
    }

    @Override
    public int hashCode() {
        return (31 * this.name.hashCode()) + (31 * this.data.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTCompound) {
            NBTCompound nbtCompound = (NBTCompound)obj;
            if ((nbtCompound.size() != this.size()) || !(nbtCompound.getName().equals(this.getName()))) {
                return false;
            }

            for (String key : nbtCompound) {
                if (!this.data.containsKey(key) || !this.get(key).equals(nbtCompound.get(key))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
