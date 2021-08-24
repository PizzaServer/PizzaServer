package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public abstract class NBTWriter<T> {

    protected NBTWriter() {}

    public abstract void write(LittleEndianDataOutputStream stream, T value) throws IOException;

}
