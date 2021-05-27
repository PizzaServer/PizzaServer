package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTFloat;

import java.io.IOException;
import java.io.OutputStream;

public class NBTFloatWriter extends NBTWriter<NBTFloat> {

    public static final NBTFloatWriter INSTANCE = new NBTFloatWriter();


    private NBTFloatWriter() {}

    @Override
    public void write(OutputStream stream, NBTFloat tag) throws IOException {
        super.write(stream, tag);
        StreamUtility.putFloat(tag.getValue(), stream);
    }

}
