package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.item.Item;
import io.github.willqi.pizzaserver.server.network.protocol.data.Experiment;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.EmptyArrays;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Different versions of Minecraft encode packets differently.
 */
public abstract class BasePacketHelper {

    protected Set<Experiment> supportedExperiments = new HashSet<>();


    public abstract void writeItem(Item item, ByteBuf buffer);

    public void writeString(String string, ByteBuf buffer) {
        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        writeByteArray(data, buffer);
    }

    public String readString(ByteBuf buffer) {
        byte[] data = readByteArray(buffer);
        return new String(data, StandardCharsets.UTF_8);
    }

    public String readLEString(ByteBuf buffer) {
        int length = buffer.readIntLE();
        String data = buffer.readSlice(length).toString(StandardCharsets.UTF_8);
        return data;
    }

    public void writeByteArray(byte[] bytes, ByteBuf buffer) {
        VarInts.writeUnsignedInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
    }

    public byte[] readByteArray(ByteBuf buffer) {
        int length = VarInts.readUnsignedInt(buffer);
        byte[] data = new byte[length];
        buffer.readBytes(data);
        return data;
    }

    public void writeNBTCompound(NBTCompound compound, ByteBuf buffer) {
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        try (NBTOutputStream nbtOutputStream = new NBTOutputStream(new VarIntDataOutputStream(resultStream))) {
            nbtOutputStream.writeCompound(compound);
            buffer.writeBytes(resultStream.toByteArray());
        } catch (IOException exception) {
            throw new RuntimeException("Failed to write NBTCompound", exception);
        }
    }

    public void writeExperiments(Set<Experiment> experiments, ByteBuf buffer)  {
        Set<Experiment> filteredExperiments = experiments.stream()
                .filter(this.supportedExperiments::contains)
                .collect(Collectors.toSet());

        buffer.writeIntLE(filteredExperiments.size());
        for (Experiment experiment : filteredExperiments) {
            this.writeString(experiment.getId(), buffer);
            buffer.writeBoolean(true);  // It's enabled if it's included
        }
    }

    public void writeVector3(ByteBuf buffer, Vector3 vector3) {
        buffer.writeFloatLE(vector3.getX());
        buffer.writeFloatLE(vector3.getY());
        buffer.writeFloatLE(vector3.getZ());
    }

    public Vector3 readVector3(ByteBuf buffer) {
        return new Vector3(
                buffer.readFloatLE(),
                buffer.readFloatLE(),
                buffer.readFloatLE()
        );
    }

    public void writeBlockVector(ByteBuf buffer, Vector3i vector3) {
        VarInts.writeInt(buffer, vector3.getX());
        VarInts.writeUnsignedInt(buffer, vector3.getY());
        VarInts.writeInt(buffer, vector3.getZ());
    }

    public Vector3i readBlockVector(ByteBuf buffer) {
        return new Vector3i(
                VarInts.readInt(buffer),
                VarInts.readUnsignedInt(buffer),
                VarInts.readInt(buffer)
        );
    }

    public UUID readUUID(ByteBuf buffer) {
        byte[] bytes = new byte[16];
        buffer.readBytes(bytes);
        return new UUID(readLLong(bytes), readLLong(new byte[]{
                bytes[8],
                bytes[9],
                bytes[10],
                bytes[11],
                bytes[12],
                bytes[13],
                bytes[14],
                bytes[15]
        }));
    }

    public static long readLLong(byte[] bytes) {
        return (((long) bytes[7] << 56) +
                ((long) (bytes[6] & 0xFF) << 48) +
                ((long) (bytes[5] & 0xFF) << 40) +
                ((long) (bytes[4] & 0xFF) << 32) +
                ((long) (bytes[3] & 0xFF) << 24) +
                ((bytes[2] & 0xFF) << 16) +
                ((bytes[1] & 0xFF) << 8) +
                ((bytes[0] & 0xFF)));
    }

}
