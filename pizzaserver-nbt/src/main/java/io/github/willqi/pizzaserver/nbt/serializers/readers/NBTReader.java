package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public abstract class NBTReader<T> {

    protected NBTReader() {}

    public abstract T read(LittleEndianDataInputStream stream) throws IOException;

}
