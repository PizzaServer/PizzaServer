package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTShortWriter extends NBTWriter<Short> {

    public static final NBTWriter<Short> INSTANCE = new NBTShortWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, Short data) throws IOException {
        stream.writeShort(data);
    }

}
