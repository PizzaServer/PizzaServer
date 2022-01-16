package io.github.pizzaserver.api.player;

import com.nukkitx.protocol.bedrock.data.PlayerPermission;
import com.nukkitx.protocol.bedrock.data.command.CommandPermission;

public interface AdventureSettings {

    boolean isWorldImmutable();

    AdventureSettings setWorldImmutable(boolean enabled);

    boolean canAutoJump();

    AdventureSettings setAutoJump(boolean enabled);

    boolean hasNoClip();

    AdventureSettings setNoClip(boolean enabled);

    boolean canFly();

    AdventureSettings setCanFly(boolean enabled);

    boolean isFlying();

    AdventureSettings setIsFlying(boolean enabled);

    /**
     * If this player bypasses the world immutable check.
     *
     * @return if this player bypasses the world immutable check
     */
    boolean isWorldBuilder();

    /**
     * Set if this player bypasses the world immutable check.
     *
     * @param enabled if this player bypasses the world immutable check.
     */
    AdventureSettings setIsWorldBuilder(boolean enabled);

    boolean isMuted();

    AdventureSettings setIsMuted(boolean enabled);

    boolean canMine();

    AdventureSettings setCanMine(boolean enabled);

    boolean canBuild();

    AdventureSettings setCanBuild(boolean enabled);

    boolean canUseDoorsAndSwitches();

    AdventureSettings setCanUseDoorsAndSwitches(boolean enabled);

    boolean canOpenContainers();

    AdventureSettings setCanOpenContainers(boolean enabled);

    boolean canAttackPlayers();

    AdventureSettings setCanAttackPlayers(boolean enabled);

    boolean canAttackMobs();

    AdventureSettings setCanAttackMobs(boolean enabled);

    boolean isOperator();

    AdventureSettings setIsOperator(boolean enabled);

    boolean canTeleport();

    AdventureSettings setCanTeleport(boolean enabled);

    CommandPermission getCommandPermission();

    AdventureSettings setCommandPermission(CommandPermission commandPermission);

    PlayerPermission getPlayerPermission();

    AdventureSettings setPlayerPermission(PlayerPermission playerPermission);
}
