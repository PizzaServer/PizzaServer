package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTString;

import java.io.IOException;
import java.io.OutputStream;

public class NBTStringWriter extends NBTWriter<NBTString> {

    public NBTStringWriter(OutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTString tag) throws IOException {
        this.stream.writeUTF(tag.getValue());
    }

}
