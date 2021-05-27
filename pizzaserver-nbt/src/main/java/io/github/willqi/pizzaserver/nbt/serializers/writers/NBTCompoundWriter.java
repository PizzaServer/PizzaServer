package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;
import java.io.OutputStream;

public class NBTCompoundWriter extends NBTWriter<NBTCompound> {

    public static final NBTCompoundWriter INSTANCE = new NBTCompoundWriter();


    private NBTCompoundWriter() {}

    @Override
    public void write(OutputStream stream, NBTCompound tag) throws IOException {
        super.write(stream, tag);
        writeContents(stream, tag);
        stream.write(NBTTag.END_ID);
    }

    private static void writeContents(OutputStream stream, NBTCompound tag) throws IOException {
        for (String name : tag.keySet()) {

            NBTTag childTag = tag.get(name);
            switch (childTag.getId()) {
                case NBTByte.ID:
                    NBTByteWriter.INSTANCE.write(stream, (NBTByte)childTag);
                    break;
                case NBTShort.ID:
                    NBTShortWriter.INSTANCE.write(stream, (NBTShort)childTag);
                    break;
                case NBTInteger.ID:
                    NBTIntegerWriter.INSTANCE.write(stream, (NBTInteger)childTag);
                    break;
                case NBTLong.ID:
                    NBTLongWriter.INSTANCE.write(stream, (NBTLong)childTag);
                    break;
                case NBTFloat.ID:
                    NBTFloatWriter.INSTANCE.write(stream, (NBTFloat)childTag);
                    break;
                case NBTDouble.ID:
                    NBTDoubleWriter.INSTANCE.write(stream, (NBTDouble)childTag);
                    break;
                case NBTCompound.ID:
                    INSTANCE.write(stream, tag);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported/invalid NBT tag id found when writing contents to NBTCompoundWriter. Id: " + tag.getId());
            }

        }
    }

}
