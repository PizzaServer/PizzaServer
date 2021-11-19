package io.github.pizzaserver.format.mcworld.world.info;

import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.format.api.LevelGameRules;
import io.github.pizzaserver.format.api.LevelData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Representative of the information in the level.dat file
 */
public class MCWorldInfo implements LevelData, Cloneable {

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


    public MCWorldInfo() {}

    public MCWorldInfo(File levelDatFile) throws IOException {
        try (NBTInputStream inputStream = new NBTInputStream(
                new LittleEndianDataInputStream(
                        new FileInputStream(levelDatFile)
                )
        )) {
            // the header is 8 bytes.
            inputStream.skip(8);    // TODO: These 8 bytes are important when writing the level.dat file
            NBTCompound compound = inputStream.readCompound();
            
            this.setCommandsEnabled(compound.getBoolean("commandsEnabled"));
            this.setCurrentTick(compound.getLong("currentTick"));
            this.setHasBeenLoadedInCreative(compound.getBoolean("hasBeenLoadedInCreative"));
            this.setHasLockedResourcePack(compound.getBoolean("hasLockedResourcePack"));
            this.setHasLockedBehaviorPack(compound.getBoolean("hasLockedBehaviorPack"));
            this.setExperiments(compound.getCompound("experiments"));
            this.setForceGamemode(compound.getBoolean("ForceGameType"));
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
            this.setPlatformBroadcastIntent(compound.getInteger("PlatformBroadcastIntent"));
            this.setRequiresCopiedPackRemovalCheck(compound.getBoolean("requiresCopiedPackRemovalCheck"));
            this.setServerChunkTickRange(compound.getInteger("serverChunkTickRange"));
            this.setSpawnOnlyV1Villagers(compound.getBoolean("SpawnV1Villagers"));
            this.setStorageVersion(compound.getInteger("StorageVersion"));
            this.setTexturePacksRequired(compound.getBoolean("texturePacksRequired"));
            this.setUseMsaGamerTagsOnly(compound.getBoolean("useMsaGamertagsOnly"));
            this.setName(compound.getString("LevelName"));
            this.setWorldStartCount(compound.getLong("worldStartCount"));
            this.setXboxLiveBroadcastIntent(compound.getInteger("XBLBroadcastIntent"));
            this.setEduOffer(compound.getInteger("eduOffer"));
            this.setEduEnabled(compound.getBoolean("educationFeaturesEnabled"));
            this.setBiomeOverride(compound.getString("BiomeOverride"));
            this.setBonusChestEnabled(compound.getBoolean("bonusChestEnabled"));
            this.setBonusChestSpawned(compound.getBoolean("bonusChestSpawned"));
            this.setCenterMapsToOrigin(compound.getBoolean("CenterMapsToOrigin"));
            this.setDefaultGamemode(compound.getInteger("GameType"));
            this.setDifficulty(compound.getInteger("Difficulty"));
            this.setFlatWorldLayers(compound.getString("FlatWorldLayers"));
            this.setLightningLevel(compound.getFloat("lightningLevel"));
            this.setLightningTime(compound.getInteger("lightningTime"));
            this.setLimitedWorldCoordinates(new Vector3i(
                    compound.getInteger("LimitedWorldOriginX"), 
                    compound.getInteger("LimitedWorldOriginY"),
                    compound.getInteger("LimitedWorldOriginZ")));
            this.setLimitedWorldWidth(compound.getInteger("limitedWorldWidth"));
            this.setNetherScale(compound.getInteger("NetherScale"));
            this.setRainLevel(compound.getFloat("rainLevel"));
            this.setRainTime(compound.getInteger("rainTime"));
            this.setSeed(compound.getLong("RandomSeed"));
            this.setWorldSpawn(new Vector3i(
                    compound.getInteger("SpawnX"), 
                    compound.getInteger("SpawnY"), 
                    compound.getInteger("SpawnZ")
            ));
            this.setStartWithMapEnabled(compound.getBoolean("startWithMapEnabled"));
            this.setTime(compound.getLong("Time"));
            this.setWorldType(compound.getInteger("Generator"));
            this.setBaseGameVersion(compound.getString("baseGameVersion"));
            this.setInventoryVersion(compound.getString("InventoryVersion"));
            this.setLastPlayed(compound.getLong("LastPlayed"));
            this.setMinimumCompatibleClientVersion(
                    Arrays.stream(compound.getList("MinimumCompatibleClientVersion").getContents())
                            .mapToInt(i -> (Integer) i)
                            .toArray()
            );
            this.setLastOpenedWithVersion(
                    Arrays.stream(compound.getList("lastOpenedWithVersion").getContents())
                            .mapToInt(i -> (Integer) i)
                            .toArray()
            );
            this.setPlatform(compound.getInteger("Platform"));
            this.setProtocol(compound.getInteger("NetworkVersion"));
            this.setPrid(compound.getString("prid"));

            NBTCompound abilities = compound.getCompound("abilities");
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
                            .setPermissionsLevel(abilities.getInteger("permissionsLevel"))
                            .setPlayerPermissionsLevel(abilities.getInteger("playerPermissionsLevel"))
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
            gameRules.setMaxCommandChainLength(compound.getInteger("maxcommandchainlength"));
            gameRules.setMobGriefingEnabled(compound.getBoolean("mobgriefing"));
            gameRules.setNaturalRegenerationEnabled(compound.getBoolean("naturalregeneration"));
            gameRules.setPVPEnabled(compound.getBoolean("pvp"));
            gameRules.setRandomTickSpeed(compound.getInteger("randomtickspeed"));
            gameRules.setSendCommandFeedbackEnabled(compound.getBoolean("sendcommandfeedback"));
            gameRules.setShowCoordinatesEnabled(compound.getBoolean("showcoordinates"));
            gameRules.setShowDeathMessagesEnabled(compound.getBoolean("showdeathmessages"));
            gameRules.setShowItemTagsEnabled(compound.getBoolean("showtags"));
            gameRules.setSpawnRadius(compound.getInteger("spawnradius"));
            gameRules.setTNTExplodesEnabled(compound.getBoolean("tntexplodes"));
            this.setGameRules(gameRules);
        }
    }

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

    public boolean isHasLockedBehaviorPack() {
        return this.hasLockedBehaviorPack;
    }

    public void setHasLockedBehaviorPack(boolean hasLocked) {
        this.hasLockedBehaviorPack = hasLocked;
    }

    /**
     * This is subject to change and will change when experiments are explored.
     * @return experiments
     */
    public NBTCompound getExperiments() {
        return this.experiments;
    }

    /**
     * This is subject to change and will change when experiments are explored.
     * @param experiments experiments
     */
    public void setExperiments(NBTCompound experiments) {
        this.experiments = experiments;
    }

    public boolean isForcedGamemode() {
        return this.forceGamemode;
    }

    public void setForceGamemode(boolean forced) {
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

    @Override
    public String getName() {
        return this.worldName;
    }

    @Override
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

    public String getFlatWorldLayers() {
        return this.flatWorldLayers;
    }

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

    public long getSeed() {
        return this.seed;
    }

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

    public boolean startWithMapEnabled() {
        return this.startWithMapEnabled;
    }

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
    public MCWorldInfo clone() {
        try {
            return (MCWorldInfo) super.clone();
        } catch (CloneNotSupportedException exception) {
            return null;
        }
    }

}
