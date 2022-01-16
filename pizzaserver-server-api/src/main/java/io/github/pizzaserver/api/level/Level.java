package io.github.pizzaserver.api.level;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.data.Dimension;

import java.io.IOException;

public interface Level {

    LevelManager getLevelManager();

    String getName();

    Server getServer();

    World getDimension(Dimension dimension);

    void save() throws IOException;
}
