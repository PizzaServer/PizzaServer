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
            this.setCommandsEnabled(compound.getByte("commandsEnabled").getValue() == 0x01)
                .setCurrentTick(compound.getLong("currentTick").getValue())
                .setHasBeenLoadedInCreative(compound.getByte("hasBeenLoadedInCreative").getValue() == 0x01)
                .setHasLockedResourcePack(compound.getByte("hasLockedResourcePack").getValue() == 0x01)
                .setHasLockedBehaviorPack(compound.getByte("hasLockedBehaviorPack").getValue() == 0x01)
                .setExperiments(compound.getCompound("experiments"))
                .setForceGamemode(compound.getByte("ForceGameType").getValue() == 0x01)
                .setImmutable(compound.getByte("immutableWorld").getValue() == 0x01)
                .setConfirmedPlatformLockedContent(compound.getByte("ConfirmedPlatformLockedContent").getValue() == 0x01)
                .setFromWorldTemplate(compound.getByte("isFromWorldTemplate").getValue() == 0x01)
                .setFromLockedTemplate(compound.getByte("isFromLockedTemplate").getValue() == 0x01)
                .setIsMultiplayerGame(compound.getByte("MultiplayerGame").getValue() == 0x01)
                .setIsSingleUseWorld(compound.getByte("isSingleUseWorld").getValue() == 0x01)
                .setIsWorldTemplateOptionsLocked(compound.getByte("isWorldTemplateOptionLocked").getValue() == 0x01)
                .setLanBroadcast(compound.getByte("LANBroadcast").getValue() == 0x01)
                .setLanBroadcastIntent(compound.getByte("LANBroadcastIntent").getValue() == 0x01)
                .setMultiplayerGameIntent(compound.getByte("MultiplayerGameIntent").getValue() == 0x01)
                .setPlatformBroadcastIntent(compound.getInteger("PlatformBroadcastIntent").getValue())
                .setRequiresCopiedPackRemovalCheck(compound.getByte("requiresCopiedPackRemovalCheck").getValue() == 0x01)
                .setServerChunkTickRange(compound.getInteger("serverChunkTickRange").getValue())
                .setSpawnOnlyV1Villagers(compound.getByte("SpawnV1Villagers").getValue() == 0x01)
                .setStorageVersion(compound.getInteger("StorageVersion").getValue())
                .setTexturePacksRequired(compound.getByte("texturePacksRequired").getValue() == 0x01)
                .setUseMsaGamerTagsOnly(compound.getByte("useMsaGamertagsOnly").getValue() == 0x01)
                .setWorldName(compound.getString("LevelName").getValue())
                .setWorldStartCount(compound.getLong("worldStartCount").getValue())
                .setXboxLiveBroadcastIntent(compound.getInteger("XBLBroadcastIntent").getValue())
                .setEduOffer(compound.getInteger("eduOffer").getValue())
                .setEduEnabled(compound.getByte("educationFeaturesEnabled").getValue() == 0x01)
                .setBiomeOverride(compound.getString("BiomeOverride").getValue())
                .setBonusChestEnabled(compound.getByte("bonusChestEnabled").getValue() == 0x01)
                .setBonusChestSpawned(compound.getByte("bonusChestSpawned").getValue() == 0x01)
                .setCenterMapsToOrigin(compound.getByte("CenterMapsToOrigin").getValue() == 0x01)
                .setDefaultGamemode(Gamemode.values()[compound.getInteger("GameType").getValue()])
                .setDifficulty(Difficulty.values()[compound.getInteger("Difficulty").getValue()])
                .setFlatWorldLayers(compound.getString("FlatWorldLayers").getValue())
                .setLightningLevel(compound.getFloat("lightningLevel").getValue())
                .setLightningTime(compound.getInteger("lightningTime").getValue())
                .setLimitedWorldCoordinates(new Vector3i(
                        compound.getInteger("LimitedWorldOriginX").getValue(),
                        compound.getInteger("LimitedWorldOriginY").getValue(),
                        compound.getInteger("LimitedWorldOriginZ").getValue()
                ))
                .setLimitedWorldWidth(compound.getInteger("limitedWorldWidth").getValue())
                .setNetherScale(compound.getInteger("NetherScale").getValue())
                .setRainLevel(compound.getFloat("rainLevel").getValue())
                .setRainTime(compound.getInteger("rainTime").getValue())
                .setSeed(compound.getLong("RandomSeed").getValue())
                .setSpawnCoordinates(new Vector3i(
                        compound.getInteger("SpawnX").getValue(),
                        compound.getInteger("SpawnY").getValue(),
                        compound.getInteger("SpawnZ").getValue()
                ))
                .setStartWithMapEnabled(compound.getByte("startWithMapEnabled").getValue() == 0x01)
                .setTime(compound.getLong("Time").getValue())
                .setWorldType(WorldType.values()[compound.getInteger("Generator").getValue()])
                .setBaseGameVersion(compound.getString("baseGameVersion").getValue())
                .setInventoryVersion(compound.getString("InventoryVersion").getValue())
                .setLastPlayed(compound.getLong("LastPlayed").getValue())
                .setMinimumCompatibleClientVersion(
                        Arrays.stream((NBTInteger[])compound.getList("MinimumCompatibleClientVersion").getContents())
                                .mapToInt(NBTInteger::getValue)
                                .toArray()
                )
                .setLastOpenedWithVersion(
                        Arrays.stream((NBTInteger[])compound.getList("lastOpenedWithVersion").getContents())
                                .mapToInt(NBTInteger::getValue)
                                .toArray()
                )
                .setPlatform(compound.getInteger("Platform").getValue())
                .setProtocol(compound.getInteger("NetworkVersion").getValue())
                .setPrid(compound.getString("prid").getValue());

            NBTCompound abilities = compound.getCompound("abilities");
            this.setPlayerAbilities(
                    new PlayerAbilities()
                            .setCanAttackMobs(abilities.getByte("attackmobs").getValue() == 0x01)
                            .setCanAttackPlayers(abilities.getByte("attackplayers").getValue() == 0x01)
                            .setCanBuild(abilities.getByte("build").getValue() == 0x01)
                            .setCanFly(abilities.getByte("mayfly").getValue() == 0x01)
                            .setCanInstaBuild(abilities.getByte("instabuild").getValue() == 0x01)
                            .setCanMine(abilities.getByte("mine").getValue() == 0x01)
                            .setCanOpenContainers(abilities.getByte("opencontainers").getValue() == 0x01)
                            .setCanTeleport(abilities.getByte("teleport").getValue() == 0x01)
                            .setCanUseDoorsAndSwitches(abilities.getByte("doorsandswitches").getValue() == 0x01)
                            .setFlySpeed(abilities.getFloat("flySpeed").getValue())
                            .setIsFlying(abilities.getByte("flying").getValue() == 0x01)
                            .setIsInvulnerable(abilities.getByte("invulnerable").getValue() == 0x01)
                            .setIsOp(abilities.getByte("op").getValue() == 0x01)
                            .setIsLightning(abilities.getByte("lightning").getValue() == 0x01)
                            .setPermissionsLevel(abilities.getInteger("permissionsLevel").getValue())
                            .setPlayerPermissionsLevel(abilities.getInteger("playerPermissionsLevel").getValue())
                            .setWalkSpeed(abilities.getFloat("walkSpeed").getValue())
            );

            this.setGameRules(new HashMap<GameRuleId, GameRule>(){
                {
                    put(GameRuleId.COMMAND_BLOCK_OUTPUT, new BooleanGameRule(GameRuleId.COMMAND_BLOCK_OUTPUT, compound.getByte("commandblockoutput").getValue() == 0x01));
                    put(GameRuleId.COMMAND_BLOCKS_ENABLED, new BooleanGameRule(GameRuleId.COMMAND_BLOCKS_ENABLED, compound.getByte("commandblocksenabled").getValue() == 0x01));
                    put(GameRuleId.DO_DAYLIGHT_CYCLE, new BooleanGameRule(GameRuleId.DO_DAYLIGHT_CYCLE, compound.getByte("dodaylightcycle").getValue() == 0x01));
                    put(GameRuleId.DO_ENTITY_DROPS, new BooleanGameRule(GameRuleId.DO_ENTITY_DROPS, compound.getByte("doentitydrops").getValue() == 0x01));
                    put(GameRuleId.DO_FIRE_TICK, new BooleanGameRule(GameRuleId.DO_FIRE_TICK, compound.getByte("dofiretick").getValue() == 0x01));
                    put(GameRuleId.DO_IMMEDIATE_RESPAWN, new BooleanGameRule(GameRuleId.DO_IMMEDIATE_RESPAWN, compound.getByte("doimmediaterespawn").getValue() == 0x01));
                    put(GameRuleId.DO_INSOMNIA, new BooleanGameRule(GameRuleId.DO_INSOMNIA, compound.getByte("doinsomnia").getValue() == 0x01));
                    put(GameRuleId.DO_MOB_LOOT, new BooleanGameRule(GameRuleId.DO_MOB_LOOT, compound.getByte("domobloot").getValue() == 0x01));
                    put(GameRuleId.DO_MOB_SPAWNING, new BooleanGameRule(GameRuleId.DO_MOB_SPAWNING, compound.getByte("domobspawning").getValue() == 0x01));
                    put(GameRuleId.DO_TILE_DROPS, new BooleanGameRule(GameRuleId.DO_TILE_DROPS, compound.getByte("dotiledrops").getValue() == 0x01));
                    put(GameRuleId.DO_WEATHER_CYCLE, new BooleanGameRule(GameRuleId.DO_WEATHER_CYCLE, compound.getByte("doweathercycle").getValue() == 0x01));
                    put(GameRuleId.DROWNING_DAMAGE, new BooleanGameRule(GameRuleId.DROWNING_DAMAGE, compound.getByte("drowningdamage").getValue() == 0x01));
                    put(GameRuleId.FALL_DAMAGE, new BooleanGameRule(GameRuleId.FALL_DAMAGE, compound.getByte("falldamage").getValue() == 0x01));
                    put(GameRuleId.FIRE_DAMAGE, new BooleanGameRule(GameRuleId.FIRE_DAMAGE, compound.getByte("firedamage").getValue() == 0x01));
                    put(GameRuleId.KEEP_INVENTORY, new BooleanGameRule(GameRuleId.KEEP_INVENTORY, compound.getByte("keepinventory").getValue() == 0x01));
                    put(GameRuleId.MAX_COMMAND_CHAIN_LENGTH, new IntegerGameRule(GameRuleId.MAX_COMMAND_CHAIN_LENGTH, compound.getInteger("maxcommandchainlength").getValue()));
                    put(GameRuleId.MOB_GRIEFING, new BooleanGameRule(GameRuleId.MOB_GRIEFING, compound.getByte("mobgriefing").getValue() == 0x01));
                    put(GameRuleId.NATURAL_REGENERATION, new BooleanGameRule(GameRuleId.NATURAL_REGENERATION, compound.getByte("naturalregeneration").getValue() == 0x01));
                    put(GameRuleId.PVP, new BooleanGameRule(GameRuleId.PVP, compound.getByte("pvp").getValue() == 0x01));
                    put(GameRuleId.RANDOM_TICK_SPEED, new IntegerGameRule(GameRuleId.RANDOM_TICK_SPEED, compound.getInteger("randomtickspeed").getValue()));
                    put(GameRuleId.SEND_COMMAND_FEEDBACK, new BooleanGameRule(GameRuleId.SEND_COMMAND_FEEDBACK, compound.getByte("sendcommandfeedback").getValue() == 0x01));
                    put(GameRuleId.SHOW_COORDINATES, new BooleanGameRule(GameRuleId.SHOW_COORDINATES, compound.getByte("showcoordinates").getValue() == 0x01));
                    put(GameRuleId.SHOW_DEATH_MESSAGES, new BooleanGameRule(GameRuleId.SHOW_DEATH_MESSAGES, compound.getByte("showdeathmessages").getValue() == 0x01));
                    put(GameRuleId.SHOW_ITEM_TAGS, new BooleanGameRule(GameRuleId.SHOW_ITEM_TAGS, compound.getByte("showtags").getValue() == 0x01));
                    put(GameRuleId.SPAWN_RADIUS, new IntegerGameRule(GameRuleId.SPAWN_RADIUS, compound.getInteger("spawnradius").getValue()));
                    put(GameRuleId.TNT_EXPLODES, new BooleanGameRule(GameRuleId.TNT_EXPLODES, compound.getByte("tntexplodes").getValue() == 0x01));
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
