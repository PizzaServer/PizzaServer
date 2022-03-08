package io.github.pizzaserver.format.data;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;

public class LevelData implements Cloneable {

    // world specific
    private boolean commandsEnabled;
    private long currentTick;
    private boolean hasBeenLoadedInCreative;
    private boolean hasLockedResourcePack;
    private boolean hasLockedBehaviorPack;
    private NbtMap experiments;    // TODO: Implement actual experiments object once you figure out experiments
    private boolean forceGamemode;
    private boolean immutable;
    private boolean isConfirmedPlatformLockedContent;
    private boolean isFromWorldTemplate;
    private boolean isFromLockedTemplate;
    private boolean isMultiplayerGame;
    private boolean isSingleUseWorld;
    private boolean isWorldTemplateOptionsLocked;
    private boolean lanBroadcast;
    private boolean lanBroadcastIntent;
    private boolean multiplayerGameIntent;
    private int platformBroadcastIntent;
    private boolean requiresCopiedPackRemovalCheck;
    private int serverChunkTickRange;
    private boolean spawnOnlyV1Villagers;
    private int storageVersion;
    private PlayerAbilities playerAbilities = new PlayerAbilities();
    private boolean texturePacksRequired;
    private boolean useMsaGamerTagsOnly;
    private String worldName;
    private long worldStartCount;
    private int xboxLiveBroadcastIntent;

    // Edu
    private int eduOffer;
    private boolean isEduEnabled;

    // level specific
    private String biomeOverride;
    private boolean bonusChestEnabled;
    private boolean bonusChestSpawned;
    private boolean centerMapsToOrigin;
    private int defaultGamemode;
    private int difficulty;
    private String flatWorldLayers;
    private LevelGameRules gameRules;
    private float lightningLevel;
    private int lightningTime;
    private Vector3i limitedWorldCoordinates;
    private int limitedWorldWidth;
    private int netherScale;
    private float rainLevel;
    private int rainTime;
    private long seed;
    private Vector3i spawnCoordinates;
    private boolean startWithMapEnabled;
    private long time;
    private int worldType;

    // metrics
    private String baseGameVersion;
    private String inventoryVersion;
    private long lastPlayed;
    private int[] minimumCompatibleClientVersion = new int[0];
    private int[] lastOpenedWithVersion = new int[0];
    private int platform;
    private int protocol;
    private String prid;


    public boolean isCommandsEnabled() {
        return this.commandsEnabled;
    }

    public void setCommandsEnabled(boolean enabled) {
        this.commandsEnabled = enabled;
    }

    public long getCurrentTick() {
        return this.currentTick;
    }

    public void setCurrentTick(long currentTick) {
        this.currentTick = currentTick;
    }

    public boolean hasBeenLoadedInCreative() {
        return this.hasBeenLoadedInCreative;
    }

    public void setHasBeenLoadedInCreative(boolean loaded) {
        this.hasBeenLoadedInCreative = loaded;
    }

    public boolean hasLockedResourcePack() {
        return this.hasLockedResourcePack;
    }

    public void setHasLockedResourcePack(boolean hasLocked) {
        this.hasLockedResourcePack = hasLocked;
    }

    public boolean hasLockedBehaviorPack() {
        return this.hasLockedBehaviorPack;
    }

    public void setHasLockedBehaviorPack(boolean hasLocked) {
        this.hasLockedBehaviorPack = hasLocked;
    }

    public NbtMap getExperiments() {
        return this.experiments;
    }

    public void setExperiments(NbtMap experiments) {
        this.experiments = experiments;
    }

    public boolean isForcedGamemode() {
        return this.forceGamemode;
    }

    public void setForcedGamemode(boolean forced) {
        this.forceGamemode = forced;
    }

    public boolean isImmutable() {
        return this.immutable;
    }

    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    public boolean isConfirmedPlatformLockedContent() {
        return this.isConfirmedPlatformLockedContent;
    }

    public void setConfirmedPlatformLockedContent(boolean locked) {
        this.isConfirmedPlatformLockedContent = locked;
    }

    public boolean isFromWorldTemplate() {
        return this.isFromWorldTemplate;
    }

    public void setFromWorldTemplate(boolean fromWorldTemplate) {
        this.isFromWorldTemplate = fromWorldTemplate;
    }

    public boolean isFromLockedTemplate() {
        return this.isFromLockedTemplate;
    }

    public void setFromLockedTemplate(boolean fromLockedTemplate) {
        this.isFromLockedTemplate = fromLockedTemplate;
    }

    public boolean isMultiplayerGame() {
        return this.isMultiplayerGame;
    }

    public void setIsMultiplayerGame(boolean multiplayerGame) {
        this.isMultiplayerGame = multiplayerGame;
    }

    public boolean isSingleUseWorld() {
        return this.isSingleUseWorld;
    }

    public void setIsSingleUseWorld(boolean singleUse) {
        this.isSingleUseWorld = singleUse;
    }

    public boolean isWorldTemplateOptionsLocked() {
        return this.isWorldTemplateOptionsLocked;
    }

    public void setIsWorldTemplateOptionsLocked(boolean locked) {
        this.isWorldTemplateOptionsLocked = locked;
    }

    public boolean isLanBroadcast() {
        return this.lanBroadcast;
    }

    public void setLanBroadcast(boolean broadcast) {
        this.lanBroadcast = broadcast;
    }

    public boolean isLanBroadcastIntent() {
        return this.lanBroadcastIntent;
    }

    public void setLanBroadcastIntent(boolean intent) {
        this.lanBroadcastIntent = intent;
    }

    public boolean isMultiplayerGameIntent() {
        return this.multiplayerGameIntent;
    }

    public void setMultiplayerGameIntent(boolean multiplayerGameIntent) {
        this.multiplayerGameIntent = multiplayerGameIntent;
    }

    public int getPlatformBroadcastIntent() {
        return this.platformBroadcastIntent;
    }

    public void setPlatformBroadcastIntent(int intent) {
        this.platformBroadcastIntent = intent;
    }

    public boolean requiresCopiedPackRemovalCheck() {
        return this.requiresCopiedPackRemovalCheck;
    }

    public void setRequiresCopiedPackRemovalCheck(boolean required) {
        this.requiresCopiedPackRemovalCheck = required;
    }

    public int getServerChunkTickRange() {
        return this.serverChunkTickRange;
    }

    public void setServerChunkTickRange(int tickRange) {
        this.serverChunkTickRange = tickRange;
    }

    public boolean spawnOnlyV1Villagers() {
        return this.spawnOnlyV1Villagers;
    }

    public void setSpawnOnlyV1Villagers(boolean spawnOnly) {
        this.spawnOnlyV1Villagers = spawnOnly;
    }

    public int getStorageVersion() {
        return this.storageVersion;
    }

    public void setStorageVersion(int version) {
        this.storageVersion = version;
    }

    public PlayerAbilities getPlayerAbilities() {
        return this.playerAbilities;
    }

    public void setPlayerAbilities(PlayerAbilities abilities) {
        this.playerAbilities = abilities;
    }

    public boolean isTexturePacksRequired() {
        return this.texturePacksRequired;
    }

    public void setTexturePacksRequired(boolean required) {
        this.texturePacksRequired = required;
    }

    public boolean useMsaGamerTagsOnly() {
        return this.useMsaGamerTagsOnly;
    }

    public void setUseMsaGamerTagsOnly(boolean useMsaGamerTagsOnly) {
        this.useMsaGamerTagsOnly = useMsaGamerTagsOnly;
    }

    public String getName() {
        return this.worldName;
    }

    public void setName(String worldName) {
        this.worldName = worldName;
    }

    public long getWorldStartCount() {
        return this.worldStartCount;
    }

    public void setWorldStartCount(long startCount) {
        this.worldStartCount = startCount;
    }

    public int getXboxLiveBroadcastIntent() {
        return this.xboxLiveBroadcastIntent;
    }

    public void setXboxLiveBroadcastIntent(int intent) {
        this.xboxLiveBroadcastIntent = intent;
    }

    public int getEduOffer() {
        return this.eduOffer;
    }

    public void setEduOffer(int offer) {
        this.eduOffer = offer;
    }

    public boolean isEduEnabled() {
        return this.isEduEnabled;
    }

    public void setEduEnabled(boolean enabled) {
        this.isEduEnabled = enabled;
    }

    public String getBiomeOverride() {
        return this.biomeOverride;
    }

    public void setBiomeOverride(String override) {
        this.biomeOverride = override;
    }

    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }

    public void setBonusChestEnabled(boolean enabled) {
        this.bonusChestEnabled = enabled;
    }

    public boolean isBonusChestSpawned() {
        return this.bonusChestSpawned;
    }

    public void setBonusChestSpawned(boolean spawned) {
        this.bonusChestSpawned = spawned;
    }

    public boolean isCenterMapsToOrigin() {
        return this.centerMapsToOrigin;
    }

    public void setCenterMapsToOrigin(boolean status) {
        this.centerMapsToOrigin = status;
    }

    public int getDefaultGamemode() {
        return this.defaultGamemode;
    }

    public void setDefaultGamemode(int defaultGamemode) {
        this.defaultGamemode = defaultGamemode;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getFlatWorldLayers() {
        return this.flatWorldLayers;
    }

    public void setFlatWorldLayers(String layers) {
        this.flatWorldLayers = layers;
    }

    public LevelGameRules getGameRules() {
        return this.gameRules;
    }

    public void setGameRules(LevelGameRules gameRules) {
        this.gameRules = gameRules;
    }

    public float getLightningLevel() {
        return this.lightningLevel;
    }

    public void setLightningLevel(float level) {
        this.lightningLevel = level;
    }

    public int getLightningTime() {
        return this.lightningTime;
    }

    public void setLightningTime(int time) {
        this.lightningTime = time;
    }

    public Vector3i getLimitedWorldCoordinates() {
        return this.limitedWorldCoordinates;
    }

    public void setLimitedWorldCoordinates(Vector3i limitedWorldCoordinates) {
        this.limitedWorldCoordinates = limitedWorldCoordinates;
    }

    public int getLimitedWorldWidth() {
        return this.limitedWorldWidth;
    }

    public void setLimitedWorldWidth(int width) {
        this.limitedWorldWidth = width;
    }

    public int getNetherScale() {
        return this.netherScale;
    }

    public void setNetherScale(int scale) {
        this.netherScale = scale;
    }

    public float getRainLevel() {
        return this.rainLevel;
    }

    public void setRainLevel(float rainLevel) {
        this.rainLevel = rainLevel;
    }

    public int getRainTime() {
        return this.rainTime;
    }

    public void setRainTime(int rainTime) {
        this.rainTime = rainTime;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public Vector3i getWorldSpawn() {
        return this.spawnCoordinates;
    }

    public void setWorldSpawn(Vector3i coordinates) {
        this.spawnCoordinates = coordinates;
    }

    public boolean startWithMapEnabled() {
        return this.startWithMapEnabled;
    }

    public void setStartWithMapEnabled(boolean enabled) {
        this.startWithMapEnabled = enabled;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getWorldType() {
        return this.worldType;
    }

    public void setWorldType(int worldType) {
        this.worldType = worldType;
    }

    public String getBaseGameVersion() {
        return this.baseGameVersion;
    }

    public void setBaseGameVersion(String baseGameVersion) {
        this.baseGameVersion = baseGameVersion;
    }

    public String getInventoryVersion() {
        return this.inventoryVersion;
    }

    public void setInventoryVersion(String inventoryVersion) {
        this.inventoryVersion = inventoryVersion;
    }

    public long getLastPlayed() {
        return this.lastPlayed;
    }

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public int[] getMinimumCompatibleClientVersion() {
        return this.minimumCompatibleClientVersion;
    }

    public void setMinimumCompatibleClientVersion(int[] version) {
        this.minimumCompatibleClientVersion = version;
    }

    public int[] getLastOpenedWithVersion() {
        return this.lastOpenedWithVersion;
    }

    public void setLastOpenedWithVersion(int[] version) {
        this.lastOpenedWithVersion = version;
    }

    public int getPlatform() {
        return this.platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getProtocol() {
        return this.protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getPrid() {
        return this.prid;
    }

    public void setPrid(String prid) {
        this.prid = prid;
    }

    @Override
    public LevelData clone() {
        try {
            return (LevelData) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError("Clone threw exception");
        }
    }

}
