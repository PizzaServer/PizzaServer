package io.github.pizzaserver.format.api.provider.mcworld.info;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import com.nukkitx.nbt.NbtUtils;
import io.github.pizzaserver.format.api.LevelData;
import io.github.pizzaserver.format.api.data.LevelGameRules;
import io.github.pizzaserver.format.api.data.PlayerAbilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Representative of the information in the level.dat file
 */
public class MCWorldLevelData implements LevelData, Cloneable {

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
    private String biomeOverride;       // TODO: What is this?
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
    private int platform;   // TODO: Maybe game platform? Investigate with mobile world.
    private int protocol;
    private String prid;    // TODO: What is this?


    public MCWorldLevelData() {}

    public MCWorldLevelData(File levelDatFile) throws IOException {

        try (InputStream levelDatStream = new FileInputStream(levelDatFile)) {
            levelDatStream.skip(8); // TODO: These 8 bytes are important when writing the level.dat file (header)
            try (NBTInputStream inputStream = NbtUtils.createReaderLE(levelDatStream)) {
                NbtMap compound = (NbtMap) inputStream.readTag();

                this.setCommandsEnabled(compound.getBoolean("commandsEnabled"));
                this.setCurrentTick(compound.getLong("currentTick"));
                this.setHasBeenLoadedInCreative(compound.getBoolean("hasBeenLoadedInCreative"));
                this.setHasLockedResourcePack(compound.getBoolean("hasLockedResourcePack"));
                this.setHasLockedBehaviorPack(compound.getBoolean("hasLockedBehaviorPack"));
                this.setExperiments(compound.getCompound("experiments"));
                this.setForcedGamemode(compound.getBoolean("ForceGameType"));
                this.setImmutable(compound.getBoolean("immutableWorld"));
                this.setConfirmedPlatformLockedContent(compound.getBoolean("ConfirmedPlatformLockedContent"));
                this.setFromWorldTemplate(compound.getBoolean("isFromWorldTemplate"));
                this.setFromLockedTemplate(compound.getBoolean("isFromLockedTemplate"));
                this.setIsMultiplayerGame(compound.getBoolean("MultiplayerGame"));
                this.setIsSingleUseWorld(compound.getBoolean("isSingleUseWorld"));
                this.setIsWorldTemplateOptionsLocked(compound.getBoolean("isWorldTemplateOptionLocked"));
                this.setLanBroadcast(compound.getBoolean("LANBroadcast"));
                this.setLanBroadcastIntent(compound.getBoolean("LANBroadcastIntent"));
                this.setMultiplayerGameIntent(compound.getBoolean("MultiplayerGameIntent"));
                this.setPlatformBroadcastIntent(compound.getInt("PlatformBroadcastIntent"));
                this.setRequiresCopiedPackRemovalCheck(compound.getBoolean("requiresCopiedPackRemovalCheck"));
                this.setServerChunkTickRange(compound.getInt("serverChunkTickRange"));
                this.setSpawnOnlyV1Villagers(compound.getBoolean("SpawnV1Villagers"));
                this.setStorageVersion(compound.getInt("StorageVersion"));
                this.setTexturePacksRequired(compound.getBoolean("texturePacksRequired"));
                this.setUseMsaGamerTagsOnly(compound.getBoolean("useMsaGamertagsOnly"));
                this.setName(compound.getString("LevelName"));
                this.setWorldStartCount(compound.getLong("worldStartCount"));
                this.setXboxLiveBroadcastIntent(compound.getInt("XBLBroadcastIntent"));
                this.setEduOffer(compound.getInt("eduOffer"));
                this.setEduEnabled(compound.getBoolean("educationFeaturesEnabled"));
                this.setBiomeOverride(compound.getString("BiomeOverride"));
                this.setBonusChestEnabled(compound.getBoolean("bonusChestEnabled"));
                this.setBonusChestSpawned(compound.getBoolean("bonusChestSpawned"));
                this.setCenterMapsToOrigin(compound.getBoolean("CenterMapsToOrigin"));
                this.setDefaultGamemode(compound.getInt("GameType"));
                this.setDifficulty(compound.getInt("Difficulty"));
                this.setFlatWorldLayers(compound.getString("FlatWorldLayers"));
                this.setLightningLevel(compound.getFloat("lightningLevel"));
                this.setLightningTime(compound.getInt("lightningTime"));
                this.setLimitedWorldCoordinates(Vector3i.from(
                        compound.getInt("LimitedWorldOriginX"),
                        compound.getInt("LimitedWorldOriginY"),
                        compound.getInt("LimitedWorldOriginZ")));
                this.setLimitedWorldWidth(compound.getInt("limitedWorldWidth"));
                this.setNetherScale(compound.getInt("NetherScale"));
                this.setRainLevel(compound.getFloat("rainLevel"));
                this.setRainTime(compound.getInt("rainTime"));
                this.setSeed(compound.getLong("RandomSeed"));
                this.setWorldSpawn(Vector3i.from(
                        compound.getInt("SpawnX"),
                        compound.getInt("SpawnY"),
                        compound.getInt("SpawnZ")
                ));
                this.setStartWithMapEnabled(compound.getBoolean("startWithMapEnabled"));
                this.setTime(compound.getLong("Time"));
                this.setWorldType(compound.getInt("Generator"));
                this.setBaseGameVersion(compound.getString("baseGameVersion"));
                this.setInventoryVersion(compound.getString("InventoryVersion"));
                this.setLastPlayed(compound.getLong("LastPlayed"));
                this.setMinimumCompatibleClientVersion(compound.getList("MinimumCompatibleClientVersion", NbtType.INT).stream().mapToInt(Integer::intValue).toArray());
                this.setLastOpenedWithVersion(compound.getList("lastOpenedWithVersion", NbtType.INT).stream().mapToInt(Integer::intValue).toArray());
                this.setPlatform(compound.getInt("Platform"));
                this.setProtocol(compound.getInt("NetworkVersion"));
                this.setPrid(compound.getString("prid"));

                NbtMap abilities = compound.getCompound("abilities");
                this.setPlayerAbilities(
                        new PlayerAbilities()
                                .setCanAttackMobs(abilities.getBoolean("attackmobs"))
                                .setCanAttackPlayers(abilities.getBoolean("attackplayers"))
                                .setCanBuild(abilities.getBoolean("build"))
                                .setCanFly(abilities.getBoolean("mayfly"))
                                .setCanInstaBuild(abilities.getBoolean("instabuild"))
                                .setCanMine(abilities.getBoolean("mine"))
                                .setCanOpenContainers(abilities.getBoolean("opencontainers"))
                                .setCanTeleport(abilities.getBoolean("teleport"))
                                .setCanUseDoorsAndSwitches(abilities.getBoolean("doorsandswitches"))
                                .setFlySpeed(abilities.getFloat("flySpeed"))
                                .setIsFlying(abilities.getBoolean("flying"))
                                .setIsInvulnerable(abilities.getBoolean("invulnerable"))
                                .setIsOp(abilities.getBoolean("op"))
                                .setIsLightning(abilities.getBoolean("lightning"))
                                .setPermissionsLevel(abilities.getInt("permissionsLevel"))
                                .setPlayerPermissionsLevel(abilities.getInt("playerPermissionsLevel"))
                                .setWalkSpeed(abilities.getFloat("walkSpeed"))
                );

                LevelGameRules gameRules = new LevelGameRules();
                gameRules.setCommandBlockOutputEnabled(compound.getBoolean("commandblockoutput"));
                gameRules.setCommandBlocksEnabled(compound.getBoolean("commandblocksenabled"));
                gameRules.setDaylightCycle(compound.getBoolean("dodaylightcycle"));
                gameRules.setEntityDropsEnabled(compound.getBoolean("doentitydrops"));
                gameRules.setFireTickEnabled(compound.getBoolean("dofiretick"));
                gameRules.setImmediateRespawnEnabled(compound.getBoolean("doimmediaterespawn"));
                gameRules.setInsomniaEnabled(compound.getBoolean("doinsomnia"));
                gameRules.setMobLootEnabled(compound.getBoolean("domobloot"));
                gameRules.setMobSpawningEnabled(compound.getBoolean("domobspawning"));
                gameRules.setTileDropsEnabled(compound.getBoolean("dotiledrops"));
                gameRules.setWeatherCycleEnabled(compound.getBoolean("doweathercycle"));
                gameRules.setDrowningDamageEnabled(compound.getBoolean("drowningdamage"));
                gameRules.setFallDamageEnabled(compound.getBoolean("falldamage"));
                gameRules.setFireDamageEnabled(compound.getBoolean("firedamage"));
                gameRules.setKeepInventoryEnabled(compound.getBoolean("keepinventory"));
                gameRules.setMaxCommandChainLength(compound.getInt("maxcommandchainlength"));
                gameRules.setMobGriefingEnabled(compound.getBoolean("mobgriefing"));
                gameRules.setNaturalRegenerationEnabled(compound.getBoolean("naturalregeneration"));
                gameRules.setPVPEnabled(compound.getBoolean("pvp"));
                gameRules.setRandomTickSpeed(compound.getInt("randomtickspeed"));
                gameRules.setSendCommandFeedbackEnabled(compound.getBoolean("sendcommandfeedback"));
                gameRules.setShowCoordinatesEnabled(compound.getBoolean("showcoordinates"));
                gameRules.setShowDeathMessagesEnabled(compound.getBoolean("showdeathmessages"));
                gameRules.setShowItemTagsEnabled(compound.getBoolean("showtags"));
                gameRules.setSpawnRadius(compound.getInt("spawnradius"));
                gameRules.setTNTExplodesEnabled(compound.getBoolean("tntexplodes"));
                this.setGameRules(gameRules);
            }
        }
    }

    @Override
    public boolean isCommandsEnabled() {
        return this.commandsEnabled;
    }

    @Override
    public void setCommandsEnabled(boolean enabled) {
        this.commandsEnabled = enabled;
    }

    @Override
    public long getCurrentTick() {
        return this.currentTick;
    }

    @Override
    public void setCurrentTick(long currentTick) {
        this.currentTick = currentTick;
    }

    @Override
    public boolean hasBeenLoadedInCreative() {
        return this.hasBeenLoadedInCreative;
    }

    @Override
    public void setHasBeenLoadedInCreative(boolean loaded) {
        this.hasBeenLoadedInCreative = loaded;
    }

    @Override
    public boolean hasLockedResourcePack() {
        return this.hasLockedResourcePack;
    }

    @Override
    public void setHasLockedResourcePack(boolean hasLocked) {
        this.hasLockedResourcePack = hasLocked;
    }

    @Override
    public boolean hasLockedBehaviorPack() {
        return this.hasLockedBehaviorPack;
    }

    @Override
    public void setHasLockedBehaviorPack(boolean hasLocked) {
        this.hasLockedBehaviorPack = hasLocked;
    }

    @Override
    public NbtMap getExperiments() {
        return this.experiments;
    }

    @Override
    public void setExperiments(NbtMap experiments) {
        this.experiments = experiments;
    }

    @Override
    public boolean isForcedGamemode() {
        return this.forceGamemode;
    }

    @Override
    public void setForcedGamemode(boolean forced) {
        this.forceGamemode = forced;
    }

    @Override
    public boolean isImmutable() {
        return this.immutable;
    }

    @Override
    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    @Override
    public boolean isConfirmedPlatformLockedContent() {
        return this.isConfirmedPlatformLockedContent;
    }

    @Override
    public void setConfirmedPlatformLockedContent(boolean locked) {
        this.isConfirmedPlatformLockedContent = locked;
    }

    @Override
    public boolean isFromWorldTemplate() {
        return this.isFromWorldTemplate;
    }

    @Override
    public void setFromWorldTemplate(boolean fromWorldTemplate) {
        this.isFromWorldTemplate = fromWorldTemplate;
    }

    @Override
    public boolean isFromLockedTemplate() {
        return this.isFromLockedTemplate;
    }

    @Override
    public void setFromLockedTemplate(boolean fromLockedTemplate) {
        this.isFromLockedTemplate = fromLockedTemplate;
    }

    @Override
    public boolean isMultiplayerGame() {
        return this.isMultiplayerGame;
    }

    @Override
    public void setIsMultiplayerGame(boolean multiplayerGame) {
        this.isMultiplayerGame = multiplayerGame;
    }

    @Override
    public boolean isSingleUseWorld() {
        return this.isSingleUseWorld;
    }

    @Override
    public void setIsSingleUseWorld(boolean singleUse) {
        this.isSingleUseWorld = singleUse;
    }

    @Override
    public boolean isWorldTemplateOptionsLocked() {
        return this.isWorldTemplateOptionsLocked;
    }

    @Override
    public void setIsWorldTemplateOptionsLocked(boolean locked) {
        this.isWorldTemplateOptionsLocked = locked;
    }

    @Override
    public boolean isLanBroadcast() {
        return this.lanBroadcast;
    }

    @Override
    public void setLanBroadcast(boolean broadcast) {
        this.lanBroadcast = broadcast;
    }

    @Override
    public boolean isLanBroadcastIntent() {
        return this.lanBroadcastIntent;
    }

    @Override
    public void setLanBroadcastIntent(boolean intent) {
        this.lanBroadcastIntent = intent;
    }

    @Override
    public boolean isMultiplayerGameIntent() {
        return this.multiplayerGameIntent;
    }

    @Override
    public void setMultiplayerGameIntent(boolean multiplayerGameIntent) {
        this.multiplayerGameIntent = multiplayerGameIntent;
    }

    @Override
    public int getPlatformBroadcastIntent() {
        return this.platformBroadcastIntent;
    }

    @Override
    public void setPlatformBroadcastIntent(int intent) {
        this.platformBroadcastIntent = intent;
    }

    @Override
    public boolean requiresCopiedPackRemovalCheck() {
        return this.requiresCopiedPackRemovalCheck;
    }

    @Override
    public void setRequiresCopiedPackRemovalCheck(boolean required) {
        this.requiresCopiedPackRemovalCheck = required;
    }

    @Override
    public int getServerChunkTickRange() {
        return this.serverChunkTickRange;
    }

    @Override
    public void setServerChunkTickRange(int tickRange) {
        this.serverChunkTickRange = tickRange;
    }

    @Override
    public boolean spawnOnlyV1Villagers() {
        return this.spawnOnlyV1Villagers;
    }

    @Override
    public void setSpawnOnlyV1Villagers(boolean spawnOnly) {
        this.spawnOnlyV1Villagers = spawnOnly;
    }

    @Override
    public int getStorageVersion() {
        return this.storageVersion;
    }

    @Override
    public void setStorageVersion(int version) {
        this.storageVersion = version;
    }

    @Override
    public PlayerAbilities getPlayerAbilities() {
        return this.playerAbilities;
    }

    @Override
    public void setPlayerAbilities(PlayerAbilities abilities) {
        this.playerAbilities = abilities;
    }

    @Override
    public boolean isTexturePacksRequired() {
        return this.texturePacksRequired;
    }

    @Override
    public void setTexturePacksRequired(boolean required) {
        this.texturePacksRequired = required;
    }

    @Override
    public boolean useMsaGamerTagsOnly() {
        return this.useMsaGamerTagsOnly;
    }

    @Override
    public void setUseMsaGamerTagsOnly(boolean useMsaGamerTagsOnly) {
        this.useMsaGamerTagsOnly = useMsaGamerTagsOnly;
    }

    @Override
    public String getName() {
        return this.worldName;
    }

    @Override
    public void setName(String worldName) {
        this.worldName = worldName;
    }

    @Override
    public long getWorldStartCount() {
        return this.worldStartCount;
    }

    @Override
    public void setWorldStartCount(long startCount) {
        this.worldStartCount = startCount;
    }

    @Override
    public int getXboxLiveBroadcastIntent() {
        return this.xboxLiveBroadcastIntent;
    }

    @Override
    public void setXboxLiveBroadcastIntent(int intent) {
        this.xboxLiveBroadcastIntent = intent;
    }

    @Override
    public int getEduOffer() {
        return this.eduOffer;
    }

    @Override
    public void setEduOffer(int offer) {
        this.eduOffer = offer;
    }

    @Override
    public boolean isEduEnabled() {
        return this.isEduEnabled;
    }

    @Override
    public void setEduEnabled(boolean enabled) {
        this.isEduEnabled = enabled;
    }

    @Override
    public String getBiomeOverride() {
        return this.biomeOverride;
    }

    @Override
    public void setBiomeOverride(String override) {
        this.biomeOverride = override;
    }

    @Override
    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }

    @Override
    public void setBonusChestEnabled(boolean enabled) {
        this.bonusChestEnabled = enabled;
    }

    @Override
    public boolean isBonusChestSpawned() {
        return this.bonusChestSpawned;
    }

    @Override
    public void setBonusChestSpawned(boolean spawned) {
        this.bonusChestSpawned = spawned;
    }

    @Override
    public boolean isCenterMapsToOrigin() {
        return this.centerMapsToOrigin;
    }

    @Override
    public void setCenterMapsToOrigin(boolean status) {
        this.centerMapsToOrigin = status;
    }

    @Override
    public int getDefaultGamemode() {
        return this.defaultGamemode;
    }

    @Override
    public void setDefaultGamemode(int defaultGamemode) {
        this.defaultGamemode = defaultGamemode;
    }

    @Override
    public int getDifficulty() {
        return this.difficulty;
    }

    @Override
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String getFlatWorldLayers() {
        return this.flatWorldLayers;
    }

    @Override
    public void setFlatWorldLayers(String layers) {
        this.flatWorldLayers = layers;
    }

    @Override
    public LevelGameRules getGameRules() {
        return this.gameRules;
    }

    @Override
    public void setGameRules(LevelGameRules gameRules) {
        this.gameRules = gameRules;
    }

    @Override
    public float getLightningLevel() {
        return this.lightningLevel;
    }

    @Override
    public void setLightningLevel(float level) {
        this.lightningLevel = level;
    }

    @Override
    public int getLightningTime() {
        return this.lightningTime;
    }

    @Override
    public void setLightningTime(int time) {
        this.lightningTime = time;
    }

    @Override
    public Vector3i getLimitedWorldCoordinates() {
        return this.limitedWorldCoordinates;
    }

    @Override
    public void setLimitedWorldCoordinates(Vector3i limitedWorldCoordinates) {
        this.limitedWorldCoordinates = limitedWorldCoordinates;
    }

    @Override
    public int getLimitedWorldWidth() {
        return this.limitedWorldWidth;
    }

    @Override
    public void setLimitedWorldWidth(int width) {
        this.limitedWorldWidth = width;
    }

    @Override
    public int getNetherScale() {
        return this.netherScale;
    }

    @Override
    public void setNetherScale(int scale) {
        this.netherScale = scale;
    }

    @Override
    public float getRainLevel() {
        return this.rainLevel;
    }

    @Override
    public void setRainLevel(float rainLevel) {
        this.rainLevel = rainLevel;
    }

    @Override
    public int getRainTime() {
        return this.rainTime;
    }

    @Override
    public void setRainTime(int rainTime) {
        this.rainTime = rainTime;
    }

    @Override
    public long getSeed() {
        return this.seed;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public Vector3i getWorldSpawn() {
        return this.spawnCoordinates;
    }

    @Override
    public void setWorldSpawn(Vector3i coordinates) {
        this.spawnCoordinates = coordinates;
    }

    @Override
    public boolean startWithMapEnabled() {
        return this.startWithMapEnabled;
    }

    @Override
    public void setStartWithMapEnabled(boolean enabled) {
        this.startWithMapEnabled = enabled;
    }

    @Override
    public long getTime() {
        return this.time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int getWorldType() {
        return this.worldType;
    }

    @Override
    public void setWorldType(int worldType) {
        this.worldType = worldType;
    }

    @Override
    public String getBaseGameVersion() {
        return this.baseGameVersion;
    }

    @Override
    public void setBaseGameVersion(String baseGameVersion) {
        this.baseGameVersion = baseGameVersion;
    }

    @Override
    public String getInventoryVersion() {
        return this.inventoryVersion;
    }

    @Override
    public void setInventoryVersion(String inventoryVersion) {
        this.inventoryVersion = inventoryVersion;
    }

    @Override
    public long getLastPlayed() {
        return this.lastPlayed;
    }

    @Override
    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    @Override
    public int[] getMinimumCompatibleClientVersion() {
        return this.minimumCompatibleClientVersion;
    }

    @Override
    public void setMinimumCompatibleClientVersion(int[] version) {
        this.minimumCompatibleClientVersion = version;
    }

    @Override
    public int[] getLastOpenedWithVersion() {
        return this.lastOpenedWithVersion;
    }

    @Override
    public void setLastOpenedWithVersion(int[] version) {
        this.lastOpenedWithVersion = version;
    }

    @Override
    public int getPlatform() {
        return this.platform;
    }

    @Override
    public void setPlatform(int platform) {
        this.platform = platform;
    }

    @Override
    public int getProtocol() {
        return this.protocol;
    }

    @Override
    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getPrid() {
        return this.prid;
    }

    @Override
    public void setPrid(String prid) {
        this.prid = prid;
    }


    @Override
    public MCWorldLevelData clone() {
        try {
            return (MCWorldLevelData) super.clone();
        } catch (CloneNotSupportedException exception) {
            return null;
        }
    }

}
