package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.ld.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;

public class NBTCompoundWriter extends NBTWriter<NBTCompound> {

    private final NBTByteWriter byteWriter = new NBTByteWriter(this.stream);
    private final NBTShortWriter shortWriter = new NBTShortWriter(this.stream);
    private final NBTIntegerWriter integerWriter = new NBTIntegerWriter(this.stream);
    private final NBTLongWriter longWriter = new NBTLongWriter(this.stream);
    private final NBTFloatWriter floatWriter = new NBTFloatWriter(this.stream);
    private final NBTDoubleWriter doubleWriter = new NBTDoubleWriter(this.stream);
    private final NBTByteArrayWriter byteArrayWriter = new NBTByteArrayWriter(this.stream);
    private final NBTStringWriter stringWriter = new NBTStringWriter(this.stream);


    public NBTCompoundWriter(LittleEndianDataOutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTCompound tag) throws IOException {
        for (String name : tag.keySet()) {

            NBTTag childTag = tag.get(name);
            childTag.setName(name);
            switch (childTag.getId()) {
                case NBTByte.ID:
                    this.byteWriter.write((NBTByte)childTag);
                    break;
                case NBTShort.ID:
                    this.shortWriter.write((NBTShort)childTag);
                    break;
                case NBTInteger.ID:
                    this.integerWriter.write((NBTInteger)childTag);
                    break;
                case NBTLong.ID:
                    this.longWriter.write((NBTLong)childTag);
                    break;
                case NBTFloat.ID:
                    this.floatWriter.write((NBTFloat)childTag);
                    break;
                case NBTDouble.ID:
                    this.doubleWriter.write((NBTDouble)childTag);
                    break;
                case NBTByteArray.ID:
                    this.byteArrayWriter.write((NBTByteArray)childTag);
                    break;
                case NBTString.ID:
                    this.stringWriter.write((NBTString)childTag);
                    break;
                case NBTList.ID:
                    NBTListWriter<? extends NBTTag> listWriter = new NBTListWriter<>(this.stream);
                    listWriter.writeTagData((NBTList)childTag);
                    break;
                case NBTCompound.ID:
                    this.write((NBTCompound)childTag);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported/invalid NBT tag id found when writing contents to NBTCompoundWriter. Id: " + tag.getId());
            }

        }
        stream.write(NBTContainer.END_ID);
    }

}
