package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.IOException;

public class NBTStringWriter extends NBTWriter<String> {

    public static final NBTWriter<String> INSTANCE = new NBTStringWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, String data) throws IOException {
        stream.writeUTF(data);
    }

}
