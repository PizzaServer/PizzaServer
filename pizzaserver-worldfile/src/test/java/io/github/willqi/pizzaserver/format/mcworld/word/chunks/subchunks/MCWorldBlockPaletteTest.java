package io.github.willqi.pizzaserver.format.mcworld.word.chunks.subchunks;

import io.github.willqi.pizzaserver.format.mcworld.world.chunks.subchunks.MCWorldBlockPalette;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MCWorldBlockPaletteTest {

    @Test
    public void resizeShouldProperlyMovePaletteIndexes() {
        MCWorldBlockPalette palette = new MCWorldBlockPalette();

        MCWorldBlockPalette.MCWorldEntry[] entries = new MCWorldBlockPalette.MCWorldEntry[]{
                new MCWorldBlockPalette.MCWorldEntry("a", new NBTCompound(), 0),
                new MCWorldBlockPalette.MCWorldEntry("b", new NBTCompound(), 0),
                new MCWorldBlockPalette.MCWorldEntry("c", new NBTCompound(), 0),
                new MCWorldBlockPalette.MCWorldEntry("d", new NBTCompound(), 0),
                new MCWorldBlockPalette.MCWorldEntry("e", new NBTCompound(), 0)
        };
        for (MCWorldBlockPalette.MCWorldEntry entry : entries) {
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
