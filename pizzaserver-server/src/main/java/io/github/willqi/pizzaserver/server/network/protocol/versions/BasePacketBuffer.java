package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.network.protocol.data.EntityLink;
import io.github.willqi.pizzaserver.server.network.protocol.data.Experiment;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventorySlot;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions.InventoryAction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.util.ByteProcessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

/**
 * Wrapper around {@link ByteBuf} that adds additional methods to parse Minecraft Bedrock packets
 */
public class BasePacketBuffer extends ByteBuf {

    private final ByteBuf buffer;
    private BaseMinecraftVersion version;


    public BasePacketBuffer(BaseMinecraftVersion version) {
        this(version, 256);
    }

    public BasePacketBuffer(BaseMinecraftVersion version, int initialCapacity) {
        this(version, ByteBufAllocator.DEFAULT.buffer(initialCapacity));
    }

    public BasePacketBuffer(BaseMinecraftVersion version, ByteBuf buffer) {
        this.buffer = buffer;
        this.version = version;
    }

    protected BasePacketBuffer createInstance(ByteBuf buffer) {
        return new BasePacketBuffer(null, buffer);
    }

    public BaseMinecraftVersion getVersion() {
        if (this.version == null) {
            throw new IllegalStateException("Called getVersion() before version was assigned");
        } else {
            return this.version;
        }
    }

    public void setVersion(BaseMinecraftVersion version) {
        this.version = version;
    }

    protected BasePacketBufferData getData() {
        throw new IllegalStateException("Called getData() before version was assigned");
    }

    @Override
    public int maxFastWritableBytes() {
        return this.buffer.maxFastWritableBytes();
    }

    @Override
    public int capacity() {
        return this.buffer.capacity();
    }

    @Override
    public BasePacketBuffer capacity(int i) {
        this.buffer.capacity(i);
        return this;
    }

    @Override
    public int maxCapacity() {
        return this.buffer.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }

    @Override
    @Deprecated
    public ByteOrder order() {
        return this.buffer.order();
    }

    @Override
    @Deprecated
    public ByteBuf order(ByteOrder byteOrder) {
        return this.buffer.order(byteOrder);
    }

    @Override
    public ByteBuf unwrap() {
        return this.buffer.unwrap();
    }

    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return this.buffer.isReadOnly();
    }

    @Override
    public BasePacketBuffer asReadOnly() {
        return this.createInstance(this.buffer.asReadOnly());
    }

    @Override
    public int readerIndex() {
        return this.buffer.readerIndex();
    }

    @Override
    public BasePacketBuffer readerIndex(int index) {
        this.buffer.readerIndex(index);
        return this;
    }

    @Override
    public int writerIndex() {
        return this.buffer.writerIndex();
    }

    @Override
    public BasePacketBuffer writerIndex(int index) {
        this.buffer.writerIndex(index);
        return this;
    }

    @Override
    public BasePacketBuffer setIndex(int readerIndex, int writerIndex) {
        this.buffer.setIndex(readerIndex, writerIndex);
        return this;
    }

    @Override
    public int readableBytes() {
        return this.buffer.readableBytes();
    }

    @Override
    public int writableBytes() {
        return this.buffer.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return this.buffer.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return this.buffer.isReadable();
    }

    @Override
    public boolean isReadable(int size) {
        return this.buffer.isReadable(size);
    }

    @Override
    public boolean isWritable() {
        return this.buffer.isWritable();
    }

    @Override
    public boolean isWritable(int size) {
        return this.buffer.isWritable(size);
    }

    @Override
    public BasePacketBuffer clear() {
        this.buffer.clear();
        return this;
    }

    @Override
    public BasePacketBuffer markReaderIndex() {
        this.buffer.markReaderIndex();
        return this;
    }

    @Override
    public BasePacketBuffer resetReaderIndex() {
        this.buffer.resetReaderIndex();
        return this;
    }

    @Override
    public BasePacketBuffer markWriterIndex() {
        this.buffer.markWriterIndex();
        return this;
    }

    @Override
    public BasePacketBuffer resetWriterIndex() {
        this.buffer.resetWriterIndex();
        return this;
    }

    @Override
    public BasePacketBuffer discardReadBytes() {
        this.buffer.discardReadBytes();
        return this;
    }

    @Override
    public BasePacketBuffer discardSomeReadBytes() {
        this.buffer.discardSomeReadBytes();
        return this;
    }

    @Override
    public BasePacketBuffer ensureWritable(int minWritableBytes) {
        this.buffer.ensureWritable(minWritableBytes);
        return this;
    }

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        return this.buffer.ensureWritable(minWritableBytes, force);
    }

    @Override
    public boolean getBoolean(int index) {
        return this.buffer.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {
        return this.buffer.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {
        return this.buffer.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {
        return this.buffer.getShort(index);
    }

    @Override
    public short getShortLE(int index) {
        return this.buffer.getShortLE(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return this.buffer.getUnsignedShort(index);
    }

    @Override
    public int getUnsignedShortLE(int index) {
        return this.buffer.getUnsignedShortLE(index);
    }

    @Override
    public int getMedium(int index) {
        return this.buffer.getMedium(index);
    }

    @Override
    public int getMediumLE(int index) {
        return this.buffer.getMediumLE(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return this.buffer.getUnsignedMedium(index);
    }

    @Override
    public int getUnsignedMediumLE(int index) {
        return this.buffer.getUnsignedMediumLE(index);
    }

    @Override
    public int getInt(int index) {
        return this.buffer.getInt(index);
    }

    @Override
    public int getIntLE(int index) {
        return this.buffer.getIntLE(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return this.buffer.getUnsignedInt(index);
    }

    @Override
    public long getUnsignedIntLE(int index) {
        return this.buffer.getUnsignedIntLE(index);
    }

    @Override
    public long getLong(int index) {
        return this.buffer.getLong(index);
    }

    @Override
    public long getLongLE(int index) {
        return this.buffer.getLongLE(index);
    }

    @Override
    public char getChar(int index) {
        return this.buffer.getChar(index);
    }

    @Override
    public float getFloat(int index) {
        return this.buffer.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return this.buffer.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf byteBuf) {
        return this.buffer.getBytes(index, byteBuf);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf byteBuf, int length) {
        return this.buffer.getBytes(index, byteBuf, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf byteBuf, int dstLength, int length) {
        return this.buffer.getBytes(index, byteBuf, dstLength, length);
    }

    @Override
    public BasePacketBuffer getBytes(int index, byte[] bytes) {
        this.buffer.getBytes(index, bytes);
        return this;
    }

    @Override
    public BasePacketBuffer getBytes(int index, byte[] bytes, int dstLength, int length) {
        this.buffer.getBytes(index, bytes, dstLength, length);
        return this;
    }

    @Override
    public BasePacketBuffer getBytes(int index, ByteBuffer byteBuffer) {
        this.buffer.getBytes(index, byteBuffer);
        return this;
    }

    @Override
    public BasePacketBuffer getBytes(int index, OutputStream outputStream, int length) throws IOException {
        this.buffer.getBytes(index, outputStream, length);
        return this;
    }

    @Override
    public int getBytes(int index, GatheringByteChannel gatheringByteChannel, int length) throws IOException {
        return this.buffer.getBytes(index, gatheringByteChannel, length);
    }

    @Override
    public int getBytes(int index, FileChannel fileChannel, long position, int length) throws IOException {
        return this.buffer.getBytes(index, fileChannel, position, length);
    }

    @Override
    public CharSequence getCharSequence(int index, int length, Charset charSet) {
        return this.buffer.getCharSequence(index, length, charSet);
    }

    @Override
    public BasePacketBuffer setBoolean(int index, boolean value) {
        this.buffer.setBoolean(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setByte(int index, int value) {
        this.buffer.setByte(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setShort(int index, int value) {
        this.buffer.setShort(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setShortLE(int index, int value) {
        this.buffer.setShortLE(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setMedium(int index, int value) {
        this.buffer.setMedium(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setMediumLE(int index, int value) {
        this.buffer.setMediumLE(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setInt(int index, int value) {
        this.buffer.setInt(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setIntLE(int index, int value) {
        this.buffer.setIntLE(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setLong(int index, long value) {
        this.buffer.setLong(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setLongLE(int index, long value) {
        this.buffer.setLongLE(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setChar(int index, int value) {
        this.buffer.setChar(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setFloat(int index, float value) {
        this.buffer.setFloat(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setDouble(int index, double value) {
        this.buffer.setDouble(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setBytes(int index, ByteBuf value) {
        this.buffer.setBytes(index, value);
        return this;
    }

    @Override
    public BasePacketBuffer setBytes(int index, ByteBuf byteBuf, int length) {
        this.buffer.setBytes(index, byteBuf, length);
        return this;
    }

    @Override
    public BasePacketBuffer setBytes(int index, ByteBuf byteBuf, int srcIndex, int length) {
        this.buffer.setBytes(index, byteBuf, srcIndex, length);
        return this;
    }

    @Override
    public BasePacketBuffer setBytes(int index, byte[] bytes) {
        this.buffer.setBytes(index, bytes);
        return this;
    }

    @Override
    public BasePacketBuffer setBytes(int index, byte[] bytes, int srcIndex, int length) {
        this.buffer.setBytes(index, bytes, srcIndex, length);
        return this;
    }

    @Override
    public BasePacketBuffer setBytes(int index, ByteBuffer byteBuffer) {
        this.buffer.setBytes(index, byteBuffer);
        return this;
    }

    @Override
    public int setBytes(int index, InputStream inputStream, int length) throws IOException {
        return this.buffer.setBytes(index, inputStream, length);
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel scatteringByteChannel, int length) throws IOException {
        return this.buffer.setBytes(index, scatteringByteChannel, length);
    }

    @Override
    public int setBytes(int index, FileChannel fileChannel, long position, int length) throws IOException {
        return this.buffer.setBytes(index, fileChannel, position, length);
    }

    @Override
    public BasePacketBuffer setZero(int index, int length) {
        this.buffer.setZero(index, length);
        return this;
    }

    @Override
    public int setCharSequence(int index, CharSequence charSequence, Charset charSet) {
        return this.buffer.setCharSequence(index, charSequence, charSet);
    }

    @Override
    public boolean readBoolean() {
        return this.buffer.readBoolean();
    }

    @Override
    public byte readByte() {
        return this.buffer.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return this.buffer.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return this.buffer.readShort();
    }

    @Override
    public short readShortLE() {
        return this.buffer.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return this.buffer.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return this.buffer.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return this.buffer.readMedium();
    }

    @Override
    public int readMediumLE() {
        return this.buffer.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return this.buffer.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return this.buffer.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return this.buffer.readInt();
    }

    @Override
    public int readIntLE() {
        return this.buffer.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return this.buffer.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return this.buffer.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return this.buffer.readLong();
    }

    @Override
    public long readLongLE() {
        return this.buffer.readLongLE();
    }

    @Override
    public char readChar() {
        return this.buffer.readChar();
    }

    @Override
    public float readFloat() {
        return this.buffer.readFloat();
    }

    @Override
    public double readDouble() {
        return this.buffer.readDouble();
    }

    public long readVarLong() {
        return VarInts.readLong(this.buffer);
    }

    public long readUnsignedVarLong() {
        return VarInts.readUnsignedLong(this.buffer);
    }

    public int readVarInt() {
        return VarInts.readInt(this.buffer);
    }

    public int readUnsignedVarInt() {
        return VarInts.readUnsignedInt(this.buffer);
    }

    public String readString() {
        return new String(this.readByteArray(), StandardCharsets.UTF_8);
    }

    public String readStringLE() {
        byte[] data = new byte[this.readIntLE()];
        this.readBytes(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    public UUID readUUID() {
        return new UUID(buffer.readLongLE(), buffer.readLongLE());
    }

    public byte[] readByteArray() {
        byte[] data = new byte[this.readUnsignedVarInt()];
        this.readBytes(data);
        return data;
    }

    public Vector3 readVector3() {
        return new Vector3(
                this.readFloatLE(),
                this.buffer.readFloatLE(),
                this.buffer.readFloatLE()
        );
    }

    public Vector3i readVector3i() {
        return new Vector3i(
            this.readVarInt(),
            this.readUnsignedVarInt(),
            this.readVarInt()
        );
    }

    public NBTCompound readNBTCompound() {
        NBTInputStream nbtOutputStream = new NBTInputStream(new VarIntDataInputStream(new ByteBufInputStream(this)));
        try {
            return nbtOutputStream.readCompound();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read NBTCompound", exception);
        }
    }

    public Skin readSkin() {
        throw new IllegalStateException("Called readSkin() before version was assigned.");
    }

    public ItemStack readItem() {
        throw new IllegalStateException("Called readItem() before version was assigned.");
    }

    public InventoryAction readInventoryAction() {
        throw new IllegalStateException("Called readInventoryAction() before version was assigned.");
    }

    public InventorySlot readInventorySlot() {
        throw new IllegalStateException("Called readInventorySlot() before version was assigned.");
    }

    @Override
    public BasePacketBuffer readBytes(int length) {
        return this.createInstance(this.buffer.readBytes(length));
    }

    @Override
    public BasePacketBuffer readSlice(int length) {
        return this.createInstance(this.buffer.readSlice(length));
    }

    @Override
    public BasePacketBuffer readRetainedSlice(int length) {
        return this.createInstance(this.buffer.readRetainedSlice(length));
    }

    @Override
    public BasePacketBuffer readBytes(ByteBuf byteBuf) {
        this.buffer.readBytes(byteBuf);
        return this;
    }

    @Override
    public BasePacketBuffer readBytes(ByteBuf byteBuf, int length) {
        this.buffer.readBytes(byteBuf, length);
        return this;
    }

    @Override
    public BasePacketBuffer readBytes(ByteBuf byteBuf, int dstIndex, int length) {
        this.buffer.readBytes(byteBuf, dstIndex, length);
        return this;
    }

    @Override
    public BasePacketBuffer readBytes(byte[] bytes) {
        this.buffer.readBytes(bytes);
        return this;
    }

    @Override
    public BasePacketBuffer readBytes(byte[] bytes, int dstIndex, int length) {
        this.buffer.readBytes(bytes, dstIndex, length);
        return this;
    }

    @Override
    public BasePacketBuffer readBytes(ByteBuffer byteBuffer) {
        this.buffer.readBytes(byteBuffer);
        return this;
    }

    @Override
    public BasePacketBuffer readBytes(OutputStream outputStream, int length) throws IOException {
        this.buffer.readBytes(outputStream, length);
        return this;
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int length) throws IOException {
        return this.buffer.readBytes(gatheringByteChannel, length);
    }

    @Override
    public CharSequence readCharSequence(int index, Charset charSet) {
        return this.buffer.readCharSequence(index, charSet);
    }

    @Override
    public int readBytes(FileChannel fileChannel, long length, int index) throws IOException {
        return this.buffer.readBytes(fileChannel, length, index);
    }

    @Override
    public BasePacketBuffer skipBytes(int amount) {
        this.buffer.skipBytes(amount);
        return this;
    }

    @Override
    public BasePacketBuffer writeBoolean(boolean value) {
        this.buffer.writeBoolean(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeByte(int value) {
        this.buffer.writeByte(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeShort(int value) {
        this.buffer.writeShortLE(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeShortLE(int value) {
        this.buffer.writeShortLE(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeMedium(int value) {
        this.buffer.writeMedium(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeMediumLE(int value) {
        this.buffer.writeMediumLE(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeInt(int value) {
        this.buffer.writeInt(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeIntLE(int value) {
        this.buffer.writeIntLE(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeLong(long value) {
        this.buffer.writeLong(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeLongLE(long value) {
        this.buffer.writeLongLE(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeChar(int value) {
        this.buffer.writeChar(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeFloat(float value) {
        this.buffer.writeFloatLE(value);
        return this;
    }

    @Override
    public BasePacketBuffer writeDouble(double value) {
        this.buffer.writeDouble(value);
        return this;
    }

    public BasePacketBuffer writeVarLong(long value) {
        VarInts.writeLong(this.buffer, value);
        return this;
    }

    public BasePacketBuffer writeUnsignedVarLong(long value) {
        VarInts.writeUnsignedLong(this.buffer, value);
        return this;
    }

    public BasePacketBuffer writeVarInt(int value) {
        VarInts.writeInt(this.buffer, value);
        return this;
    }

    public BasePacketBuffer writeUnsignedVarInt(int value) {
        VarInts.writeUnsignedInt(this.buffer, value);
        return this;
    }

    public BasePacketBuffer writeString(String string) {
        return this.writeByteArray(string.getBytes(StandardCharsets.UTF_8));
    }

    public BasePacketBuffer writeUUID(UUID uuid) {
        this.buffer.writeLongLE(uuid.getMostSignificantBits());
        this.buffer.writeLongLE(uuid.getLeastSignificantBits());
        return this;
    }

    public BasePacketBuffer writeByteArray(byte[] data) {
        this.writeUnsignedVarInt(data.length);
        this.writeBytes(data);
        return this;
    }

    public BasePacketBuffer writeVector3(Vector3 vector3) {
        this.writeFloatLE(vector3.getX());
        this.writeFloatLE(vector3.getY());
        this.writeFloatLE(vector3.getZ());
        return this;
    }

    public BasePacketBuffer writeVector3i(Vector3i vector3i) {
        this.writeVarInt(vector3i.getX());
        this.writeUnsignedVarInt(vector3i.getY());
        this.writeVarInt(vector3i.getZ());
        return this;
    }

    public BasePacketBuffer writeNBTCompound(NBTCompound compound) {
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        try (NBTOutputStream nbtOutputStream = new NBTOutputStream(new VarIntDataOutputStream(resultStream))) {
            nbtOutputStream.writeCompound(compound);
            this.writeBytes(resultStream.toByteArray());
        } catch (IOException exception) {
            throw new RuntimeException("Failed to write NBTCompound", exception);
        }
        return this;
    }

    public BasePacketBuffer writeExperiments(Set<Experiment> experiments) {
        this.writeIntLE(experiments.size());
        for (Experiment experiment : experiments) {
            this.writeString(experiment.getId());
            this.writeBoolean(true);  // It's enabled if it's included
        }
        return this;
    }

    public BasePacketBuffer writeItem(ItemStack data) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    public BasePacketBuffer writeSkin(Skin skin) {
        throw new IllegalStateException("Called writeSkin(Skin skin) before version was assigned.");
    }

    public BasePacketBuffer writeInventoryAction(InventoryAction action) {
        throw new IllegalStateException("Called writeInventoryAction() before version was assigned.");
    }

    public BasePacketBuffer writeInventorySlot(InventorySlot data) {
        throw new IllegalStateException("Called readInventorySlot() before version was assigned.");
    }

    public BasePacketBuffer writeEntityMetadata(EntityMetaData entityMetaData) {
        throw new IllegalStateException("Called writeEntityMetadata(EntityMetaData entityMetaData) before version was assigned.");
    }

    public BasePacketBuffer writeEntityLink(EntityLink entityLink) {
        throw new IllegalStateException("Called writeEntityLink(EntityLink entityLink) before version was assigned.");
    }

    @Override
    public BasePacketBuffer writeBytes(ByteBuf byteBuf) {
        this.buffer.writeBytes(byteBuf);
        return this;
    }

    @Override
    public BasePacketBuffer writeBytes(ByteBuf byteBuf, int index) {
        this.buffer.writeBytes(byteBuf, index);
        return this;
    }

    @Override
    public BasePacketBuffer writeBytes(ByteBuf byteBuf, int srcIndex, int length) {
        this.buffer.writeBytes(byteBuf, srcIndex, length);
        return this;
    }

    @Override
    public BasePacketBuffer writeBytes(byte[] bytes) {
        this.buffer.writeBytes(bytes);
        return this;
    }

    @Override
    public BasePacketBuffer writeBytes(byte[] bytes, int srcIndex, int length) {
        this.buffer.writeBytes(bytes, srcIndex, length);
        return this;
    }

    @Override
    public BasePacketBuffer writeBytes(ByteBuffer byteBuffer) {
        this.buffer.writeBytes(byteBuffer);
        return this;
    }

    @Override
    public int writeBytes(InputStream inputStream, int index) throws IOException {
        return this.buffer.writeBytes(inputStream, index);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int index) throws IOException {
        return this.buffer.writeBytes(scatteringByteChannel, index);
    }

    @Override
    public int writeBytes(FileChannel fileChannel, long length, int index) throws IOException {
        return this.buffer.writeBytes(fileChannel, length, index);
    }

    @Override
    public BasePacketBuffer writeZero(int amount) {
        this.buffer.writeZero(amount);
        return this;
    }

    @Override
    public int writeCharSequence(CharSequence charSequence, Charset charSet) {
        return this.buffer.writeCharSequence(charSequence, charSet);
    }

    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return this.buffer.indexOf(fromIndex, toIndex, value);
    }

    @Override
    public int bytesBefore(byte value) {
        return this.buffer.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {
        return this.buffer.bytesBefore(length, value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {
        return this.buffer.bytesBefore(index, length, value);
    }

    @Override
    public int forEachByte(ByteProcessor byteProcessor) {
        return this.buffer.forEachByte(byteProcessor);
    }

    @Override
    public int forEachByte(int index, int length, ByteProcessor byteProcessor) {
        return this.buffer.forEachByte(index, length, byteProcessor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor byteProcessor) {
        return this.buffer.forEachByteDesc(byteProcessor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteProcessor byteProcessor) {
        return this.buffer.forEachByteDesc(index, length, byteProcessor);
    }

    @Override
    public BasePacketBuffer copy() {
        return this.createInstance(this.buffer.copy());
    }

    @Override
    public BasePacketBuffer copy(int index, int length) {
        return this.createInstance(this.buffer.copy(index, length));
    }

    @Override
    public BasePacketBuffer slice() {
        return this.createInstance(this.buffer.slice());
    }

    @Override
    public BasePacketBuffer retainedSlice() {
        return this.createInstance(this.buffer.retainedSlice());
    }

    @Override
    public BasePacketBuffer slice(int index, int length) {
        return this.createInstance(this.buffer.slice(index, length));
    }

    @Override
    public BasePacketBuffer retainedSlice(int index, int length) {
        return this.createInstance(this.buffer.retainedSlice(index, length));
    }

    @Override
    public BasePacketBuffer duplicate() {
        return this.createInstance(this.buffer.duplicate());
    }

    @Override
    public BasePacketBuffer retainedDuplicate() {
        return this.createInstance(this.buffer.retainedDuplicate());
    }

    @Override
    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.buffer.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return this.buffer.nioBuffer();
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        return this.buffer.internalNioBuffer(index, length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.buffer.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return this.buffer.nioBuffers(index, length);
    }

    @Override
    public boolean hasArray() {
        return this.buffer.hasArray();
    }

    @Override
    public byte[] array() {
        return this.buffer.array();
    }

    @Override
    public int arrayOffset() {
        return this.buffer.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.buffer.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return this.buffer.memoryAddress();
    }

    @Override
    public String toString(Charset charSet) {
        return this.buffer.toString(charSet);
    }

    @Override
    public String toString(int index, int length, Charset charSet) {
        return this.buffer.toString(index, length, charSet);
    }

    @Override
    public int hashCode() {
        return (37 * this.buffer.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BasePacketBuffer) {
            return ((BasePacketBuffer)obj).buffer.equals(this.buffer);
        }
        return false;
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return this.buffer.compareTo(byteBuf);
    }

    @Override
    public String toString() {
        return this.buffer.toString();
    }

    @Override
    public BasePacketBuffer retain(int increment) {
        this.buffer.retain(increment);
        return this;
    }

    @Override
    public BasePacketBuffer retain() {
        this.buffer.retain();
        return this;
    }

    @Override
    public BasePacketBuffer touch() {
        this.buffer.touch();
        return this;
    }

    @Override
    public BasePacketBuffer touch(Object obj) {
        this.buffer.touch(obj);
        return this;
    }

    @Override
    public int refCnt() {
        return this.buffer.refCnt();
    }

    @Override
    public boolean release() {
        return this.buffer.release();
    }

    @Override
    public boolean release(int decrement) {
        return this.buffer.release(decrement);
    }

}
