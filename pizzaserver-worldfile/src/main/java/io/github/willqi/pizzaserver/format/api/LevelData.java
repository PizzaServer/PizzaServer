package io.github.willqi.pizzaserver.format.api;

import io.github.willqi.pizzaserver.commons.server.Difficulty;
import io.github.willqi.pizzaserver.commons.server.Gamemode;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.commons.world.gamerules.GameRule;
import io.github.willqi.pizzaserver.commons.world.gamerules.GameRuleID;

import java.util.Map;

public interface LevelData {

    String getName();

    void setName(String name);

    Gamemode getDefaultGamemode();

    void setDefaultGamemode(Gamemode gamemode);

    Difficulty getDifficulty();

    void setDifficulty(Difficulty difficulty);

    Map<GameRuleID, GameRule<?>> getGameRules();

    void setGameRules(Map<GameRuleID, GameRule<?>> gameRules);

    Vector3i getWorldSpawn();

    void setWorldSpawn(Vector3i worldSpawn);

    long getTime();

    void setTime(long time);

    int getRainTime();

    void setRainTime(int rainTime);

    float getRainLevel();

    void setRainLevel(float rainLevel);

    int getLightningTime();

    void setLightningTime(int lightningTime);

    float getLightningLevel();

    void setLightningLevel(float lightningLevel);

}
