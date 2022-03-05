package io.github.pizzaserver.format.dimension.chunks.subchunk.utils;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.format.dimension.chunks.subchunk.BlockPaletteEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaletteTest {

    @Test
    public void resizeShouldProperlyMovePaletteIndexes() {
        Palette<BlockPaletteEntry> palette = new Palette<>();

        BlockPaletteEntry[] entries = new BlockPaletteEntry[]{
                new BlockPaletteEntry("a", 0, NbtMap.EMPTY),
                new BlockPaletteEntry("b", 0, NbtMap.EMPTY),
                new BlockPaletteEntry("c", 0, NbtMap.EMPTY),
                new BlockPaletteEntry("d", 0, NbtMap.EMPTY),
                new BlockPaletteEntry("e", 0, NbtMap.EMPTY)
        };
        for (BlockPaletteEntry entry : entries) {
            palette.addEntry(entry);
        }
        palette.removeEntry(entries[0]);
        palette.removeEntry(entries[2]);
        palette.removeEntry(entries[3]);
        palette.resize();
        assertEquals(1, palette.getPaletteIndex(entries[4]));
    }

}
