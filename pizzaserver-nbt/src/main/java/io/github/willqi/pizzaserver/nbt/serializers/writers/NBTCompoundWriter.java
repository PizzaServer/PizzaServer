package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;
import java.io.OutputStream;

public class NBTCompoundWriter extends NBTWriter<NBTCompound> {

    public NBTCompoundWriter(OutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTCompound tag) throws IOException {
        for (String name : tag.keySet()) {

            NBTTag childTag = tag.get(name);
            switch (childTag.getId()) {
                case NBTByte.ID:
                    NBTByteWriter byteWriter = new NBTByteWriter(this.stream);
                    byteWriter.write((NBTByte)childTag);
                    break;
                case NBTShort.ID:
                    NBTShortWriter shortWriter = new NBTShortWriter(this.stream);
                    shortWriter.write((NBTShort)childTag);
                    break;
                case NBTInteger.ID:
                    NBTIntegerWriter integerWriter = new NBTIntegerWriter(this.stream);
                    integerWriter.write((NBTInteger)childTag);
                    break;
                case NBTLong.ID:
                    NBTLongWriter longWriter = new NBTLongWriter(this.stream);
                    longWriter.write((NBTLong)childTag);
                    break;
                case NBTFloat.ID:
                    NBTFloatWriter floatWriter = new NBTFloatWriter(this.stream);
                    floatWriter.write((NBTFloat)childTag);
                    break;
                case NBTDouble.ID:
                    NBTDoubleWriter doubleWriter = new NBTDoubleWriter(this.stream);
                    doubleWriter.write((NBTDouble)childTag);
                    break;
                case NBTCompound.ID:
                    NBTCompoundWriter compoundWriter = new NBTCompoundWriter(this.stream);
                    compoundWriter.write((NBTCompound)childTag);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported/invalid NBT tag id found when writing contents to NBTCompoundWriter. Id: " + tag.getId());
            }

        }
        stream.write(NBTContainer.END_ID);
    }

}
