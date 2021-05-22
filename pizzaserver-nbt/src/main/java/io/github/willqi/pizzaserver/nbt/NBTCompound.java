package io.github.willqi.pizzaserver.nbt;

import java.util.HashMap;
import java.util.Map;

public class NBTCompound extends NBTTag {

    public static final byte ID = 10;

    private final Map<String, NBTTag> data = new HashMap<>();


    public NBTCompound(String name) {
        super(name);
    }

}
