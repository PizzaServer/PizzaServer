package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTByteArray;

import java.io.IOException;
import java.io.OutputStream;

public class NBTByteArrayWriter extends NBTWriter<NBTByteArray> {

    public NBTByteArrayWriter(OutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTByteArray tag) throws IOException {
        this.stream.writeInt(tag.getData().length);
        for (byte b : tag.getData()) {
            this.stream.writeByte(b);
        }
    }

}
