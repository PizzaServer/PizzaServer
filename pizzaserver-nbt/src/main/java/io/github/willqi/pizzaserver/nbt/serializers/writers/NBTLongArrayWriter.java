package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTLong;
import io.github.willqi.pizzaserver.nbt.tags.NBTLongArray;

import java.io.IOException;

public class NBTLongArrayWriter extends NBTWriter<NBTLongArray> {

    private final NBTLongWriter longWriter;


    public NBTLongArrayWriter(LittleEndianDataOutputStream stream) {
        super(stream);
        this.longWriter = new NBTLongWriter(this.stream);
    }

    @Override
    protected void writeTagData(NBTLongArray tag) throws IOException {
        this.stream.writeInt(tag.getData().length);
        for (long l : tag.getData()) {
            this.longWriter.writeTagData(new NBTLong(l));
        }
    }

}
