package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;

public class NBTListWriter<T> extends NBTWriter<NBTList<T>> {

    public static final NBTWriter<NBTList<?>> INSTANCE = new NBTListWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, NBTList<T> data) throws IOException {
        int childrenTagId = data.getChildrenTypeId();

        stream.writeByte(childrenTagId);
        stream.writeInt(data.getContents().length);

        for (Object childTag : data.getContents()) {
            switch (childrenTagId) {
                case NBTTag.BYTE_TAG_ID:
                    NBTByteWriter.INSTANCE.write(stream, (byte)childTag);
                    break;
                case NBTTag.SHORT_TAG_ID:
                    NBTShortWriter.INSTANCE.write(stream, (short)childTag);

                    break;
                case NBTTag.INT_TAG_ID:
                    NBTIntegerWriter.INSTANCE.write(stream, (int)childTag);
                    break;
                case NBTTag.LONG_TAG_ID:
                    NBTLongWriter.INSTANCE.write(stream, (long)childTag);
                    break;
                case NBTTag.FLOAT_TAG_ID:
                    NBTFloatWriter.INSTANCE.write(stream, (float)childTag);
                    break;
                case NBTTag.DOUBLE_TAG_ID:
                    NBTDoubleWriter.INSTANCE.write(stream, (double)childTag);
                    break;
                case NBTTag.BYTE_ARRAY_TAG_ID:
                    NBTByteArrayWriter.INSTANCE.write(stream, (byte[])childTag);
                    break;
                case NBTTag.STRING_TAG_ID:
                    NBTStringWriter.INSTANCE.write(stream, (String)childTag);
                    break;
                case NBTTag.LIST_TAG_ID:
                    NBTListWriter.INSTANCE.write(stream, (NBTList<Object>)childTag);
                    break;
                case NBTTag.COMPOUND_TAG_ID:
                    NBTCompoundWriter.INSTANCE.write(stream, (NBTCompound)childTag);
                    break;
                case NBTTag.INT_ARRAY_TAG_ID:
                    NBTIntegerArrayWriter.INSTANCE.write(stream, (int[])childTag);
                    break;
                case NBTTag.LONG_ARRAY_TAG_ID:
                    NBTLongArrayWriter.INSTANCE.write(stream, (long[])childTag);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported/invalid NBT tag id found when reading contents in NBTListReader. Id: " + childrenTagId);
            }
        }


    }

}
