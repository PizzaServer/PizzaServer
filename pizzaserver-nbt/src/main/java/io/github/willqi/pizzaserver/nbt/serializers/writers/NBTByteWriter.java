package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTByte;

import java.io.IOException;
import java.io.OutputStream;

public class NBTByteWriter extends NBTWriter<NBTByte> {

    public static final NBTByteWriter INSTANCE = new NBTByteWriter();


    private NBTByteWriter() {}

    @Override
    public void write(OutputStream stream, NBTByte tag) throws IOException {
        stream.write(tag.getByte());
    }

}
