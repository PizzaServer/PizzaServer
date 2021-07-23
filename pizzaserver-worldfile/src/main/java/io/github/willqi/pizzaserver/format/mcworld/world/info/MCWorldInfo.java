package io.github.willqi.pizzaserver.format.mcworld.world.info;

import io.github.willqi.pizzaserver.commons.server.Difficulty;
import io.github.willqi.pizzaserver.commons.server.Gamemode;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.commons.world.WorldType;
import io.github.willqi.pizzaserver.commons.world.gamerules.BooleanGameRule;
import io.github.willqi.pizzaserver.commons.world.gamerules.GameRule;
import io.github.willqi.pizzaserver.commons.world.gamerules.GameRuleId;
import io.github.willqi.pizzaserver.commons.world.gamerules.IntegerGameRule;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Representative of the information in the level.dat file
 */
public class MCWorldInfo implements Cloneable {

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
    private int limitedWorldWidth;
    private int netherScale;
    private float rainLevel;
    private int rainTime;
    private long seed;
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
            this.setCommandsEnabled(compound.getByte("commandsEnabled") == 0b1)
                .setCurrentTick(compound.getLong("currentTick"))
                .setHasBeenLoadedInCreative(compound.getByte("hasBeenLoadedInCreative") == 0b1)
                .setHasLockedResourcePack(compound.getByte("hasLockedResourcePack") == 0b1)
                .setHasLockedBehaviorPack(compound.getByte("hasLockedBehaviorPack") == 0b1)
                .setExperiments(compound.getCompound("experiments"))
                .setForceGamemode(compound.getByte("ForceGameType") == 0b1)
                .setImmutable(compound.getByte("immutableWorld") == 0b1)
                .setConfirmedPlatformLockedContent(compound.getByte("ConfirmedPlatformLockedContent") == 0b1)
                .setFromWorldTemplate(compound.getByte("isFromWorldTemplate") == 0b1)
                .setFromLockedTemplate(compound.getByte("isFromLockedTemplate") == 0b1)
                .setIsMultiplayerGame(compound.getByte("MultiplayerGame") == 0b1)
                .setIsSingleUseWorld(compound.getByte("isSingleUseWorld") == 0b1)
                .setIsWorldTemplateOptionsLocked(compound.getByte("isWorldTemplateOptionLocked") == 0b1)
                .setLanBroadcast(compound.getByte("LANBroadcast") == 0b1)
                .setLanBroadcastIntent(compound.getByte("LANBroadcastIntent") == 0b1)
                .setMultiplayerGameIntent(compound.getByte("MultiplayerGameIntent") == 0b1)
                .setPlatformBroadcastIntent(compound.getInteger("PlatformBroadcastIntent"))
                .setRequiresCopiedPackRemovalCheck(compound.getByte("requiresCopiedPackRemovalCheck") == 0b1)
                .setServerChunkTickRange(compound.getInteger("serverChunkTickRange"))
                .setSpawnOnlyV1Villagers(compound.getByte("SpawnV1Villagers") == 0b1)
                .setStorageVersion(compound.getInteger("StorageVersion"))
                .setTexturePacksRequired(compound.getByte("texturePacksRequired") == 0b1)
                .setUseMsaGamerTagsOnly(compound.getByte("useMsaGamertagsOnly") == 0b1)
                .setWorldName(compound.getString("LevelName"))
                .setWorldStartCount(compound.getLong("worldStartCount"))
                .setXboxLiveBroadcastIntent(compound.getInteger("XBLBroadcastIntent"))
                .setEduOffer(compound.getInteger("eduOffer"))
                .setEduEnabled(compound.getByte("educationFeaturesEnabled") == 0b1)
                .setBiomeOverride(compound.getString("BiomeOverride"))
                .setBonusChestEnabled(compound.getByte("bonusChestEnabled") == 0b1)
                .setBonusChestSpawned(compound.getByte("bonusChestSpawned") == 0b1)
                .setCenterMapsToOrigin(compound.getByte("CenterMapsToOrigin") == 0b1)
                .setDefaultGamemode(Gamemode.values()[compound.getInteger("GameType")])
                .setDifficulty(Difficulty.values()[compound.getInteger("Difficulty")])
                .setFlatWorldLayers(compound.getString("FlatWorldLayers"))
                .setLightningLevel(compound.getFloat("lightningLevel"))
                .setLightningTime(compound.getInteger("lightningTime"))
                .setLimitedWorldCoordinates(new Vector3i(
                        compound.getInteger("LimitedWorldOriginX"),
                        compound.getInteger("LimitedWorldOriginY"),
                        compound.getInteger("LimitedWorldOriginZ")
                ))
                .setLimitedWorldWidth(compound.getInteger("limitedWorldWidth"))
                .setNetherScale(compound.getInteger("NetherScale"))
                .setRainLevel(compound.getFloat("rainLevel"))
                .setRainTime(compound.getInteger("rainTime"))
                .setSeed(compound.getLong("RandomSeed"))
                .setSpawnCoordinates(new Vector3i(
                        compound.getInteger("SpawnX"),
                        compound.getInteger("SpawnY"),
                        compound.getInteger("SpawnZ")
                ))
                .setStartWithMapEnabled(compound.getByte("startWithMapEnabled") == 0x01)
                .setTime(compound.getLong("Time"))
                .setWorldType(WorldType.values()[compound.getInteger("Generator")])
                .setBaseGameVersion(compound.getString("baseGameVersion"))
                .setInventoryVersion(compound.getString("InventoryVersion"))
                .setLastPlayed(compound.getLong("LastPlayed"))
                .setMinimumCompatibleClientVersion(
                        Arrays.stream((NBTInteger[])compound.getList("MinimumCompatibleClientVersion"))
                                .mapToInt(NBTInteger::getValue)
                                .toArray()
                )
                .setLastOpenedWithVersion(
                        Arrays.stream((NBTInteger[])compound.getList("lastOpenedWithVersion"))
                                .mapToInt(NBTInteger::getValue)
                                .toArray()
                )
                .setPlatform(compound.getInteger("Platform"))
                .setProtocol(compound.getInteger("NetworkVersion"))
                .setPrid(compound.getString("prid"));

            NBTCompound abilities = compound.getCompound("abilities");
            this.setPlayerAbilities(
                    new PlayerAbilities()
                            .setCanAttackMobs(abilities.getByte("attackmobs") == 0b1)
                            .setCanAttackPlayers(abilities.getByte("attackplayers") == 0b1)
                            .setCanBuild(abilities.getByte("build") == 0b1)
                            .setCanFly(abilities.getByte("mayfly") == 0b1)
                            .setCanInstaBuild(abilities.getByte("instabuild") == 0b1)
                            .setCanMine(abilities.getByte("mine") == 0b1)
                            .setCanOpenContainers(abilities.getByte("opencontainers") == 0b1)
                            .setCanTeleport(abilities.getByte("teleport") == 0b1)
                            .setCanUseDoorsAndSwitches(abilities.getByte("doorsandswitches") == 0b1)
                            .setFlySpeed(abilities.getFloat("flySpeed"))
                            .setIsFlying(abilities.getByte("flying") == 0b1)
                            .setIsInvulnerable(abilities.getByte("invulnerable") == 0b1)
                            .setIsOp(abilities.getByte("op") == 0b1)
                            .setIsLightning(abilities.getByte("lightning") == 0b1)
                            .setPermissionsLevel(abilities.getInteger("permissionsLevel"))
                            .setPlayerPermissionsLevel(abilities.getInteger("playerPermissionsLevel"))
                            .setWalkSpeed(abilities.getFloat("walkSpeed"))
            );

            this.setGameRules(new HashMap<GameRuleId, GameRule>(){
                {
                    put(GameRuleId.COMMAND_BLOCK_OUTPUT, new BooleanGameRule(GameRuleId.COMMAND_BLOCK_OUTPUT, compound.getByte("commandblockoutput") == 0b1));
                    put(GameRuleId.COMMAND_BLOCKS_ENABLED, new BooleanGameRule(GameRuleId.COMMAND_BLOCKS_ENABLED, compound.getByte("commandblocksenabled") == 0b1));
                    put(GameRuleId.DO_DAYLIGHT_CYCLE, new BooleanGameRule(GameRuleId.DO_DAYLIGHT_CYCLE, compound.getByte("dodaylightcycle") == 0b1));
                    put(GameRuleId.DO_ENTITY_DROPS, new BooleanGameRule(GameRuleId.DO_ENTITY_DROPS, compound.getByte("doentitydrops") == 0b1));
                    put(GameRuleId.DO_FIRE_TICK, new BooleanGameRule(GameRuleId.DO_FIRE_TICK, compound.getByte("dofiretick") == 0b1));
                    put(GameRuleId.DO_IMMEDIATE_RESPAWN, new BooleanGameRule(GameRuleId.DO_IMMEDIATE_RESPAWN, compound.getByte("doimmediaterespawn") == 0b1));
                    put(GameRuleId.DO_INSOMNIA, new BooleanGameRule(GameRuleId.DO_INSOMNIA, compound.getByte("doinsomnia") == 0b1));
                    put(GameRuleId.DO_MOB_LOOT, new BooleanGameRule(GameRuleId.DO_MOB_LOOT, compound.getByte("domobloot") == 0b1));
                    put(GameRuleId.DO_MOB_SPAWNING, new BooleanGameRule(GameRuleId.DO_MOB_SPAWNING, compound.getByte("domobspawning") == 0b1));
                    put(GameRuleId.DO_TILE_DROPS, new BooleanGameRule(GameRuleId.DO_TILE_DROPS, compound.getByte("dotiledrops") == 0b1));
                    put(GameRuleId.DO_WEATHER_CYCLE, new BooleanGameRule(GameRuleId.DO_WEATHER_CYCLE, compound.getByte("doweathercycle") == 0b1));
                    put(GameRuleId.DROWNING_DAMAGE, new BooleanGameRule(GameRuleId.DROWNING_DAMAGE, compound.getByte("drowningdamage") == 0b1));
                    put(GameRuleId.FALL_DAMAGE, new BooleanGameRule(GameRuleId.FALL_DAMAGE, compound.getByte("falldamage") == 0b1));
                    put(GameRuleId.FIRE_DAMAGE, new BooleanGameRule(GameRuleId.FIRE_DAMAGE, compound.getByte("firedamage") == 0b1));
                    put(GameRuleId.KEEP_INVENTORY, new BooleanGameRule(GameRuleId.KEEP_INVENTORY, compound.getByte("keepinventory") == 0b1));
                    put(GameRuleId.MAX_COMMAND_CHAIN_LENGTH, new IntegerGameRule(GameRuleId.MAX_COMMAND_CHAIN_LENGTH, compound.getInteger("maxcommandchainlength")));
                    put(GameRuleId.MOB_GRIEFING, new BooleanGameRule(GameRuleId.MOB_GRIEFING, compound.getByte("mobgriefing") == 0b1));
                    put(GameRuleId.NATURAL_REGENERATION, new BooleanGameRule(GameRuleId.NATURAL_REGENERATION, compound.getByte("naturalregeneration") == 0b1));
                    put(GameRuleId.PVP, new BooleanGameRule(GameRuleId.PVP, compound.getByte("pvp") == 0b1));
                    put(GameRuleId.RANDOM_TICK_SPEED, new IntegerGameRule(GameRuleId.RANDOM_TICK_SPEED, compound.getInteger("randomtickspeed")));
                    put(GameRuleId.SEND_COMMAND_FEEDBACK, new BooleanGameRule(GameRuleId.SEND_COMMAND_FEEDBACK, compound.getByte("sendcommandfeedback") == 0b1));
                    put(GameRuleId.SHOW_COORDINATES, new BooleanGameRule(GameRuleId.SHOW_COORDINATES, compound.getByte("showcoordinates") == 0b1));
                    put(GameRuleId.SHOW_DEATH_MESSAGES, new BooleanGameRule(GameRuleId.SHOW_DEATH_MESSAGES, compound.getByte("showdeathmessages") == 0b1));
                    put(GameRuleId.SHOW_ITEM_TAGS, new BooleanGameRule(GameRuleId.SHOW_ITEM_TAGS, compound.getByte("showtags") == 0b1));
                    put(GameRuleId.SPAWN_RADIUS, new IntegerGameRule(GameRuleId.SPAWN_RADIUS, compound.getInteger("spawnradius")));
                    put(GameRuleId.TNT_EXPLODES, new BooleanGameRule(GameRuleId.TNT_EXPLODES, compound.getByte("tntexplodes") == 0b1));
                }
            });
        }
    }

    public boolean isCommandsEnabled() {
        return this.commandsEnabled;
    }

    public MCWorldInfo setCommandsEnabled(boolean enabled) {
        this.commandsEnabled = enabled;
        return this;
    }

    public long getCurrentTick() {
        return this.currentTick;
    }

    public MCWorldInfo setCurrentTick(long currentTick) {
        this.currentTick = currentTick;
        return this;
    }

    public boolean hasBeenLoadedInCreative() {
        return this.hasBeenLoadedInCreative;
    }

    public MCWorldInfo setHasBeenLoadedInCreative(boolean loaded) {
        this.hasBeenLoadedInCreative = loaded;
        return this;
    }

    public boolean hasLockedResourcePack() {
        return this.hasLockedResourcePack;
    }

    public MCWorldInfo setHasLockedResourcePack(boolean hasLocked) {
        this.hasLockedResourcePack = hasLocked;
        return this;
    }

    public boolean isHasLockedBehaviorPack() {
        return this.hasLockedBehaviorPack;
    }

    public MCWorldInfo setHasLockedBehaviorPack(boolean hasLocked) {
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
    public MCWorldInfo setExperiments(NBTCompound experiments) {
        this.experiments = experiments;
        return this;
    }

    public boolean isForcedGamemode() {
        return this.forceGamemode;
    }

    public MCWorldInfo setForceGamemode(boolean forced) {
        this.forceGamemode = forced;
        return this;
    }

    public boolean isImmutable() {
        return this.immutable;
    }

    public MCWorldInfo setImmutable(boolean immutable) {
        this.immutable = immutable;
        return this;
    }

    public boolean isConfirmedPlatformLockedContent() {
        return this.isConfirmedPlatformLockedContent;
    }

    public MCWorldInfo setConfirmedPlatformLockedContent(boolean locked) {
        this.isConfirmedPlatformLockedContent = locked;
        return this;
    }

    public boolean isFromWorldTemplate() {
        return this.isFromWorldTemplate;
    }

    public MCWorldInfo setFromWorldTemplate(boolean fromWorldTemplate) {
        this.isFromWorldTemplate = fromWorldTemplate;
        return this;
    }

    public boolean isFromLockedTemplate() {
        return this.isFromLockedTemplate;
    }

    public MCWorldInfo setFromLockedTemplate(boolean fromLockedTemplate) {
        this.isFromLockedTemplate = fromLockedTemplate;
        return this;
    }

    public boolean isMultiplayerGame() {
        return this.isMultiplayerGame;
    }

    public MCWorldInfo setIsMultiplayerGame(boolean multiplayerGame) {
        this.isMultiplayerGame = multiplayerGame;
        return this;
    }

    public boolean isSingleUseWorld() {
        return this.isSingleUseWorld;
    }

    public MCWorldInfo setIsSingleUseWorld(boolean singleUse) {
        this.isSingleUseWorld = singleUse;
        return this;
    }

    public boolean isWorldTemplateOptionsLocked() {
        return this.isWorldTemplateOptionsLocked;
    }

    public MCWorldInfo setIsWorldTemplateOptionsLocked(boolean locked) {
        this.isWorldTemplateOptionsLocked = locked;
        return this;
    }

    public boolean isLanBroadcast() {
        return this.lanBroadcast;
    }

    public MCWorldInfo setLanBroadcast(boolean broadcast) {
        this.lanBroadcast = broadcast;
        return this;
    }

    public boolean isLanBroadcastIntent() {
        return this.lanBroadcastIntent;
    }

    public MCWorldInfo setLanBroadcastIntent(boolean intent) {
        this.lanBroadcastIntent = intent;
        return this;
    }

    public boolean isMultiplayerGameIntent() {
        return this.multiplayerGameIntent;
    }

    public MCWorldInfo setMultiplayerGameIntent(boolean multiplayerGameIntent) {
        this.multiplayerGameIntent = multiplayerGameIntent;
        return this;
    }

    public int getPlatformBroadcastIntent() {
        return this.platformBroadcastIntent;
    }

    public MCWorldInfo setPlatformBroadcastIntent(int intent) {
        this.platformBroadcastIntent = intent;
        return this;
    }

    public boolean requiresCopiedPackRemovalCheck() {
        return this.requiresCopiedPackRemovalCheck;
    }

    public MCWorldInfo setRequiresCopiedPackRemovalCheck(boolean required) {
        this.requiresCopiedPackRemovalCheck = required;
        return this;
    }

    public int getServerChunkTickRange() {
        return this.serverChunkTickRange;
    }

    public MCWorldInfo setServerChunkTickRange(int tickRange) {
        this.serverChunkTickRange = tickRange;
        return this;
    }

    public boolean spawnOnlyV1Villagers() {
        return this.spawnOnlyV1Villagers;
    }

    public MCWorldInfo setSpawnOnlyV1Villagers(boolean spawnOnly) {
        this.spawnOnlyV1Villagers = spawnOnly;
        return this;
    }

    public int getStorageVersion() {
        return this.storageVersion;
    }

    public MCWorldInfo setStorageVersion(int version) {
        this.storageVersion = version;
        return this;
    }

    public PlayerAbilities getPlayerAbilities() {
        return this.playerAbilities;
    }

    public MCWorldInfo setPlayerAbilities(PlayerAbilities abilities) {
        this.playerAbilities = abilities;
        return this;
    }

    public boolean isTexturePacksRequired() {
        return this.texturePacksRequired;
    }

    public MCWorldInfo setTexturePacksRequired(boolean required) {
        this.texturePacksRequired = required;
        return this;
    }

    public boolean useMsaGamerTagsOnly() {
        return this.useMsaGamerTagsOnly;
    }

    public MCWorldInfo setUseMsaGamerTagsOnly(boolean useMsaGamerTagsOnly) {
        this.useMsaGamerTagsOnly = useMsaGamerTagsOnly;
        return this;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public MCWorldInfo setWorldName(String worldName) {
        this.worldName = worldName;
        return this;
    }

    public long getWorldStartCount() {
        return this.worldStartCount;
    }

    public MCWorldInfo setWorldStartCount(long startCount) {
        this.worldStartCount = startCount;
        return this;
    }

    public int getXboxLiveBroadcastIntent() {
        return this.xboxLiveBroadcastIntent;
    }

    public MCWorldInfo setXboxLiveBroadcastIntent(int intent) {
        this.xboxLiveBroadcastIntent = intent;
        return this;
    }


    public int getEduOffer() {
        return this.eduOffer;
    }

    public MCWorldInfo setEduOffer(int offer) {
        this.eduOffer = offer;
        return this;
    }

    public boolean isEduEnabled() {
        return this.isEduEnabled;
    }

    public MCWorldInfo setEduEnabled(boolean enabled) {
        this.isEduEnabled = enabled;
        return this;
    }


    public String getBiomeOverride() {
        return this.biomeOverride;
    }

    public MCWorldInfo setBiomeOverride(String override) {
        this.biomeOverride = override;
        return this;
    }

    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }

    public MCWorldInfo setBonusChestEnabled(boolean enabled) {
        this.bonusChestEnabled = enabled;
        return this;
    }

    public boolean isBonusChestSpawned() {
        return this.bonusChestSpawned;
    }

    public MCWorldInfo setBonusChestSpawned(boolean spawned) {
        this.bonusChestSpawned = spawned;
        return this;
    }

    public boolean isCenterMapsToOrigin() {
        return this.centerMapsToOrigin;
    }

    public MCWorldInfo setCenterMapsToOrigin(boolean status) {
        this.centerMapsToOrigin = status;
        return this;
    }

    public Gamemode getDefaultGamemode() {
        return this.defaultGamemode;
    }

    public MCWorldInfo setDefaultGamemode(Gamemode defaultGamemode) {
        this.defaultGamemode = defaultGamemode;
        return this;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public MCWorldInfo setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public String getFlatWorldLayers() {
        return this.flatWorldLayers;
    }

    public MCWorldInfo setFlatWorldLayers(String layers) {
        this.flatWorldLayers = flatWorldLayers;
        return this;
    }

    public Map<GameRuleId, GameRule> getGameRules() {
        return this.gameRules;
    }

    public MCWorldInfo setGameRules(Map<GameRuleId, GameRule> gameRules) {
        this.gameRules = gameRules;
        return this;
    }

    public float getLightningLevel() {
        return this.lightningLevel;
    }

    public MCWorldInfo setLightningLevel(float level) {
        this.lightningLevel = level;
        return this;
    }

    public int getLightningTime() {
        return this.lightningTime;
    }

    public MCWorldInfo setLightningTime(int time) {
        this.lightningTime = time;
        return this;
    }

    public Vector3i getLimitedWorldCoordinates() {
        return this.limitedWorldCoordinates;
    }

    public MCWorldInfo setLimitedWorldCoordinates(Vector3i limitedWorldCoordinates) {
        this.limitedWorldCoordinates = limitedWorldCoordinates;
        return this;
    }

    public int getLimitedWorldWidth() {
        return this.limitedWorldWidth;
    }

    public MCWorldInfo setLimitedWorldWidth(int width) {
        this.limitedWorldWidth = width;
        return this;
    }

    public int getNetherScale() {
        return this.netherScale;
    }

    public MCWorldInfo setNetherScale(int scale) {
        this.netherScale = scale;
        return this;
    }

    public float getRainLevel() {
        return this.rainLevel;
    }

    public MCWorldInfo setRainLevel(float rainLevel) {
        this.rainLevel = rainLevel;
        return this;
    }

    public int getRainTime() {
        return this.rainTime;
    }

    public MCWorldInfo setRainTime(int rainTime) {
        this.rainTime = rainTime;
        return this;
    }

    public long getSeed() {
        return this.seed;
    }

    public MCWorldInfo setSeed(long seed) {
        this.seed = seed;
        return this;
    }

    public Vector3i getSpawnCoordinates() {
        return this.spawnCoordinates;
    }

    public MCWorldInfo setSpawnCoordinates(Vector3i coordinates) {
        this.spawnCoordinates = coordinates;
        return this;
    }

    public boolean startWithMapEnabled() {
        return this.startWithMapEnabled;
    }

    public MCWorldInfo setStartWithMapEnabled(boolean enabled) {
        this.startWithMapEnabled = enabled;
        return this;
    }

    public long getTime() {
        return this.time;
    }

    public MCWorldInfo setTime(long time) {
        this.time = time;
        return this;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public MCWorldInfo setWorldType(WorldType worldType) {
        this.worldType = worldType;
        return this;
    }


    public String getBaseGameVersion() {
        return this.baseGameVersion;
    }

    public MCWorldInfo setBaseGameVersion(String baseGameVersion) {
        this.baseGameVersion = baseGameVersion;
        return this;
    }

    public String getInventoryVersion() {
        return this.inventoryVersion;
    }

    public MCWorldInfo setInventoryVersion(String inventoryVersion) {
        this.inventoryVersion = inventoryVersion;
        return this;
    }

    public long getLastPlayed() {
        return this.lastPlayed;
    }

    public MCWorldInfo setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
        return this;
    }

    public int[] getMinimumCompatibleClientVersion() {
        return this.minimumCompatibleClientVersion;
    }

    public MCWorldInfo setMinimumCompatibleClientVersion(int[] version) {
        this.minimumCompatibleClientVersion = version;
        return this;
    }

    public int[] getLastOpenedWithVersion() {
        return this.lastOpenedWithVersion;
    }

    public MCWorldInfo setLastOpenedWithVersion(int[] version) {
        this.lastOpenedWithVersion = version;
        return this;
    }

    public int getPlatform() {
        return this.platform;
    }

    public MCWorldInfo setPlatform(int platform) {
        this.platform = platform;
        return this;
    }

    public int getProtocol() {
        return this.protocol;
    }

    public MCWorldInfo setProtocol(int protocol) {
        this.protocol = protocol;
        return this;
    }

    public String getPrid() {
        return this.prid;
    }

    public MCWorldInfo setPrid(String prid) {
        this.prid = prid;
        return this;
    }


    @Override
    public MCWorldInfo clone() {
        try {
            return (MCWorldInfo)super.clone();
        } catch (CloneNotSupportedException exception) {
            return null;
        }
    }

}
