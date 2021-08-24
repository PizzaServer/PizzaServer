package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTDoubleWriter extends NBTWriter<Double> {

    public static final NBTWriter<Double> INSTANCE = new NBTDoubleWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, Double data) throws IOException {
        stream.writeDouble(data);
    }

}
