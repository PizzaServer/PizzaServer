package io.github.pizzaserver.format.mcworld.world.chunks.subchunks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.pizzaserver.format.mcworld.utils.VarInts;
import io.github.pizzaserver.format.api.chunks.subchunks.BlockPalette;
import io.github.pizzaserver.format.BlockRuntimeMapper;
import io.github.pizzaserver.format.exceptions.world.chunks.ChunkParseException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.*;

public class MCWorldBlockPalette implements BlockPalette {

    private final BiMap<Integer, Entry> entries = HashBiMap.create();
    private int paletteEntries = 0;


    public MCWorldBlockPalette() {}

    /**
     * Create a palette based on palette data serialized for disk.
     * @param buffer disk buffer
     * @throws IOException if it failed to read the disk serialized block palette
     */
    public MCWorldBlockPalette(ByteBuf buffer) throws IOException {
        int paletteLength = buffer.readIntLE();
        NBTInputStream inputStream = new NBTInputStream(new LittleEndianDataInputStream(new ByteBufInputStream(buffer)));
        try {
            for (int i = 0; i < paletteLength; i++) {
                NBTCompound compound = inputStream.readCompound();
                this.add(new MCWorldBlockPaletteEntry(compound));
            }
        } catch (IOException exception) {
            throw new ChunkParseException("Failed to parse chunk palette.", exception);
        }
    }


    @Override
    public Entry create(String name, NBTCompound states, int version) {
        return new MCWorldBlockPaletteEntry(name, states, version);
    }

    @Override
    public synchronized void add(Entry entry) {
        if (!this.entries.inverse().containsKey(entry)) {
            this.entries.put(this.paletteEntries++, entry);
        }
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    @Override
    public synchronized Set<Entry> getEntries() {
        return Collections.unmodifiableSet(this.entries.values());
    }

    @Override
    public void removeEntry(Entry entry) {
        this.removeEntry(entry, true);
    }

    public synchronized void removeEntry(Entry entry, boolean resize) {
        this.entries.inverse().remove(entry);
        if (resize) {
            this.resize();
        }
    }

    /**
     * Resize modifies the block palette indexes in order to take as less space as possible.
     * Unused palette entries are shifted.
     */
    public synchronized void resize() {
        int resizeStartingIndex = -1;   // The first entry index that we need to relocate
        for (int index = 0; index < this.paletteEntries; index++) {
            if (!this.entries.containsKey(index)) {
                resizeStartingIndex = index + 1;
                break;
            }
        }

        if (resizeStartingIndex > -1) {
            int oldTotalPaletteEntries = this.paletteEntries;
            int freeIndexAt = resizeStartingIndex - 1;  // New entry position - incremented everytime we relocate a entry
            for (int index = resizeStartingIndex; index < oldTotalPaletteEntries; index++) {
                if (this.entries.containsKey(index)) {
                    Entry entry = this.entries.remove(index);
                    this.entries.put(freeIndexAt++, entry);
                } else {
                    // Another entry was removed
                    this.paletteEntries--;
                }
            }
            this.paletteEntries--;
        }
    }

    @Override
    public synchronized Entry getEntry(int index) {
        return this.entries.get(index);
    }

    @Override
    public synchronized int getPaletteIndex(Entry entry) {
        return this.entries.inverse().get(entry);
    }

    @Override
    public byte[] serializeForDisk() throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();
        Set<BlockPalette.Entry> entries = this.getEntries();
        buffer.writeIntLE(entries.size());
        NBTOutputStream outputStream = new NBTOutputStream(new LittleEndianDataOutputStream(new ByteBufOutputStream(buffer)));
        for (BlockPalette.Entry data : entries) {
            NBTCompound compound = new NBTCompound();
            compound.putString("name", data.getId())
                    .putInteger("version", data.getVersion())
                    .putCompound("states", data.getState());

            outputStream.writeCompound(compound);
        }
        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();

        return serialized;
    }

    @Override
    public byte[] serializeForNetwork(BlockRuntimeMapper runtimeMapper) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        Set<BlockPalette.Entry> entries = this.getEntries();
        VarInts.writeInt(buffer, entries.size());
        for (BlockPalette.Entry data : entries) {
            int id = runtimeMapper.getBlockRuntimeId(data.getId(), data.getState());
            VarInts.writeInt(buffer, id);
        }
        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();

        return serialized;
    }


    public static class MCWorldBlockPaletteEntry extends Entry {

        private final String name;
        private final int version;
        private final NBTCompound state;


        public MCWorldBlockPaletteEntry(NBTCompound data) {
            this.name = data.getString("name");
            this.version = data.getInteger("version");
            this.state = data.getCompound("states");
        }

        public MCWorldBlockPaletteEntry(String name, NBTCompound states, int version) {
            this.name = name;
            this.state = states;
            this.version = version;
        }

        @Override
        public String getId() {
            return this.name;
        }

        @Override
        public int getVersion() {
            return this.version;
        }

        @Override
        public NBTCompound getState() {
            return this.state;
        }

    }

}
