package io.github.willqi.pizzaserver.mcworld.world.info;

import io.github.willqi.pizzaserver.mcworld.MCWorld;
import io.github.willqi.pizzaserver.mcworld.ResourceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorldInfoTests {

    @Test
    public void shouldParseTestWorldInfo(@TempDir Path temporaryDir) throws IOException {
        ResourceUtils.copyTestWorld(temporaryDir);
        WorldInfo info;
        try (MCWorld world = new MCWorld(temporaryDir.toFile())) {
            info = world.getWorldInfo();
        }
        ResourceUtils.deleteDirectoryContents(temporaryDir);

        assertEquals("1.16.100 World Test", info.getWorldName());
        assertEquals(true, info.getPlayerAbilities().canAttackPlayers());
    }

}
