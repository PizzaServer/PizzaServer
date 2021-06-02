package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;

public class NBTListWriter<T extends NBTTag> extends NBTWriter<NBTList<T>> {

    private final NBTByteWriter byteWriter = new NBTByteWriter(this.stream);
    private final NBTShortWriter shortWriter = new NBTShortWriter(this.stream);
    private final NBTIntegerWriter integerWriter = new NBTIntegerWriter(this.stream);
    private final NBTLongWriter longWriter = new NBTLongWriter(this.stream);
    private final NBTFloatWriter floatWriter = new NBTFloatWriter(this.stream);
    private final NBTDoubleWriter doubleWriter = new NBTDoubleWriter(this.stream);
    private final NBTByteArrayWriter byteArrayWriter = new NBTByteArrayWriter(this.stream);
    private final NBTStringWriter stringWriter = new NBTStringWriter(this.stream);
    private final NBTIntegerArrayWriter integerArrayWriter = new NBTIntegerArrayWriter(this.stream);

    public NBTListWriter(LittleEndianDataOutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTList<T> tag) throws IOException {
        if (tag.getContents().length == 0) {
            throw new IOException("Cannot write empty NBT list");
        }
        this.stream.writeByte(tag.getContents()[0].getId());
        this.stream.writeInt(tag.getContents().length);

        int contentTagId = tag.getContents()[0].getId();
        for (NBTTag childTag : tag.getContents()) {
            switch (contentTagId) {
                case NBTByte.ID:
                    this.byteWriter.writeTagData((NBTByte)childTag);
                    break;
                case NBTShort.ID:
                    this.shortWriter.writeTagData((NBTShort)childTag);
                    break;
                case NBTInteger.ID:
                    this.integerWriter.writeTagData((NBTInteger)childTag);
                    break;
                case NBTLong.ID:
                    this.longWriter.writeTagData((NBTLong)childTag);
                    break;
                case NBTFloat.ID:
                    this.floatWriter.writeTagData((NBTFloat)childTag);
                    break;
                case NBTDouble.ID:
                    this.doubleWriter.writeTagData((NBTDouble)childTag);
                    break;
                case NBTByteArray.ID:
                    this.byteArrayWriter.writeTagData((NBTByteArray)childTag);
                    break;
                case NBTString.ID:
                    this.stringWriter.writeTagData((NBTString)childTag);
                    break;
                case NBTList.ID:
                    this.writeTagData((NBTList)childTag);
                    break;
                case NBTCompound.ID:
                    new NBTCompoundWriter(this.stream).writeTagData((NBTCompound)childTag);
                    break;
                case NBTIntegerArray.ID:
                    this.integerArrayWriter.write((NBTIntegerArray)childTag);
                    break;
                default:
                    throw new UnsupportedOperationException("Unspported/invalid NBT tag id found when writing contents to NBTListWriter. Id: " + contentTagId);
            }
        }


    }

}
