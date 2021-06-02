package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTByte;
import io.github.willqi.pizzaserver.nbt.tags.NBTByteArray;

import java.io.IOException;

public class NBTByteArrayWriter extends NBTWriter<NBTByteArray> {

    private final NBTByteWriter byteWriter;


    public NBTByteArrayWriter(LittleEndianDataOutputStream stream) {
        super(stream);
        this.byteWriter = new NBTByteWriter(this.stream);
    }

    @Override
    protected void writeTagData(NBTByteArray tag) throws IOException {
        this.stream.writeInt(tag.getData().length);
        for (byte b : tag.getData()) {
            this.byteWriter.writeTagData(new NBTByte(b));
        }
    }

}
