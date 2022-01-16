package io.github.pizzaserver.format.mcworld.world.info;

import io.github.pizzaserver.format.api.LevelData;
import io.github.pizzaserver.format.mcworld.MCWorldLevel;
import io.github.pizzaserver.format.mcworld.ResourceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldInfoTests {

    @Test
    public void shouldParseTestWorldInfo(@TempDir Path temporaryDir) throws IOException {
        ResourceUtils.copyTestWorld(temporaryDir);
        MCWorldLevel world = new MCWorldLevel(temporaryDir.toFile());
        try {
            LevelData info = world.getLevelData();

            assertEquals("1.16.100 World Test", info.getName());
            assertTrue(info.getPlayerAbilities().canAttackPlayers());
        } finally {
            world.close();
            ResourceUtils.deleteDirectoryContents(temporaryDir);
        }
    }
}
