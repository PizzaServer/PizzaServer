package io.github.pizzaserver.api.level.data.gamerules;

public enum GameRuleID {

    COMMAND_BLOCK_OUTPUT("commandBlockOutput"),
    COMMAND_BLOCKS_ENABLED("commandBlockEenabled"),
    DO_DAYLIGHT_CYCLE("doDaylightCycle"),
    DO_ENTITY_DROPS("doEntityDrops"),
    DO_FIRE_TICK("doFireTick"),
    DO_IMMEDIATE_RESPAWN("doImmediateRespawn"),
    DO_INSOMNIA("doInsomnia"),
    DO_MOB_LOOT("doMobLoot"),
    DO_MOB_SPAWNING("doMobSpawning"),
    DO_TILE_DROPS("doTileDrops"),
    DO_WEATHER_CYCLE("doWeatherCycle"),
    DROWNING_DAMAGE("drowningDamage"),
    FALL_DAMAGE("fallDamage"),
    FIRE_DAMAGE("fireDamage"),
    KEEP_INVENTORY("keepInventory"),
    MAX_COMMAND_CHAIN_LENGTH("maxCommandChainLength"),
    MOB_GRIEFING("mobGriefing"),
    NATURAL_REGENERATION("naturalRegeneration"),
    PVP("pvp"),
    RANDOM_TICK_SPEED("randomTickSpeed"),
    SEND_COMMAND_FEEDBACK("sendCommandFeedback"),
    SHOW_COORDINATES("showCoordinates"),
    SHOW_DEATH_MESSAGES("showDeathMessages"),
    SHOW_ITEM_TAGS("showTags"),
    SPAWN_RADIUS("spawnRadius"),
    TNT_EXPLODES("tntExplodes");


    private final String id;

    GameRuleID(String id) {
        this.id = id;
    }

    public String getName() {
        return this.id;
    }

}
