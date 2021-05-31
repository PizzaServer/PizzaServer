package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTFloat;

import java.io.IOException;
import java.io.OutputStream;

public class NBTFloatWriter extends NBTWriter<NBTFloat> {

    public NBTFloatWriter(OutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTFloat tag) throws IOException {
        this.stream.writeFloat(tag.getValue());
    }

}
