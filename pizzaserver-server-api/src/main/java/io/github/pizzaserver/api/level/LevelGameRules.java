package io.github.pizzaserver.api.level;

public interface LevelGameRules {

    boolean isDaylightCycleEnabled();

    void setDaylightCycleEnabled(boolean enabled);

    boolean isEntityDropsEnabled();

    void setEntityDropsEnabled(boolean enabled);

    boolean isFireTickEnabled();

    void setFireTickEnabled(boolean enabled);

    boolean isMobLootEnabled();

    void setMobLootEnabled(boolean enabled);

    boolean isMobSpawningEnabled();

    void setMobSpawningEnabled(boolean enabled);

    boolean isTileDropsEnabled();

    void setTileDropsEnabled(boolean enabled);

    boolean isWeatherCycleEnabled();

    void setWeatherCycleEnabled(boolean enabled);

    boolean isDrowningDamageEnabled();

    void setDrowningDamageEnabled(boolean enabled);

    boolean isFallDamageEnabled();

    void setFallDamageEnabled(boolean enabled);

    boolean isFireDamageEnabled();

    void setFireDamageEnabled(boolean enabled);

    boolean isKeepInventoryEnabled();

    void setKeepInventoryEnabled(boolean enabled);

    boolean isMobGriefingEnabled();

    void setMobGriefingEnabled(boolean enabled);

    boolean isPVPEnabled();

    void setPVPEnabled(boolean enabled);

    boolean isShowCoordinatesEnabled();

    void setShowCoordinatesEnabled(boolean enabled);

    boolean isNaturalRegenerationEnabled();

    void setNaturalRegenerationEnabled(boolean enabled);

    boolean isTNTExplosionEnabled();

    void setTNTExplosionEnabled(boolean enabled);

    boolean isSendCommandFeedbackEnabled();

    void setCommandFeedbackEnabled(boolean enabled);

    int getMaxCommandChainLength();

    void setMaxCommandChainLength(int length);

    boolean isInsomniaEnabled();

    void setInsomniaEnabled(boolean enabled);

    boolean isCommandBlocksEnabled();

    void setCommandBlocksEnabled(boolean enabled);

    int getRandomTickSpeed();

    void setRandomTickSpeed(int speed);

    boolean isImmediateRespawnEnabled();

    void setImmediateRespawnEnabled(boolean enabled);

    boolean isShowDeathMessagesEnabled();

    void setShowDeathMessagesEnabled(boolean enabled);

    int getSpawnRadius();

    void setSpawnRadius(int radius);

    boolean isShowTagsEnabled();

    void setShowTagsEnabled(boolean enabled);

    boolean isFreezeDamageEnabled();

    void setFreezeDamageEnabled(boolean enabled);

    boolean isRespawnBlockExplosionEnabled();

    void setRespawnBlockExplosionEnabled(boolean enabled);

    boolean isShowBorderEffectsEnabled();

    void setShowBorderEffectsEnabled(boolean enabled);

}
