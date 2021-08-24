package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTByteWriter extends NBTWriter<Byte> {

    public static final NBTWriter<Byte> INSTANCE = new NBTByteWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, Byte data) throws IOException {
        stream.writeByte(data);
    }

}
