package io.github.pizzaserver.format.api;

import com.nukkitx.math.vector.Vector3i;

public interface LevelData {

    String getName();

    void setName(String name);

    int getDefaultGamemode();

    void setDefaultGamemode(int gamemode);

    int getDifficulty();

    void setDifficulty(int difficulty);

    LevelGameRules getGameRules();

    void setGameRules(LevelGameRules rules);

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
