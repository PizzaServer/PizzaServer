package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTIntegerArrayWriter extends NBTWriter<int[]> {

    public static final NBTWriter<int[]> INSTANCE = new NBTIntegerArrayWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, int[] data) throws IOException {
        stream.writeInt(data.length);
        for (int integer : data) {
            NBTIntegerWriter.INSTANCE.write(stream, integer);
        }
    }
}
