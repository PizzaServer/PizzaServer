package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;

public class NBTCompoundWriter extends NBTWriter<NBTCompound> {

    public static final NBTWriter<NBTCompound> INSTANCE = new NBTCompoundWriter();


    @Override
    public void write(LittleEndianDataOutputStream stream, NBTCompound tag) throws IOException {
        for (String childName : tag.keySet()) {
            Object childTag = tag.get(childName);

            if (Byte.class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.BYTE_TAG_ID);
                stream.writeUTF(childName);
                NBTByteWriter.INSTANCE.write(stream, (Byte)childTag);

            } else if (Short.class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.SHORT_TAG_ID);
                stream.writeUTF(childName);
                NBTShortWriter.INSTANCE.write(stream, (Short)childTag);

            } else if (Integer.class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.INT_TAG_ID);
                stream.writeUTF(childName);
                NBTIntegerWriter.INSTANCE.write(stream, (Integer)childTag);

            } else if (Long.class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.LONG_TAG_ID);
                stream.writeUTF(childName);
                NBTLongWriter.INSTANCE.write(stream, (Long)childTag);

            } else if (Float.class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.FLOAT_TAG_ID);
                stream.writeUTF(childName);
                NBTFloatWriter.INSTANCE.write(stream, (Float)childTag);

            } else if (Double.class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.DOUBLE_TAG_ID);
                stream.writeUTF(childName);
                NBTDoubleWriter.INSTANCE.write(stream, (Double)childTag);

            } else if (byte[].class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.BYTE_ARRAY_TAG_ID);
                stream.writeUTF(childName);
                NBTByteArrayWriter.INSTANCE.write(stream, (byte[])childTag);

            } else if (String.class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.STRING_TAG_ID);
                stream.writeUTF(childName);
                NBTStringWriter.INSTANCE.write(stream, (String)childTag);

            } else if (NBTList.class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.LIST_TAG_ID);
                stream.writeUTF(childName);
                NBTListWriter.INSTANCE.write(stream, (NBTList<Object>)childTag);

            } else if (NBTCompound.class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.COMPOUND_TAG_ID);
                stream.writeUTF(childName);
                this.write(stream, (NBTCompound)childTag);

            } else if (int[].class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.INT_ARRAY_TAG_ID);
                stream.writeUTF(childName);
                NBTIntegerArrayWriter.INSTANCE.write(stream, (int[])childTag);

            } else if (long[].class.equals(childTag.getClass())) {
                stream.writeByte(NBTTag.LONG_ARRAY_TAG_ID);
                stream.writeUTF(childName);
                NBTLongArrayWriter.INSTANCE.write(stream, (long[])childTag);

            } else {
                throw new UnsupportedOperationException("Unsupported/invalid NBT tag id found when writing contents to NBTCompoundWriter. Class: " + childTag.getClass().getName());
            }

        }
        stream.writeByte(NBTTag.END_TAG_ID);
    }

}
