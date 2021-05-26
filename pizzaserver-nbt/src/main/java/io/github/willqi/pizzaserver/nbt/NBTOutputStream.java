package io.github.willqi.pizzaserver.nbt;

import io.github.willqi.pizzaserver.nbt.serializers.writers.NBTByteWriter;
import io.github.willqi.pizzaserver.nbt.serializers.writers.NBTCompoundWriter;
import io.github.willqi.pizzaserver.nbt.tags.NBTByte;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NBTOutputStream extends OutputStream {

    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
    }

    public void writeCompound(NBTCompound compound) throws IOException {
        NBTCompoundWriter.INSTANCE.write(this.stream, compound);
    }

    public void writeByte(NBTByte nbtByte) throws IOException {
        NBTByteWriter.INSTANCE.write(this.stream, nbtByte);
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

}
