package io.github.pizzaserver.server.level;

import com.nukkitx.protocol.bedrock.data.GameRuleData;
import com.nukkitx.protocol.bedrock.packet.GameRulesChangedPacket;
import io.github.pizzaserver.api.level.LevelGameRules;
import io.github.pizzaserver.api.player.Player;

public class ImplLevelGameRules implements LevelGameRules {

    private static final String DO_DAYLIGHT_CYCLE_ID = "doDaylightCycle";
    private static final String IMMEDIATE_RESPAWN_ID = "doImmediateRespawn";
    private static final String SHOW_COORDINATES_ID = "showCoordinates";
    private static final String SHOW_ITEM_TAGS_ID = "showTags";

    private final ImplLevel level;


    public ImplLevelGameRules(ImplLevel level) {
        this.level = level;
    }

    @Override
    public boolean isDaylightCycleEnabled() {
        return this.level.getLevelData().getGameRules().isDaylightCycleEnabled();
    }

    @Override
    public void setDaylightCycleEnabled(boolean enabled) {
        if (this.isDaylightCycleEnabled() != enabled) {
            this.level.getLevelData().getGameRules().setDaylightCycle(enabled);
            this.onUpdate(new GameRuleData<>(DO_DAYLIGHT_CYCLE_ID, enabled));
        }
    }

    @Override
    public boolean isEntityDropsEnabled() {
        return this.level.getLevelData().getGameRules().isEntityDropsEnabled();
    }

    @Override
    public void setEntityDropsEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setEntityDropsEnabled(enabled);
    }

    @Override
    public boolean isFireTickEnabled() {
        return this.level.getLevelData().getGameRules().isFireTickEnabled();
    }

    @Override
    public void setFireTickEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setFireTickEnabled(enabled);
    }

    @Override
    public boolean isMobLootEnabled() {
        return this.level.getLevelData().getGameRules().isMobLootEnabled();
    }

    @Override
    public void setMobLootEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setMobLootEnabled(enabled);
    }

    @Override
    public boolean isMobSpawningEnabled() {
        return this.level.getLevelData().getGameRules().isMobSpawningEnabled();
    }

    @Override
    public void setMobSpawningEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setMobSpawningEnabled(enabled);
    }

    @Override
    public boolean isTileDropsEnabled() {
        return this.level.getLevelData().getGameRules().isTileDropsEnabled();
    }

    @Override
    public void setTileDropsEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setTileDropsEnabled(enabled);
    }

    @Override
    public boolean isWeatherCycleEnabled() {
        return this.level.getLevelData().getGameRules().isWeatherCycleEnabled();
    }

    @Override
    public void setWeatherCycleEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setWeatherCycleEnabled(enabled);
    }

    @Override
    public boolean isDrowningDamageEnabled() {
        return this.level.getLevelData().getGameRules().isDrowningDamageEnabled();
    }

    @Override
    public void setDrowningDamageEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setDrowningDamageEnabled(enabled);
    }

    @Override
    public boolean isFallDamageEnabled() {
        return this.level.getLevelData().getGameRules().isFallDamageEnabled();
    }

    @Override
    public void setFallDamageEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setFallDamageEnabled(enabled);
    }

    @Override
    public boolean isFireDamageEnabled() {
        return this.level.getLevelData().getGameRules().isFireDamageEnabled();
    }

    @Override
    public void setFireDamageEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setFireDamageEnabled(enabled);
    }

    @Override
    public boolean isKeepInventoryEnabled() {
        return this.level.getLevelData().getGameRules().isKeepInventoryEnabled();
    }

    @Override
    public void setKeepInventoryEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setKeepInventoryEnabled(enabled);
    }

    @Override
    public boolean isMobGriefingEnabled() {
        return this.level.getLevelData().getGameRules().isMobGriefingEnabled();
    }

    @Override
    public void setMobGriefingEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setMobGriefingEnabled(enabled);
    }

    @Override
    public boolean isPVPEnabled() {
        return this.level.getLevelData().getGameRules().isPVPEnabled();
    }

    @Override
    public void setPVPEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setPVPEnabled(enabled);
    }

    @Override
    public boolean isShowCoordinatesEnabled() {
        return this.level.getLevelData().getGameRules().isShowCoordinatesEnabled();
    }

    @Override
    public void setShowCoordinatesEnabled(boolean enabled) {
        if (this.isShowCoordinatesEnabled() != enabled) {
            this.level.getLevelData().getGameRules().setShowCoordinatesEnabled(enabled);
            this.onUpdate(new GameRuleData<>(SHOW_COORDINATES_ID, enabled));
        }
    }

    @Override
    public boolean isNaturalRegenerationEnabled() {
        return this.level.getLevelData().getGameRules().isNaturalRegenerationEnabled();
    }

    @Override
    public void setNaturalRegenerationEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setNaturalRegenerationEnabled(enabled);
    }

    @Override
    public boolean isTNTExplosionEnabled() {
        return this.level.getLevelData().getGameRules().isTNTExplodesEnabled();
    }

    @Override
    public void setTNTExplosionEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setTNTExplodesEnabled(enabled);
    }

    @Override
    public boolean isSendCommandFeedbackEnabled() {
        return this.level.getLevelData().getGameRules().isSendCommandFeedbackEnabled();
    }

    @Override
    public void setCommandFeedbackEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setSendCommandFeedbackEnabled(enabled);
    }

    @Override
    public int getMaxCommandChainLength() {
        return this.level.getLevelData().getGameRules().getMaxCommandChainLength();
    }

    @Override
    public void setMaxCommandChainLength(int length) {
        this.level.getLevelData().getGameRules().setMaxCommandChainLength(length);
    }

    @Override
    public boolean isInsomniaEnabled() {
        return this.level.getLevelData().getGameRules().isInsomniaEnabled();
    }

    @Override
    public void setInsomniaEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setInsomniaEnabled(enabled);
    }

    @Override
    public boolean isCommandBlocksEnabled() {
        return this.level.getLevelData().getGameRules().isCommandBlocksEnabled();
    }

    @Override
    public void setCommandBlocksEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setCommandBlocksEnabled(enabled);
    }

    @Override
    public int getRandomTickSpeed() {
        return this.level.getLevelData().getGameRules().getRandomTickSpeed();
    }

    @Override
    public void setRandomTickSpeed(int speed) {
        this.level.getLevelData().getGameRules().setRandomTickSpeed(speed);
    }

    @Override
    public boolean isImmediateRespawnEnabled() {
        return this.level.getLevelData().getGameRules().isImmediateRespawnEnabled();
    }

    @Override
    public void setImmediateRespawnEnabled(boolean enabled) {
        if (this.isImmediateRespawnEnabled() != enabled) {
            this.level.getLevelData().getGameRules().setImmediateRespawnEnabled(enabled);
            this.onUpdate(new GameRuleData<>(IMMEDIATE_RESPAWN_ID, enabled));
        }
    }

    @Override
    public boolean isShowDeathMessagesEnabled() {
        return this.level.getLevelData().getGameRules().isShowDeathMessagesEnabled();
    }

    @Override
    public void setShowDeathMessagesEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setShowDeathMessagesEnabled(enabled);
    }

    @Override
    public int getSpawnRadius() {
        return this.level.getLevelData().getGameRules().getSpawnRadius();
    }

    @Override
    public void setSpawnRadius(int radius) {
        this.level.getLevelData().getGameRules().setSpawnRadius(radius);
    }

    @Override
    public boolean isShowTagsEnabled() {
        return this.level.getLevelData().getGameRules().isShowItemTagsEnabled();
    }

    @Override
    public void setShowTagsEnabled(boolean enabled) {
        if (this.isShowTagsEnabled() != enabled) {
            this.level.getLevelData().getGameRules().setShowItemTagsEnabled(enabled);
            this.onUpdate(new GameRuleData<>(SHOW_ITEM_TAGS_ID, enabled));
        }
    }

    @Override
    public boolean isFreezeDamageEnabled() {
        return this.level.getLevelData().getGameRules().isFreezeDamageEnabled();
    }

    @Override
    public void setFreezeDamageEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setFreezeDamageEnabled(enabled);
    }

    @Override
    public boolean isRespawnBlockExplosionEnabled() {
        return this.level.getLevelData().getGameRules().isRespawnBlockExplosionsEnabled();
    }

    @Override
    public void setRespawnBlockExplosionEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setRespawnBlockExplosionsEnabled(enabled);
    }

    @Override
    public boolean isShowBorderEffectsEnabled() {
        return this.level.getLevelData().getGameRules().isShowBorderEffectsEnabled();
    }

    @Override
    public void setShowBorderEffectsEnabled(boolean enabled) {
        this.level.getLevelData().getGameRules().setShowBorderEffectsEnabled(enabled);
    }

    public void sendTo(Player player) {
        GameRulesChangedPacket rulesChangedPacket = new GameRulesChangedPacket();
        rulesChangedPacket.getGameRules().add(new GameRuleData<>(DO_DAYLIGHT_CYCLE_ID, this.isDaylightCycleEnabled()));
        rulesChangedPacket.getGameRules().add(new GameRuleData<>(IMMEDIATE_RESPAWN_ID, this.isImmediateRespawnEnabled()));
        rulesChangedPacket.getGameRules().add(new GameRuleData<>(SHOW_COORDINATES_ID, this.isShowCoordinatesEnabled()));
        rulesChangedPacket.getGameRules().add(new GameRuleData<>(SHOW_ITEM_TAGS_ID, this.isShowTagsEnabled()));
        player.sendPacket(rulesChangedPacket);
    }

    private void onUpdate(GameRuleData<?> data) {
        GameRulesChangedPacket rulesChangedPacket = new GameRulesChangedPacket();
        rulesChangedPacket.getGameRules().add(data);

        for (Player player : this.level.getPlayers()) {
            player.sendPacket(rulesChangedPacket);
        }
    }

}
