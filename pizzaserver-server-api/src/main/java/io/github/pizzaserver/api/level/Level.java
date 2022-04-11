package io.github.pizzaserver.api.level;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.level.data.Difficulty;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.data.Dimension;

import java.io.IOException;

public interface Level {

    LevelManager getLevelManager();

    String getName();

    Server getServer();

    World getDimension(Dimension dimension);

    Difficulty getDifficulty();

    void setDifficulty(Difficulty difficulty);

    void save() throws IOException;

}
