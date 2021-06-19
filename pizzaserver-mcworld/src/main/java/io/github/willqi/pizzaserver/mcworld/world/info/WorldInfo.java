package io.github.willqi.pizzaserver.mcworld.world.info;

import io.github.willqi.pizzaserver.commons.server.Difficulty;
import io.github.willqi.pizzaserver.commons.server.Gamemode;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.commons.world.WorldType;
import io.github.willqi.pizzaserver.commons.world.gamerules.GameRule;
import io.github.willqi.pizzaserver.commons.world.gamerules.GameRuleId;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.HashMap;
import java.util.Map;

/**
 * Representative of the information in the level.dat file
 */
public class WorldInfo implements Cloneable {

    // world specific
    private boolean commandsEnabled;
    private long currentTick;
    private boolean hasBeenLoadedInCreative;
    private boolean hasLockedResourcePack;
    private boolean hasLockedBehaviorPack;
    private NBTCompound experiments;    // TODO: Implement actual experiments object once you figure out experiments
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
    private String biomeOverride;       // TODO: What is this?
    private boolean bonusChestEnabled;
    private boolean bonusChestSpawned;
    private boolean centerMapsToOrigin;
    private Gamemode defaultGamemode;
    private Difficulty difficulty;
    private String flatWorldLayers;
    private Map<GameRuleId, GameRule> gameRules = new HashMap<>();
    private float lightningLevel;
    private int lightningTime;
    private Vector3i limitedWorldCoordinates;
    private Vector3i limitedWorldDimensions;
    private int netherScale;
    private float rainLevel;
    private int rainTime;
    private int seed;
    private Vector3i spawnCoordinates;
    private boolean startWithMapEnabled;
    private long time;
    private WorldType worldType;

    // metrics
    private String baseGameVersion;
    private String inventoryVersion;
    private long lastPlayed;
    private int[] minimumCompatibleClientVersion = new int[0];
    private int[] lastOpenedWithVersion = new int[0];
    private int platform;   // TODO: Maybe game platform? Investigate with mobile world.
    private int protocol;
    private String prid;    // TODO: What is this?


    public boolean isCommandsEnabled() {
        return this.commandsEnabled;
    }

    public WorldInfo setCommandsEnabled(boolean enabled) {
        this.commandsEnabled = enabled;
        return this;
    }

    public long getCurrentTick() {
        return this.currentTick;
    }

    public WorldInfo setCurrentTick(long currentTick) {
        this.currentTick = currentTick;
        return this;
    }

    public boolean hasBeenLoadedInCreative() {
        return this.hasBeenLoadedInCreative;
    }

    public WorldInfo setHasBeenLoadedInCreative(boolean loaded) {
        this.hasBeenLoadedInCreative = loaded;
        return this;
    }

    public boolean hasLockedResourcePack() {
        return this.hasLockedResourcePack;
    }

    public WorldInfo setHasLockedResourcePack(boolean hasLocked) {
        this.hasLockedResourcePack = hasLocked;
        return this;
    }

    public boolean isHasLockedBehaviorPack() {
        return this.hasLockedBehaviorPack;
    }

    public WorldInfo setHasLockedBehaviorPack(boolean hasLocked) {
        this.hasLockedBehaviorPack = hasLocked;
        return this;
    }

    /**
     * This is subject to change and will change when experiments are explored
     * @return experiments
     */
    public NBTCompound getExperiments() {
        return this.experiments;
    }

    /**
     * This is subject to change and will change when experiments are explored
     * @param experiments
     * @return self for chaining
     */
    public WorldInfo setExperiments(NBTCompound experiments) {
        this.experiments = experiments;
        return this;
    }

    public boolean isForcedGamemode() {
        return this.forceGamemode;
    }

    public WorldInfo setForceGamemode(boolean forced) {
        this.forceGamemode = forced;
        return this;
    }

    public boolean isImmutable() {
        return this.immutable;
    }

    public WorldInfo setImmutable(boolean immutable) {
        this.immutable = immutable;
        return this;
    }

    public boolean isConfirmedPlatformLockedContent() {
        return this.isConfirmedPlatformLockedContent;
    }

    public WorldInfo setConfirmedPlatformLockedContent(boolean locked) {
        this.isConfirmedPlatformLockedContent = locked;
        return this;
    }

    public boolean isFromWorldTemplate() {
        return this.isFromWorldTemplate;
    }

    public WorldInfo setFromWorldTemplate(boolean fromWorldTemplate) {
        this.isFromWorldTemplate = fromWorldTemplate;
        return this;
    }

    public boolean isFromLockedTemplate() {
        return this.isFromLockedTemplate;
    }

    public WorldInfo setFromLockedTemplate(boolean fromLockedTemplate) {
        this.isFromLockedTemplate = fromLockedTemplate;
        return this;
    }

    public boolean isMultiplayerGame() {
        return this.isMultiplayerGame;
    }

    public WorldInfo setIsMultiplayerGame(boolean multiplayerGame) {
        this.isMultiplayerGame = multiplayerGame;
        return this;
    }

    public boolean isSingleUseWorld() {
        return this.isSingleUseWorld;
    }

    public WorldInfo setIsSingleUseWorld(boolean singleUse) {
        this.isSingleUseWorld = singleUse;
        return this;
    }

    public boolean isWorldTemplateOptionsLocked() {
        return this.isWorldTemplateOptionsLocked;
    }

    public WorldInfo setIsWorldTemplateOptionsLocked(boolean locked) {
        this.isWorldTemplateOptionsLocked = locked;
        return this;
    }

    public boolean isLanBroadcast() {
        return this.lanBroadcast;
    }

    public WorldInfo setLanBroadcast(boolean broadcast) {
        this.lanBroadcast = broadcast;
        return this;
    }

    public boolean isLanBroadcastIntent() {
        return this.lanBroadcastIntent;
    }

    public WorldInfo setLanBroadcastIntent(boolean intent) {
        this.lanBroadcastIntent = intent;
        return this;
    }

    public boolean isMultiplayerGameIntent() {
        return this.multiplayerGameIntent;
    }

    public WorldInfo setMultiplayerGameIntent(boolean multiplayerGameIntent) {
        this.multiplayerGameIntent = multiplayerGameIntent;
        return this;
    }

    public int getPlatformBroadcastIntent() {
        return this.platformBroadcastIntent;
    }

    public WorldInfo setPlatformBroadcastIntent(int intent) {
        this.platformBroadcastIntent = intent;
        return this;
    }

    public boolean requiresCopiedPackRemovalCheck() {
        return this.requiresCopiedPackRemovalCheck;
    }

    public WorldInfo setRequiresCopiedPackRemovalCheck(boolean required) {
        this.requiresCopiedPackRemovalCheck = required;
        return this;
    }

    public int getServerChunkTickRange() {
        return this.serverChunkTickRange;
    }

    public WorldInfo setServerChunkTickRange(int tickRange) {
        this.serverChunkTickRange = tickRange;
        return this;
    }

    public boolean spawnOnlyV1Villagers() {
        return this.spawnOnlyV1Villagers;
    }

    public WorldInfo setSpawnOnlyV1Villagers(boolean spawnOnly) {
        this.spawnOnlyV1Villagers = spawnOnly;
        return this;
    }

    public int getStorageVersion() {
        return this.storageVersion;
    }

    public WorldInfo setStorageVersion(int version) {
        this.storageVersion = version;
        return this;
    }

    public PlayerAbilities getPlayerAbilities() {
        return this.playerAbilities;
    }

    public WorldInfo setPlayerAbilities(PlayerAbilities abilities) {
        this.playerAbilities = abilities;
        return this;
    }

    public boolean isTexturePacksRequired() {
        return this.texturePacksRequired;
    }

    public WorldInfo setTexturePacksRequired(boolean required) {
        this.texturePacksRequired = required;
        return this;
    }

    public boolean useMsaGamerTagsOnly() {
        return this.useMsaGamerTagsOnly;
    }

    public WorldInfo setUseMsaGamerTagsOnly(boolean useMsaGamerTagsOnly) {
        this.useMsaGamerTagsOnly = useMsaGamerTagsOnly;
        return this;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public WorldInfo setWorldName(String worldName) {
        this.worldName = worldName;
        return this;
    }

    public long getWorldStartCount() {
        return this.worldStartCount;
    }

    public WorldInfo setWorldStartCount(long startCount) {
        this.worldStartCount = startCount;
        return this;
    }

    public int getXboxLiveBroadcastIntent() {
        return this.xboxLiveBroadcastIntent;
    }

    public WorldInfo setXboxLiveBroadcastIntent(int intent) {
        this.xboxLiveBroadcastIntent = intent;
        return this;
    }


    public int getEduOffer() {
        return this.eduOffer;
    }

    public WorldInfo setEduOffer(int offer) {
        this.eduOffer = offer;
        return this;
    }

    public boolean isEduEnabled() {
        return this.isEduEnabled;
    }

    public WorldInfo setEduEnabled(boolean enabled) {
        this.isEduEnabled = enabled;
        return this;
    }


    public String getBiomeOverride() {
        return this.biomeOverride;
    }

    public WorldInfo setBiomeOverride(String override) {
        this.biomeOverride = override;
        return this;
    }

    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }

    public WorldInfo setBonusChestEnabled(boolean enabled) {
        this.bonusChestEnabled = enabled;
        return this;
    }

    public boolean isBonusChestSpawned() {
        return this.bonusChestSpawned;
    }

    public WorldInfo setBonusChestSpawned(boolean spawned) {
        this.bonusChestSpawned = spawned;
        return this;
    }

    public boolean isCenterMapsToOrigin() {
        return this.centerMapsToOrigin;
    }

    public WorldInfo setCenterMapsToOrigin(boolean status) {
        this.centerMapsToOrigin = status;
        return this;
    }

    public Gamemode getDefaultGamemode() {
        return this.defaultGamemode;
    }

    public WorldInfo setDefaultGamemode(Gamemode defaultGamemode) {
        this.defaultGamemode = defaultGamemode;
        return this;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public WorldInfo setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public String getFlatWorldLayers() {
        return this.flatWorldLayers;
    }

    public WorldInfo setFlatWorldLayers(String layers) {
        this.flatWorldLayers = flatWorldLayers;
        return this;
    }

    public Map<GameRuleId, GameRule> getGameRules() {
        return this.gameRules;
    }

    public WorldInfo setGameRules(Map<GameRuleId, GameRule> gameRules) {
        this.gameRules = gameRules;
        return this;
    }

    public float getLightningLevel() {
        return this.lightningLevel;
    }

    public WorldInfo setLightningLevel(float level) {
        this.lightningLevel = level;
        return this;
    }

    public int getLightningTime() {
        return this.lightningTime;
    }

    public WorldInfo setLightningTime(int time) {
        this.lightningTime = time;
        return this;
    }

    public Vector3i getLimitedWorldCoordinates() {
        return this.limitedWorldCoordinates;
    }

    public WorldInfo setLimitedWorldCoordinates(Vector3i limitedWorldCoordinates) {
        this.limitedWorldCoordinates = limitedWorldCoordinates;
        return this;
    }

    public Vector3i getLimitedWorldDimensions() {
        return this.limitedWorldDimensions;
    }

    public WorldInfo setLimitedWorldDimensions(Vector3i dimensions) {
        this.limitedWorldDimensions = dimensions;
        return this;
    }

    public int getNetherScale() {
        return this.netherScale;
    }

    public WorldInfo setNetherScale(int scale) {
        this.netherScale = scale;
        return this;
    }

    public float getRainLevel() {
        return this.rainLevel;
    }

    public WorldInfo setRainLevel(float rainLevel) {
        this.rainLevel = rainLevel;
        return this;
    }

    public int getRainTime() {
        return this.rainTime;
    }

    public WorldInfo setRainTime(int rainTime) {
        this.rainTime = rainTime;
        return this;
    }

    public int getSeed() {
        return this.seed;
    }

    public WorldInfo setSeed(int seed) {
        this.seed = seed;
        return this;
    }

    public Vector3i getSpawnCoordinates() {
        return this.spawnCoordinates;
    }

    public WorldInfo setSpawnCoordinates(Vector3i coordinates) {
        this.spawnCoordinates = coordinates;
        return this;
    }

    public boolean startWithMapEnabled() {
        return this.startWithMapEnabled;
    }

    public WorldInfo setStartWithMapEnabled(boolean enabled) {
        this.startWithMapEnabled = enabled;
        return this;
    }

    public long getTime() {
        return this.time;
    }

    public WorldInfo setTime(long time) {
        this.time = time;
        return this;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public WorldInfo setWorldType(WorldType worldType) {
        this.worldType = worldType;
        return this;
    }


    public String getBaseGameVersion() {
        return this.baseGameVersion;
    }

    public WorldInfo setBaseGameVersion(String baseGameVersion) {
        this.baseGameVersion = baseGameVersion;
        return this;
    }

    public String getInventoryVersion() {
        return this.inventoryVersion;
    }

    public WorldInfo setInventoryVersion(String inventoryVersion) {
        this.inventoryVersion = inventoryVersion;
        return this;
    }

    public long getLastPlayed() {
        return this.lastPlayed;
    }

    public WorldInfo setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
        return this;
    }

    public int[] getMinimumCompatibleClientVersion() {
        return this.minimumCompatibleClientVersion;
    }

    public WorldInfo setMinimumCompatibleClientVersion(int[] version) {
        this.minimumCompatibleClientVersion = version;
        return this;
    }

    public int[] getLastOpenedWithVersion() {
        return this.lastOpenedWithVersion;
    }

    public WorldInfo setLastOpenedWithVersion(int[] version) {
        this.lastOpenedWithVersion = version;
        return this;
    }

    public int getPlatform() {
        return this.platform;
    }

    public WorldInfo setPlatform(int platform) {
        this.platform = platform;
        return this;
    }

    public int getProtocol() {
        return this.protocol;
    }

    public WorldInfo setProtocol(int protocol) {
        this.protocol = protocol;
        return this;
    }

    public String getPrid() {
        return this.prid;
    }

    public WorldInfo setPrid(String prid) {
        this.prid = prid;
        return this;
    }


    @Override
    public WorldInfo clone() {
        try {
            return (WorldInfo)super.clone();
        } catch (CloneNotSupportedException exception) {
            return null;
        }
    }

}
