package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTTag;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class NBTWriter<T extends NBTTag> {

    protected final DataOutputStream stream;

    public NBTWriter(OutputStream stream) {
        this.stream = new DataOutputStream(stream);
    }

    public void write(T tag) throws IOException {
        this.stream.write(tag.getId());
        this.stream.writeUTF(tag.getName());
        this.writeTagData(tag);
    }

    protected abstract void writeTagData(T tag) throws IOException;

}
