package io.github.pizzaserver.format.mcworld.word.chunks.subchunks;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.format.mcworld.world.chunks.subchunks.MCWorldBlockPalette;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MCWorldLevelBlockPaletteTest {

    @Test
    public void resizeShouldProperlyMovePaletteIndexes() {
        MCWorldBlockPalette palette = new MCWorldBlockPalette();

        MCWorldBlockPalette.MCWorldBlockPaletteEntry[] entries = new MCWorldBlockPalette.MCWorldBlockPaletteEntry[]{
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("a", NbtMap.EMPTY, 0),
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("b", NbtMap.EMPTY, 0),
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("c", NbtMap.EMPTY, 0),
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("d", NbtMap.EMPTY, 0),
                new MCWorldBlockPalette.MCWorldBlockPaletteEntry("e", NbtMap.EMPTY, 0)
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
