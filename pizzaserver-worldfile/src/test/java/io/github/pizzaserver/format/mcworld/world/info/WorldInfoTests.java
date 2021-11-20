package io.github.pizzaserver.format.mcworld.world.info;

import io.github.pizzaserver.format.mcworld.MCWorld;
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
        MCWorld world = new MCWorld(temporaryDir.toFile());
        MCWorldInfo info = world.getWorldInfo();

        ResourceUtils.deleteDirectoryContents(temporaryDir);

        assertEquals("1.16.100 World Test", info.getName());
        assertTrue(info.getPlayerAbilities().canAttackPlayers());
    }

}
