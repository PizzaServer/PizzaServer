package io.github.pizzaserver.format.api;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.format.api.data.LevelGameRules;
import io.github.pizzaserver.format.api.data.PlayerAbilities;

public interface LevelData {

    boolean isImmutable();

    void setImmutable(boolean immutable);

    boolean isConfirmedPlatformLockedContent();

    void setConfirmedPlatformLockedContent(boolean locked);

    boolean isFromWorldTemplate();

    void setFromWorldTemplate(boolean fromWorldTemplate);

    boolean isFromLockedTemplate();

    void setFromLockedTemplate(boolean fromLockedTemplate);

    boolean isMultiplayerGame();

    void setIsMultiplayerGame(boolean multiplayerGame);

    boolean isSingleUseWorld();

    void setIsSingleUseWorld(boolean singleUse);

    boolean isWorldTemplateOptionsLocked();

    void setIsWorldTemplateOptionsLocked(boolean locked);

    boolean isLanBroadcast();

    void setLanBroadcast(boolean broadcast);

    boolean isLanBroadcastIntent();

    void setLanBroadcastIntent(boolean intent);

    boolean isMultiplayerGameIntent();

    void setMultiplayerGameIntent(boolean multiplayerGameIntent);

    int getPlatformBroadcastIntent();

    void setPlatformBroadcastIntent(int intent);

    boolean requiresCopiedPackRemovalCheck();

    void setRequiresCopiedPackRemovalCheck(boolean required);

    int getServerChunkTickRange();

    void setServerChunkTickRange(int tickRange);

    boolean spawnOnlyV1Villagers();

    void setSpawnOnlyV1Villagers(boolean spawnOnly);

    int getStorageVersion();

    void setStorageVersion(int version);

    PlayerAbilities getPlayerAbilities();

    void setPlayerAbilities(PlayerAbilities abilities);

    boolean isTexturePacksRequired();

    void setTexturePacksRequired(boolean required);

    boolean useMsaGamerTagsOnly();

    void setUseMsaGamerTagsOnly(boolean useMsaGamerTagsOnly);

    String getName();

    void setName(String name);

    boolean isCommandsEnabled();

    void setCommandsEnabled(boolean enabled);

    long getCurrentTick();

    void setCurrentTick(long tick);

    boolean hasBeenLoadedInCreative();

    void setHasBeenLoadedInCreative(boolean loaded);

    boolean hasLockedResourcePack();

    void setHasLockedResourcePack(boolean hasLocked);

    boolean hasLockedBehaviorPack();

    void setHasLockedBehaviorPack(boolean hasLocked);

    NbtMap getExperiments();

    void setExperiments(NbtMap experiments);

    boolean isForcedGamemode();

    void setForcedGamemode(boolean forced);

    long getWorldStartCount();

    void setWorldStartCount(long startCount);

    int getXboxLiveBroadcastIntent();

    void setXboxLiveBroadcastIntent(int intent);

    int getEduOffer();

    void setEduOffer(int offer);

    boolean isEduEnabled();

    void setEduEnabled(boolean enabled);

    String getBiomeOverride();

    void setBiomeOverride(String override);

    boolean isBonusChestEnabled();

    void setBonusChestEnabled(boolean enabled);

    boolean isBonusChestSpawned();

    void setBonusChestSpawned(boolean spawned);

    boolean isCenterMapsToOrigin();

    void setCenterMapsToOrigin(boolean status);

    int getDefaultGamemode();

    void setDefaultGamemode(int gamemode);

    int getDifficulty();

    void setDifficulty(int difficulty);

    String getFlatWorldLayers();

    void setFlatWorldLayers(String layers);

    LevelGameRules getGameRules();

    void setGameRules(LevelGameRules rules);

    long getSeed();

    void setSeed(long seed);

    Vector3i getWorldSpawn();

    void setWorldSpawn(Vector3i worldSpawn);

    boolean startWithMapEnabled();

    void setStartWithMapEnabled(boolean enabled);

    long getTime();

    void setTime(long time);

    int getRainTime();

    void setRainTime(int rainTime);

    Vector3i getLimitedWorldCoordinates();

    void setLimitedWorldCoordinates(Vector3i limitedWorldCoordinates);

    int getLimitedWorldWidth();

    void setLimitedWorldWidth(int width);

    int getNetherScale();

    void setNetherScale(int scale);

    float getRainLevel();

    void setRainLevel(float rainLevel);

    int getLightningTime();

    void setLightningTime(int lightningTime);

    float getLightningLevel();

    void setLightningLevel(float lightningLevel);

    int getWorldType();

    void setWorldType(int worldType);

    String getBaseGameVersion();

    void setBaseGameVersion(String baseGameVersion);

    String getInventoryVersion();

    void setInventoryVersion(String inventoryVersion);

    long getLastPlayed();

    void setLastPlayed(long lastPlayed);

    int[] getMinimumCompatibleClientVersion();

    void setMinimumCompatibleClientVersion(int[] version);

    int[] getLastOpenedWithVersion();

    void setLastOpenedWithVersion(int[] version);

    int getPlatform();

    void setPlatform(int platform);

    int getProtocol();

    void setProtocol(int protocol);

    String getPrid();

    void setPrid(String prid);

}
