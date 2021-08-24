package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTIntegerWriter extends NBTWriter<Integer> {

    public static final NBTWriter<Integer> INSTANCE = new NBTIntegerWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, Integer data) throws IOException {
        stream.writeInt(data);
    }

}
