package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTLong;

import java.io.IOException;
import java.io.OutputStream;

public class NBTLongWriter extends NBTWriter<NBTLong> {

    public NBTLongWriter(OutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTLong tag) throws IOException {
        this.stream.writeLong(tag.getValue());
    }

}
