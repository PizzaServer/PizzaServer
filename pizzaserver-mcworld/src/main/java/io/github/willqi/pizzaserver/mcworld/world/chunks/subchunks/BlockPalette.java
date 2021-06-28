package io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockPalette {

    private final List<BlockPaletteData> paletteData = new ArrayList<>();


    public void add(NBTCompound data) {
        this.paletteData.add(new BlockPaletteData(data));
    }

    public List<BlockPaletteData> getAllPaletteData() {
        return Collections.unmodifiableList(paletteData);
    }
    
    public BlockPaletteData getPaletteData(int index) {
        return this.paletteData.get(index);
    }


    public static class BlockPaletteData {

        private final String name;
        private final int version;
        private final NBTCompound state;


        public BlockPaletteData(NBTCompound data) {
            this.name = data.getString("name").getValue();
            this.version = data.getInteger("version").getValue();
            this.state = data.getCompound("states");
        }

        public String getName() {
            return this.name;
        }

        public int getVersion() {
            return this.version;
        }

        public NBTCompound getState() {
            return this.state;
        }


    }

}
