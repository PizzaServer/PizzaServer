package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTTag;

import java.io.IOException;
import java.io.InputStream;

public abstract class NBTReader<T extends NBTTag> {

    public abstract T read(InputStream stream) throws IOException;

}
