package io.github.pizzaserver.api.player;

import com.nukkitx.protocol.bedrock.data.PlayerPermission;
import com.nukkitx.protocol.bedrock.data.command.CommandPermission;

public interface AdventureSettings {

    boolean isWorldImmutable();

    void setWorldImmutable(boolean enabled);

    boolean canAutoJump();

    void setAutoJump(boolean enabled);

    boolean hasNoClip();

    void setNoClip(boolean enabled);

    boolean canFly();

    void setCanFly(boolean enabled);

    boolean isFlying();

    void setIsFlying(boolean enabled);

    /**
     * If this player bypasses the world immutable check.
     * @return if this player bypasses the world immutable check
     */
    boolean isWorldBuilder();

    /**
     * Set if this player bypasses the world immutable check.
     * @param enabled if this player bypasses the world immutable check.
     */
    void setIsWorldBuilder(boolean enabled);

    boolean isMuted();

    void setIsMuted(boolean enabled);

    boolean canMine();

    void setCanMine(boolean enabled);

    boolean canBuild();

    void setCanBuild(boolean enabled);

    boolean canUseDoorsAndSwitches();

    void setCanUseDoorsAndSwitches(boolean enabled);

    boolean canOpenContainers();

    void setCanOpenContainers(boolean enabled);

    boolean canAttackPlayers();

    void setCanAttackPlayers(boolean enabled);

    boolean canAttackMobs();

    void setCanAttackMobs(boolean enabled);

    boolean isOperator();

    void setIsOperator(boolean enabled);

    boolean canTeleport();

    void setCanTeleport(boolean enabled);

    CommandPermission getCommandPermission();

    void setCommandPermission(CommandPermission commandPermission);

    PlayerPermission getPlayerPermission();

    void setPlayerPermission(PlayerPermission playerPermission);

}
