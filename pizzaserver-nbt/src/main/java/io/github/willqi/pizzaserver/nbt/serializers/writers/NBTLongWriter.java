package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTLongWriter extends NBTWriter<Long> {

    public static final NBTWriter<Long> INSTANCE = new NBTLongWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, Long data) throws IOException {
        stream.writeLong(data);
    }

}
