package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;

import java.io.IOException;
import java.io.OutputStream;

public class NBTIntegerWriter extends NBTWriter<NBTInteger> {

    public static final NBTIntegerWriter INSTANCE = new NBTIntegerWriter();


    private NBTIntegerWriter() {}

    @Override
    public void write(OutputStream stream, NBTInteger tag) throws IOException {
        super.write(stream, tag);
        StreamUtility.putSignedInt(tag.getValue(), stream);
    }

}
