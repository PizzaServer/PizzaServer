package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTDouble;

import java.io.IOException;
import java.io.OutputStream;

public class NBTDoubleWriter extends NBTWriter<NBTDouble> {

    public static final NBTDoubleWriter INSTANCE = new NBTDoubleWriter();


    private NBTDoubleWriter() {}

    @Override
    public void write(OutputStream stream, NBTDouble tag) throws IOException {
        super.write(stream, tag);
        StreamUtility.putDouble(tag.getValue(), stream);
    }

}
