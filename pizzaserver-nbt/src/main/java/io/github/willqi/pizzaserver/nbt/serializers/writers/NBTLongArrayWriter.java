package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTLongArrayWriter extends NBTWriter<long[]> {

    public static final NBTWriter<long[]> INSTANCE = new NBTLongArrayWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, long[] data) throws IOException {
        stream.writeInt(data.length);
        for (long l : data) {
            NBTLongWriter.INSTANCE.write(stream, l);
        }
    }

}
