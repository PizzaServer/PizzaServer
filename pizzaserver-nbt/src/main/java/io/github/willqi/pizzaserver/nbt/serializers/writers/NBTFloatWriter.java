package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTFloatWriter extends NBTWriter<Float> {

    public static final NBTWriter<Float> INSTANCE = new NBTFloatWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, Float data) throws IOException {
        stream.writeFloat(data);
    }

}
