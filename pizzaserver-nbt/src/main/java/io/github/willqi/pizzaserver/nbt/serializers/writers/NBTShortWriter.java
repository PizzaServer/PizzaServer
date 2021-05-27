package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTShort;

import java.io.IOException;
import java.io.OutputStream;

public class NBTShortWriter extends NBTWriter<NBTShort> {

    public static final NBTShortWriter INSTANCE = new NBTShortWriter();


    private NBTShortWriter() {}

    @Override
    public void write(OutputStream stream, NBTShort tag) throws IOException {
        super.write(stream, tag);
        StreamUtility.putShort(tag.getValue(), stream);
    }

}
