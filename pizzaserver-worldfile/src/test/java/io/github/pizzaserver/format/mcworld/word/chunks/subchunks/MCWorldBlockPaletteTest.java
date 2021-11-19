package io.github.pizzaserver.format.mcworld.word.chunks.subchunks;

import io.github.pizzaserver.format.mcworld.world.chunks.subchunks.MCWorldBlockPalette;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MCWorldBlockPaletteTest {

    @Test
    public void resizeShouldProperlyMovePaletteIndexes() {
        MCWorldBlockPalette palette = new MCWorldBlockPalette();

        MCWorldBlockPalette.MCWorldBlockPaletteEntry[] entries = new MCWorldBlockPalette.MCWorldBlockPaletteEntry[]{
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("a", new NBTCompound(), 0),
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("b", new NBTCompound(), 0),
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("c", new NBTCompound(), 0),
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("d", new NBTCompound(), 0),
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("e", new NBTCompound(), 0)
        };
        for (MCWorldBlockPalette.MCWorldBlockPaletteEntry entry : entries) {
            palette.add(entry);
        }
        palette.removeEntry(entries[0]);
        assertEquals(0, palette.getPaletteIndex(entries[1]));

        palette.removeEntry(entries[2], false);
        palette.removeEntry(entries[3], false);
        palette.resize();
        assertEquals(1, palette.getPaletteIndex(entries[4]));
    }

}
