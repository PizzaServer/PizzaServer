package io.github.pizzaserver.api.level;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.level.data.Difficulty;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.player.Player;

import java.io.IOException;
import java.util.Set;

public interface Level {

    LevelManager getLevelManager();

    String getName();

    Server getServer();

    Set<Player> getPlayers();

    World getDimension(Dimension dimension);

    boolean isDay();

    int getTime();

    void setTime(int time);

    Difficulty getDifficulty();

    void setDifficulty(Difficulty difficulty);

    LevelGameRules getGameRules();

    void save() throws IOException;

}
