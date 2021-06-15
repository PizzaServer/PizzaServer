package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTTag;

import java.io.IOException;

public abstract class NBTWriter<T extends NBTTag> {

    protected final LittleEndianDataOutputStream stream;

    public NBTWriter(LittleEndianDataOutputStream stream) {
        this.stream = stream;
    }

    public void write(T tag) throws IOException {
        this.stream.writeByte(tag.getId());
        this.stream.writeUTF(tag.getName());
        this.writeTagData(tag);
    }

    protected abstract void writeTagData(T tag) throws IOException;

}
