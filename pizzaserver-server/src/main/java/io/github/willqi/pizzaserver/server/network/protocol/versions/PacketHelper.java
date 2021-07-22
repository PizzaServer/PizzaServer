package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.item.Item;
import io.github.willqi.pizzaserver.server.network.protocol.data.Experiment;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Different versions of Minecraft encode packets differently.
 */
public abstract class PacketHelper {

    protected Set<Experiment> supportedExperiments = new HashSet<>();


    public abstract void writeItem(Item item, ByteBuf buffer);

    public String readLEString(ByteBuf buffer) {
        int length = buffer.readIntLE();
        String data = buffer.readSlice(length).toString(StandardCharsets.UTF_8);
        return data;
    }

    public String readString(ByteBuf buffer) {
        byte[] data = readByteArray(buffer);
        return new String(data, StandardCharsets.UTF_8);
    }

    public void writeString(String string, ByteBuf buffer) {
        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        writeByteArray(data, buffer);
    }

    public byte[] readByteArray(ByteBuf buffer) {
        int length = VarInts.readUnsignedInt(buffer);
        byte[] data = new byte[length];
        buffer.readBytes(data);
        return data;
    }

    public void writeByteArray(byte[] bytes, ByteBuf buffer) {
        VarInts.writeUnsignedInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
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

}
