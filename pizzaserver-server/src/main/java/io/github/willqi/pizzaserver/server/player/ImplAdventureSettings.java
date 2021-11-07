package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.api.commands.CommandPermissionLevel;
import io.github.willqi.pizzaserver.api.network.protocol.packets.AdventureSettingsPacket;
import io.github.willqi.pizzaserver.api.player.AdventureSettings;
import io.github.willqi.pizzaserver.api.player.data.PlayerPermissionLevel;

import java.util.HashSet;
import java.util.Set;

public class ImplAdventureSettings implements AdventureSettings, Cloneable {

    protected Set<AdventureSettingsPacket.Flag> flags = new HashSet<AdventureSettingsPacket.Flag>() {
        {
            this.add(AdventureSettingsPacket.Flag.CAN_BUILD);
            this.add(AdventureSettingsPacket.Flag.CAN_MINE);
            this.add(AdventureSettingsPacket.Flag.CAN_USE_DOORS_AND_SWITCHES);
            this.add(AdventureSettingsPacket.Flag.CAN_OPEN_CONTAINERS);
            this.add(AdventureSettingsPacket.Flag.CAN_ATTACK_PLAYERS);
            this.add(AdventureSettingsPacket.Flag.CAN_ATTACK_MOBS);
        }
    };

    private CommandPermissionLevel commandPermissionLevel = CommandPermissionLevel.NORMAL;
    private PlayerPermissionLevel playerPermissionLevel = PlayerPermissionLevel.CUSTOM;


    public Set<AdventureSettingsPacket.Flag> getFlags() {
        return new HashSet<>(this.flags);
    }

    @Override
    public boolean isWorldImmutable() {
        return this.flags.contains(AdventureSettingsPacket.Flag.WORLD_IMMUTABLE);
    }

    @Override
    public void setWorldImmutable(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.WORLD_IMMUTABLE);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.WORLD_IMMUTABLE);
        }
    }

    @Override
    public boolean canAutoJump() {
        return this.flags.contains(AdventureSettingsPacket.Flag.AUTO_JUMP);
    }

    @Override
    public void setAutoJump(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.AUTO_JUMP);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.AUTO_JUMP);
        }
    }

    @Override
    public boolean hasNoClip() {
        return this.flags.contains(AdventureSettingsPacket.Flag.NO_CLIP);
    }

    @Override
    public void setNoClip(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.NO_CLIP);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.NO_CLIP);
        }
    }

    @Override
    public boolean canFly() {
        return this.flags.contains(AdventureSettingsPacket.Flag.CAN_FLY);
    }

    @Override
    public void setCanFly(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.CAN_FLY);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.CAN_FLY);
        }
    }

    @Override
    public boolean isFlying() {
        return this.flags.contains(AdventureSettingsPacket.Flag.FLYING);
    }

    @Override
    public void setIsFlying(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.FLYING);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.FLYING);
        }
    }

    @Override
    public boolean isWorldBuilder() {
        return this.flags.contains(AdventureSettingsPacket.Flag.WORLD_BUILDER);
    }

    @Override
    public void setIsWorldBuilder(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.WORLD_BUILDER);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.WORLD_BUILDER);
        }
    }

    @Override
    public boolean isMuted() {
        return this.flags.contains(AdventureSettingsPacket.Flag.MUTED);
    }

    @Override
    public void setIsMuted(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.MUTED);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.MUTED);
        }
    }

    @Override
    public boolean canMine() {
        return this.flags.contains(AdventureSettingsPacket.Flag.CAN_MINE);
    }

    @Override
    public void setCanMine(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.CAN_MINE);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.CAN_MINE);
        }
    }

    @Override
    public boolean canBuild() {
        return this.flags.contains(AdventureSettingsPacket.Flag.CAN_BUILD);
    }

    @Override
    public void setCanBuild(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.CAN_BUILD);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.CAN_BUILD);
        }
    }

    @Override
    public boolean canUseDoorsAndSwitches() {
        return this.flags.contains(AdventureSettingsPacket.Flag.CAN_USE_DOORS_AND_SWITCHES);
    }

    @Override
    public void setCanUseDoorsAndSwitches(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.CAN_USE_DOORS_AND_SWITCHES);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.CAN_USE_DOORS_AND_SWITCHES);
        }
    }

    @Override
    public boolean canOpenContainers() {
        return this.flags.contains(AdventureSettingsPacket.Flag.CAN_OPEN_CONTAINERS);
    }

    @Override
    public void setCanOpenContainers(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.CAN_OPEN_CONTAINERS);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.CAN_OPEN_CONTAINERS);
        }
    }

    @Override
    public boolean canAttackPlayers() {
        return this.flags.contains(AdventureSettingsPacket.Flag.CAN_ATTACK_PLAYERS);
    }

    @Override
    public void setCanAttackPlayers(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.CAN_ATTACK_PLAYERS);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.CAN_ATTACK_PLAYERS);
        }
    }

    @Override
    public boolean canAttackMobs() {
        return this.flags.contains(AdventureSettingsPacket.Flag.CAN_ATTACK_MOBS);
    }

    @Override
    public void setCanAttackMobs(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.CAN_ATTACK_MOBS);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.CAN_ATTACK_MOBS);
        }
    }

    @Override
    public boolean isOperator() {
        return this.flags.contains(AdventureSettingsPacket.Flag.IS_OPERATOR);
    }

    @Override
    public void setIsOperator(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.IS_OPERATOR);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.IS_OPERATOR);
        }
    }

    @Override
    public boolean canTeleport() {
        return this.flags.contains(AdventureSettingsPacket.Flag.CAN_TELEPORT);
    }

    @Override
    public void setCanTeleport(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSettingsPacket.Flag.CAN_TELEPORT);
        } else {
            this.flags.remove(AdventureSettingsPacket.Flag.CAN_TELEPORT);
        }
    }

    @Override
    public CommandPermissionLevel getCommandPermissionLevel() {
        return this.commandPermissionLevel;
    }

    @Override
    public void setCommandPermissionLevel(CommandPermissionLevel commandPermissionLevel) {
        this.commandPermissionLevel = commandPermissionLevel;
    }

    @Override
    public PlayerPermissionLevel getPlayerPermissionLevel() {
        return this.playerPermissionLevel;
    }

    @Override
    public void setPlayerPermissionLevel(PlayerPermissionLevel playerPermissionLevel) {
        this.playerPermissionLevel = playerPermissionLevel;
    }

    @Override
    public ImplAdventureSettings clone() {
        try {
            ImplAdventureSettings settings = (ImplAdventureSettings) super.clone();
            settings.flags = new HashSet<>(this.flags);
            return settings;
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError("Clone threw an exception", exception);
        }
    }

}
