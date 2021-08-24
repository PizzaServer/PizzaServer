package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTByteArrayWriter extends NBTWriter<byte[]> {

    public static final NBTWriter<byte[]> INSTANCE = new NBTByteArrayWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, byte[] data) throws IOException {
        stream.writeInt(data.length);
        for (byte b : data) {
            NBTByteWriter.INSTANCE.write(stream, b);
        }
    }

}
