package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTDouble;

import java.io.IOException;
import java.io.OutputStream;

public class NBTDoubleWriter extends NBTWriter<NBTDouble> {

    public NBTDoubleWriter(OutputStream stream) {
        super(stream);
    }


    @Override
    protected void writeTagData(NBTDouble tag) throws IOException {
        this.stream.writeDouble(tag.getValue());
    }

}
