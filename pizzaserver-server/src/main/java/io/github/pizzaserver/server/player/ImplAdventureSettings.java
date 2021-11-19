package io.github.pizzaserver.server.player;

import com.nukkitx.protocol.bedrock.data.AdventureSetting;
import com.nukkitx.protocol.bedrock.data.PlayerPermission;
import com.nukkitx.protocol.bedrock.data.command.CommandPermission;
import io.github.pizzaserver.api.player.AdventureSettings;

import java.util.HashSet;
import java.util.Set;

public class ImplAdventureSettings implements AdventureSettings, Cloneable {

    protected Set<AdventureSetting> flags = new HashSet<AdventureSetting>() {
        {
            this.add(AdventureSetting.BUILD);
            this.add(AdventureSetting.MINE);
            this.add(AdventureSetting.DOORS_AND_SWITCHES);
            this.add(AdventureSetting.OPEN_CONTAINERS);
            this.add(AdventureSetting.ATTACK_PLAYERS);
            this.add(AdventureSetting.ATTACK_MOBS);
        }
    };

    private CommandPermission commandPermission = CommandPermission.NORMAL;
    private PlayerPermission playerPermission = PlayerPermission.MEMBER;


    public Set<AdventureSetting> getSettings() {
        return new HashSet<>(this.flags);
    }

    @Override
    public boolean isWorldImmutable() {
        return this.flags.contains(AdventureSetting.WORLD_IMMUTABLE);
    }

    @Override
    public void setWorldImmutable(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.WORLD_IMMUTABLE);
        } else {
            this.flags.remove(AdventureSetting.WORLD_IMMUTABLE);
        }
    }

    @Override
    public boolean canAutoJump() {
        return this.flags.contains(AdventureSetting.AUTO_JUMP);
    }

    @Override
    public void setAutoJump(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.AUTO_JUMP);
        } else {
            this.flags.remove(AdventureSetting.AUTO_JUMP);
        }
    }

    @Override
    public boolean hasNoClip() {
        return this.flags.contains(AdventureSetting.NO_CLIP);
    }

    @Override
    public void setNoClip(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.NO_CLIP);
        } else {
            this.flags.remove(AdventureSetting.NO_CLIP);
        }
    }

    @Override
    public boolean canFly() {
        return this.flags.contains(AdventureSetting.MAY_FLY);
    }

    @Override
    public void setCanFly(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.MAY_FLY);
        } else {
            this.flags.remove(AdventureSetting.MAY_FLY);
        }
    }

    @Override
    public boolean isFlying() {
        return this.flags.contains(AdventureSetting.FLYING);
    }

    @Override
    public void setIsFlying(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.FLYING);
        } else {
            this.flags.remove(AdventureSetting.FLYING);
        }
    }

    @Override
    public boolean isWorldBuilder() {
        return this.flags.contains(AdventureSetting.WORLD_BUILDER);
    }

    @Override
    public void setIsWorldBuilder(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.WORLD_BUILDER);
        } else {
            this.flags.remove(AdventureSetting.WORLD_BUILDER);
        }
    }

    @Override
    public boolean isMuted() {
        return this.flags.contains(AdventureSetting.MUTED);
    }

    @Override
    public void setIsMuted(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.MUTED);
        } else {
            this.flags.remove(AdventureSetting.MUTED);
        }
    }

    @Override
    public boolean canMine() {
        return this.flags.contains(AdventureSetting.MINE);
    }

    @Override
    public void setCanMine(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.MINE);
        } else {
            this.flags.remove(AdventureSetting.MINE);
        }
    }

    @Override
    public boolean canBuild() {
        return this.flags.contains(AdventureSetting.BUILD);
    }

    @Override
    public void setCanBuild(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.BUILD);
        } else {
            this.flags.remove(AdventureSetting.BUILD);
        }
    }

    @Override
    public boolean canUseDoorsAndSwitches() {
        return this.flags.contains(AdventureSetting.DOORS_AND_SWITCHES);
    }

    @Override
    public void setCanUseDoorsAndSwitches(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.DOORS_AND_SWITCHES);
        } else {
            this.flags.remove(AdventureSetting.DOORS_AND_SWITCHES);
        }
    }

    @Override
    public boolean canOpenContainers() {
        return this.flags.contains(AdventureSetting.OPEN_CONTAINERS);
    }

    @Override
    public void setCanOpenContainers(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.OPEN_CONTAINERS);
        } else {
            this.flags.remove(AdventureSetting.OPEN_CONTAINERS);
        }
    }

    @Override
    public boolean canAttackPlayers() {
        return this.flags.contains(AdventureSetting.ATTACK_PLAYERS);
    }

    @Override
    public void setCanAttackPlayers(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.ATTACK_PLAYERS);
        } else {
            this.flags.remove(AdventureSetting.ATTACK_PLAYERS);
        }
    }

    @Override
    public boolean canAttackMobs() {
        return this.flags.contains(AdventureSetting.ATTACK_MOBS);
    }

    @Override
    public void setCanAttackMobs(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.ATTACK_MOBS);
        } else {
            this.flags.remove(AdventureSetting.ATTACK_MOBS);
        }
    }

    @Override
    public boolean isOperator() {
        return this.flags.contains(AdventureSetting.OPERATOR);
    }

    @Override
    public void setIsOperator(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.OPERATOR);
        } else {
            this.flags.remove(AdventureSetting.OPERATOR);
        }
    }

    @Override
    public boolean canTeleport() {
        return this.flags.contains(AdventureSetting.TELEPORT);
    }

    @Override
    public void setCanTeleport(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.TELEPORT);
        } else {
            this.flags.remove(AdventureSetting.TELEPORT);
        }
    }

    @Override
    public CommandPermission getCommandPermission() {
        return this.commandPermission;
    }

    @Override
    public void setCommandPermission(CommandPermission commandPermission) {
        this.commandPermission = commandPermission;
    }

    @Override
    public PlayerPermission getPlayerPermission() {
        return this.playerPermission;
    }

    @Override
    public void setPlayerPermission(PlayerPermission playerPermission) {
        this.playerPermission = playerPermission;
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
