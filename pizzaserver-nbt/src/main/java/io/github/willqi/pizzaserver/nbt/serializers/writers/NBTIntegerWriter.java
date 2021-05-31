package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;

import java.io.IOException;
import java.io.OutputStream;

public class NBTIntegerWriter extends NBTWriter<NBTInteger> {

    public NBTIntegerWriter(OutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTInteger tag) throws IOException {
        this.stream.writeInt(tag.getValue());
    }

}
