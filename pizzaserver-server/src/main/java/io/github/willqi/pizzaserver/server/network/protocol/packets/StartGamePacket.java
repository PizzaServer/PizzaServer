package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.commons.server.Difficulty;
import io.github.willqi.pizzaserver.server.network.protocol.data.ItemState;
import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerMovementType;
import io.github.willqi.pizzaserver.commons.server.Gamemode;
import io.github.willqi.pizzaserver.server.player.data.PermissionLevel;
import io.github.willqi.pizzaserver.commons.utils.Vector2;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.world.data.Dimension;
import io.github.willqi.pizzaserver.server.data.ServerOrigin;
import io.github.willqi.pizzaserver.commons.world.WorldType;
import io.github.willqi.pizzaserver.commons.world.gamerules.GameRule;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class StartGamePacket extends BedrockPacket {

    public static final int ID = 0x0b;

    // Entity specific
    private Dimension dimension;
    private long entityId;
    private Gamemode playerGamemode;
    private PermissionLevel permissionLevel;
    private long runtimeEntityId;
    private Vector2 rotation;
    private Vector3 spawn;

    // Server
    private boolean achievementsEnabled;
    private int chunkTickRange;
    private boolean commandsEnabled;
    private long currentTick;
    private Gamemode defaultGamemode;
    private Difficulty difficulty;
    private int enchantmentSeed;
    private boolean forceExperimentalGameplay;
    private String gameVersion;
    private boolean isTrial;
    private String serverName;
    private boolean spawnOnlyV1Villagers;

    private int movementRewindSize;
    private PlayerMovementType movementType;
    private boolean serverAuthoritativeBlockBreaking;
    private boolean serverAuthoritativeInventory;

    private boolean hasLockedBehaviorPacks;
    private boolean hasLockedResourcePacks;
    private boolean isFromLockedWorldTemplate;
    private boolean isFromWorldTemplate;
    private boolean isWorldTemplateOptionsLocked;
    private String premiumWorldTemplateId = "";
    private boolean resourcePacksRequired;

    private boolean eduFeaturesEnabled;
    private UUID eduUuid;
    private ServerOrigin serverOrigin;

    // TODO: block properties for custom blocks
    private Collection<ItemState> itemStates = new HashSet<>();

    private boolean broadcastToLan;
    private boolean hasPlatformLockedContent;
    private boolean isMultiplayer;
    private UUID multiplayerId = UUID.randomUUID();
    private int platformBroadcastMode;  // TODO: convert to enum
    private int xboxLiveBroadcastMode;  // TODO: convert to enum
    private boolean useMsaGamerTagsOnly;

    // World
    private boolean bonusChestEnabled;
    private boolean bonusMapEnabled;
    private Collection<GameRule<?>> gameRules = new HashSet<>();
    private boolean isNetherType;
    private float lightingLevel;
    private int limitedWorldHeight;
    private int limitedWorldWidth;
    private float rainLevel;
    private int seed;
    private Vector3i worldSpawn;
    private String worldId;
    private int worldTime;
    private WorldType worldType;

    private short biomeType;            // TODO: convert to class
    private String customBiomeName = "";


    public StartGamePacket() {
        super(ID);
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public long getEntityId() {
        return this.entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public Gamemode getPlayerGamemode() {
        return this.playerGamemode;
    }

    public void setPlayerGamemode(Gamemode gamemode) {
        this.playerGamemode = gamemode;
    }

    public PermissionLevel getPlayerPermissionLevel() {
        return this.permissionLevel;
    }

    public void setPlayerPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public Vector2 getPlayerRotation() {
        return this.rotation;
    }

    public void setPlayerRotation(Vector2 rotation) {
        this.rotation = rotation;
    }

    public Vector3 getPlayerSpawn() {
        return this.spawn;
    }

    public void setPlayerSpawn(Vector3 spawn) {
        this.spawn = spawn;
    }


    public boolean areAchievementsEnabled() {
        return this.achievementsEnabled;
    }

    public void setAchievementsEnabled(boolean enabled) {
        this.achievementsEnabled = enabled;
    }

    public int getChunkTickRange() {
        return this.chunkTickRange;
    }

    public void setChunkTickRange(int chunkTickRange) {
        this.chunkTickRange = chunkTickRange;
    }

    public boolean areCommandsEnabled() {
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

    public Gamemode getDefaultGamemode() {
        return this.defaultGamemode;
    }

    public void setDefaultGamemode(Gamemode gamemode) {
        this.defaultGamemode = gamemode;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getEnchantmentSeed() {
        return this.enchantmentSeed;
    }

    public void setEnchantmentSeed(int seed) {
        this.enchantmentSeed = seed;
    }

    public boolean isExperimentalGameplayForced() {
        return this.forceExperimentalGameplay;
    }

    public void setExperimentalGameplayForced(boolean status) {
        this.forceExperimentalGameplay = status;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public boolean isTrial() {
        return this.isTrial;
    }

    public void setTrial(boolean isTrial) {
        this.isTrial = isTrial;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean isSpawningOnlyV1VillagersAllowed() {
        return this.spawnOnlyV1Villagers;
    }

    public void setSpawnOnlyV1Villagers(boolean status) {
        this.spawnOnlyV1Villagers = status;
    }


    public int getMovementRewindSize() {
        return this.movementRewindSize;
    }

    public void setMovementRewindSize(int size) {
        this.movementRewindSize = size;
    }

    public PlayerMovementType getMovementType() {
        return this.movementType;
    }

    public void setMovementType(PlayerMovementType movementType) {
        this.movementType = movementType;
    }

    public boolean isServerAuthoritativeBlockBreaking() {
        return this.serverAuthoritativeBlockBreaking;
    }

    public void setServerAuthoritativeBlockBreaking(boolean enabled) {
        this.serverAuthoritativeBlockBreaking = enabled;
    }

    public boolean isServerAuthoritativeInventory() {
        return this.serverAuthoritativeInventory;
    }

    public void setServerAuthoritativeInventory(boolean enabled) {
        this.serverAuthoritativeInventory = enabled;
    }


    public boolean hasLockedBehaviorPacks() {
        return this.hasLockedBehaviorPacks;
    }

    public void setHasLockedBehaviorPacks(boolean status) {
        this.hasLockedBehaviorPacks = status;
    }

    public boolean hasLockedResourcePacks() {
        return this.hasLockedResourcePacks;
    }

    public void setHasLockedResourcePacks(boolean status) {
        this.hasLockedResourcePacks = status;
    }

    public boolean isFromLockedWorldTemplate() {
        return this.isFromLockedWorldTemplate;
    }

    public void setFromLockedWorldTemplate(boolean status) {
        this.isFromLockedWorldTemplate = status;
    }

    public boolean isFromWorldTemplate() {
        return this.isFromWorldTemplate;
    }

    public void setFromWorldTemplate(boolean status) {
        this.isFromWorldTemplate = status;
    }

    public boolean isWorldTemplateOptionsLocked() {
        return this.isWorldTemplateOptionsLocked;
    }

    public void setWorldTemplateOptionsLocked(boolean status) {
        this.isWorldTemplateOptionsLocked = status;
    }

    public String getPremiumWorldTemplateId() {
        return this.premiumWorldTemplateId;
    }

    public void setPremiumWorldTemplateId(String premiumWorldTemplateId) {
        this.premiumWorldTemplateId = premiumWorldTemplateId;
    }

    public boolean areResourcePacksRequired() {
        return this.resourcePacksRequired;
    }

    public void setResourcePacksRequired(boolean required) {
        this.resourcePacksRequired = required;
    }


    public boolean areEduFeaturedEnabled() {
        return this.eduFeaturesEnabled;
    }

    public void setEduFeaturesEnabled(boolean enabled) {
        this.eduFeaturesEnabled = enabled;
    }

    public UUID getEduUuid() {
        return this.eduUuid;
    }

    public void setEduUuid(UUID id) {
        this.eduUuid = id;
    }

    public ServerOrigin getServerOrigin() {
        return this.serverOrigin;
    }

    public void setServerOrigin(ServerOrigin serverOrigin) {
        this.serverOrigin = serverOrigin;
    }


    public Collection<ItemState> getItemStates() {
        return this.itemStates;
    }

    public void setItemStates(Collection<ItemState> itemStates) {
        this.itemStates = itemStates;
    }


    public boolean broadcastingToLan() {
        return this.broadcastToLan;
    }

    public void setBroadcastToLan(boolean broadcast) {
        this.broadcastToLan = broadcast;
    }

    public boolean hasPlatformLockedContent() {
        return this.hasPlatformLockedContent;
    }

    public void setHasPlatformLockedContent(boolean status) {
        this.hasPlatformLockedContent = status;
    }

    public boolean isMultiplayer() {
        return this.isMultiplayer;
    }

    public void setMultiplayer(boolean status) {
        this.isMultiplayer = true;
    }

    public UUID getMultiplayerId() {
        return this.multiplayerId;
    }

    public void setMultiplayerId(UUID uuid) {
        this.multiplayerId = uuid;
    }

    public int getPlatformBroadcastMode() {
        return this.platformBroadcastMode;
    }

    public void setPlatformBroadcastMode(int mode) {
        this.platformBroadcastMode = mode;
    }

    public int getXboxLiveBroadcastMode() {
        return this.xboxLiveBroadcastMode;
    }

    public void setXboxLiveBroadcastMode(int mode) {
        this.xboxLiveBroadcastMode = mode;
    }

    public boolean useMsaGamerTagsOnly() {
        return this.useMsaGamerTagsOnly;
    }

    public void setUseMsaGamerTagsOnly(boolean status) {
        this.useMsaGamerTagsOnly = status;
    }


    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }

    public void setBonusChestEnabled(boolean enabled) {
        this.bonusChestEnabled = enabled;
    }

    public boolean isBonusMapEnabled() {
        return this.bonusMapEnabled;
    }

    public void setBonusMapEnabled(boolean enabled) {
        this.bonusMapEnabled = enabled;
    }

    public Collection<GameRule<?>> getGameRules() {
        return this.gameRules;
    }

    public void setGameRules(Collection<GameRule<?>> gameRules) {
        this.gameRules = gameRules;
    }

    public boolean isNetherType() {
        return this.isNetherType;
    }

    public void setNetherType(boolean isNetherType) {
        this.isNetherType = isNetherType;
    }

    public float getLightingLevel() {
        return this.lightingLevel;
    }

    public void setLightingLevel(float level) {
        this.lightingLevel = level;
    }

    public int getLimitedWorldHeight() {
        return this.limitedWorldHeight;
    }

    public void setLimitedWorldHeight(int height) {
        this.limitedWorldHeight = height;
    }

    public int getLimitedWorldWidth() {
        return this.limitedWorldWidth;
    }

    public void setLimitedWorldWidth(int width) {
        this.limitedWorldWidth = width;
    }

    public float getRainLevel() {
        return this.rainLevel;
    }

    public void setRainLevel(float level) {
        this.rainLevel = level;
    }

    public int getSeed() {
        return this.seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public Vector3i getWorldSpawn() {
        return this.worldSpawn;
    }

    public void setWorldSpawn(Vector3i worldSpawn) {
        this.worldSpawn = worldSpawn;
    }

    public String getWorldId() {
        return this.worldId;
    }

    public void setWorldId(String id) {
        this.worldId = id;
    }

    public int getWorldTime() {
        return this.worldTime;
    }

    public void setWorldTime(int time) {
        this.worldTime = time;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public void setWorldType(WorldType worldType) {
        this.worldType = worldType;
    }


    public short getBiomeType() {
        return this.biomeType;
    }

    public void setBiomeType(short biomeType) {
        this.biomeType = biomeType;
    }

    public String getCustomBiomeName() {
        return this.customBiomeName;
    }

    public void setCustomBiomeName(String customBiomeName) {
        this.customBiomeName = customBiomeName;
    }


}
