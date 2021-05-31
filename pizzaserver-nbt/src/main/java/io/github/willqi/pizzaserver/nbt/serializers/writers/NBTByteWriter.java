package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTByte;

import java.io.IOException;
import java.io.OutputStream;

public class NBTByteWriter extends NBTWriter<NBTByte> {

    public NBTByteWriter(OutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTByte tag) throws IOException {
        this.stream.write(tag.getId());
    }

}
