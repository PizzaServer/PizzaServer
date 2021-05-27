package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTLong;

import java.io.IOException;
import java.io.OutputStream;

public class NBTLongWriter extends NBTWriter<NBTLong> {

    public static final NBTLongWriter INSTANCE = new NBTLongWriter();


    private NBTLongWriter() {}

    @Override
    public void write(OutputStream stream, NBTLong tag) throws IOException {
        super.write(stream, tag);
        StreamUtility.putLong(tag.getValue(), stream);
    }

}
