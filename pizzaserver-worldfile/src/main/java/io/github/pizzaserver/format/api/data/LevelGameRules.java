package io.github.pizzaserver.format.api.data;

public class LevelGameRules {

    private boolean commandBlockOutput;
    private boolean commandBlocksEnabled;
    private boolean doDaylightCycle;
    private boolean doEntityDrops;
    private boolean doFireTick;
    private boolean doImmediateRespawn;
    private boolean doInsomnia;
    private boolean doMobLoot;
    private boolean doMobSpawning;
    private boolean doTileDrops;
    private boolean doWeatherCycle;
    private boolean drowningDamage;
    private boolean fallDamage;
    private boolean fireDamage;
    private boolean keepInventory;
    private int maxCommandChainLength;
    private boolean mobGriefing;
    private boolean naturalRegeneration;
    private boolean pvp;
    private int randomTickSpeed;
    private boolean sendCommandFeedback;
    private boolean showCoordinates;
    private boolean showDeathMessages;
    private boolean showItemTags;
    private int spawnRadius;
    private boolean tntExplodes;


    public boolean isCommandBlockOutputEnabled() {
        return this.commandBlockOutput;
    }

    public void setCommandBlockOutputEnabled(boolean enabled) {
        this.commandBlockOutput = enabled;
    }

    public boolean isCommandBlocksEnabled() {
        return this.commandBlocksEnabled;
    }

    public void setCommandBlocksEnabled(boolean enabled) {
        this.commandBlocksEnabled = enabled;
    }

    public boolean isDaylightCycleEnabled() {
        return this.doDaylightCycle;
    }

    public void setDaylightCycle(boolean enabled) {
        this.doDaylightCycle = enabled;
    }

    public boolean isEntityDropsEnabled() {
        return this.doEntityDrops;
    }

    public void setEntityDropsEnabled(boolean enabled) {
        this.doEntityDrops = enabled;
    }

    public boolean isFireTickEnabled() {
        return this.doFireTick;
    }

    public void setFireTickEnabled(boolean enabled) {
        this.doFireTick = enabled;
    }

    public boolean isImmediateRespawnEnabled() {
        return this.doImmediateRespawn;
    }

    public void setImmediateRespawnEnabled(boolean enabled) {
        this.doImmediateRespawn = enabled;
    }

    public boolean isInsomniaEnabled() {
        return this.doInsomnia;
    }

    public void setInsomniaEnabled(boolean enabled) {
        this.doInsomnia = enabled;
    }

    public boolean isMobLootEnabled() {
        return this.doMobLoot;
    }

    public void setMobLootEnabled(boolean enabled) {
        this.doMobLoot = enabled;
    }

    public boolean isMobSpawningEnabled() {
        return this.doMobSpawning;
    }

    public void setMobSpawningEnabled(boolean enabled) {
        this.doMobSpawning = enabled;
    }

    public boolean isTileDropsEnabled() {
        return this.doTileDrops;
    }

    public void setTileDropsEnabled(boolean enabled) {
        this.doTileDrops = enabled;
    }

    public boolean isWeatherCycleEnabled() {
        return this.doWeatherCycle;
    }

    public void setWeatherCycleEnabled(boolean enabled) {
        this.doWeatherCycle = enabled;
    }

    public boolean isDrowningDamageEnabled() {
        return this.drowningDamage;
    }

    public void setDrowningDamageEnabled(boolean enabled) {
        this.drowningDamage = enabled;
    }

    public boolean isFallDamageEnabled() {
        return this.fallDamage;
    }

    public void setFallDamageEnabled(boolean enabled) {
        this.fallDamage = enabled;
    }

    public boolean isFireDamageEnabled() {
        return this.fireDamage;
    }

    public void setFireDamageEnabled(boolean enabled) {
        this.fireDamage = enabled;
    }

    public boolean isKeepInventoryEnabled() {
        return this.keepInventory;
    }

    public void setKeepInventoryEnabled(boolean enabled) {
        this.keepInventory = enabled;
    }

    public int getMaxCommandChainLength() {
        return this.maxCommandChainLength;
    }

    public void setMaxCommandChainLength(int length) {
        this.maxCommandChainLength = length;
    }

    public boolean isMobGriefingEnabled() {
        return this.mobGriefing;
    }

    public void setMobGriefingEnabled(boolean enabled) {
        this.mobGriefing = enabled;
    }

    public boolean isNaturalRegenerationEnabled() {
        return this.naturalRegeneration;
    }

    public void setNaturalRegenerationEnabled(boolean enabled) {
        this.naturalRegeneration = enabled;
    }

    public boolean isPVPEnabled() {
        return this.pvp;
    }

    public void setPVPEnabled(boolean enabled) {
        this.pvp = enabled;
    }

    public int getRandomTickSpeed() {
        return this.randomTickSpeed;
    }

    public void setRandomTickSpeed(int speed) {
        this.randomTickSpeed = speed;
    }

    public boolean isSendCommandFeedbackEnabled() {
        return this.sendCommandFeedback;
    }

    public void setSendCommandFeedbackEnabled(boolean enabled) {
        this.sendCommandFeedback = enabled;
    }

    public boolean isShowCoordinatesEnabled() {
        return this.showCoordinates;
    }

    public void setShowCoordinatesEnabled(boolean enabled) {
        this.showCoordinates = enabled;
    }

    public boolean isShowDeathMessagesEnabled() {
        return this.showDeathMessages;
    }

    public void setShowDeathMessagesEnabled(boolean enabled) {
        this.showDeathMessages = enabled;
    }

    public boolean isShowItemTagsEnabled() {
        return this.showItemTags;
    }

    public void setShowItemTagsEnabled(boolean enabled) {
        this.showItemTags = enabled;
    }

    public int getSpawnRadius() {
        return this.spawnRadius;
    }

    public void setSpawnRadius(int spawnRadius) {
        this.spawnRadius = spawnRadius;
    }

    public boolean isTNTExplodesEnabled() {
        return this.tntExplodes;
    }

    public void setTNTExplodesEnabled(boolean enabled) {
        this.tntExplodes = enabled;
    }
}
