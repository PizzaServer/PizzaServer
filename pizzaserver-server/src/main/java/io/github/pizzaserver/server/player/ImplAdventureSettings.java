package io.github.pizzaserver.server.player;

import org.cloudburstmc.protocol.bedrock.data.AdventureSetting;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;
import org.cloudburstmc.protocol.bedrock.packet.AdventureSettingsPacket;
import io.github.pizzaserver.api.player.AdventureSettings;
import io.github.pizzaserver.api.player.Player;

import java.util.HashSet;
import java.util.Set;

public class ImplAdventureSettings implements AdventureSettings {

    protected final Player player;

    protected final Set<AdventureSetting> flags = new HashSet<AdventureSetting>() {
        {
            this.add(AdventureSetting.BUILD);
            this.add(AdventureSetting.MINE);
            this.add(AdventureSetting.DOORS_AND_SWITCHES);
            this.add(AdventureSetting.OPEN_CONTAINERS);
            this.add(AdventureSetting.ATTACK_PLAYERS);
            this.add(AdventureSetting.ATTACK_MOBS);
        }
    };

    protected CommandPermission commandPermission = CommandPermission.ANY;
    protected PlayerPermission playerPermission = PlayerPermission.MEMBER;


    public ImplAdventureSettings(Player player) {
        this.player = player;
    }

    public Set<AdventureSetting> getSettings() {
        return new HashSet<>(this.flags);
    }

    @Override
    public boolean isWorldImmutable() {
        return this.flags.contains(AdventureSetting.WORLD_IMMUTABLE);
    }

    @Override
    public AdventureSettings setWorldImmutable(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.WORLD_IMMUTABLE);
        } else {
            this.flags.remove(AdventureSetting.WORLD_IMMUTABLE);
        }
        this.send();
        return this;
    }

    @Override
    public boolean canAutoJump() {
        return this.flags.contains(AdventureSetting.AUTO_JUMP);
    }

    @Override
    public AdventureSettings setAutoJump(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.AUTO_JUMP);
        } else {
            this.flags.remove(AdventureSetting.AUTO_JUMP);
        }
        this.send();
        return this;
    }

    @Override
    public boolean hasNoClip() {
        return this.flags.contains(AdventureSetting.NO_CLIP);
    }

    @Override
    public AdventureSettings setNoClip(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.NO_CLIP);
        } else {
            this.flags.remove(AdventureSetting.NO_CLIP);
        }
        this.send();
        return this;
    }

    @Override
    public boolean canFly() {
        return this.flags.contains(AdventureSetting.MAY_FLY);
    }

    @Override
    public AdventureSettings setCanFly(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.MAY_FLY);
        } else {
            this.flags.remove(AdventureSetting.MAY_FLY);
        }
        this.send();
        return this;
    }

    @Override
    public boolean isFlying() {
        return this.flags.contains(AdventureSetting.FLYING);
    }

    @Override
    public AdventureSettings setIsFlying(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.FLYING);
        } else {
            this.flags.remove(AdventureSetting.FLYING);
        }
        this.send();
        return this;
    }

    @Override
    public boolean isWorldBuilder() {
        return this.flags.contains(AdventureSetting.WORLD_BUILDER);
    }

    @Override
    public AdventureSettings setIsWorldBuilder(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.WORLD_BUILDER);
        } else {
            this.flags.remove(AdventureSetting.WORLD_BUILDER);
        }
        this.send();
        return this;
    }

    @Override
    public boolean isMuted() {
        return this.flags.contains(AdventureSetting.MUTED);
    }

    @Override
    public AdventureSettings setIsMuted(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.MUTED);
        } else {
            this.flags.remove(AdventureSetting.MUTED);
        }
        this.send();
        return this;
    }

    @Override
    public boolean canMine() {
        return this.flags.contains(AdventureSetting.MINE);
    }

    @Override
    public AdventureSettings setCanMine(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.MINE);
        } else {
            this.flags.remove(AdventureSetting.MINE);
        }
        this.send();
        return this;
    }

    @Override
    public boolean canBuild() {
        return this.flags.contains(AdventureSetting.BUILD);
    }

    @Override
    public AdventureSettings setCanBuild(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.BUILD);
        } else {
            this.flags.remove(AdventureSetting.BUILD);
        }
        this.send();
        return this;
    }

    @Override
    public boolean canUseDoorsAndSwitches() {
        return this.flags.contains(AdventureSetting.DOORS_AND_SWITCHES);
    }

    @Override
    public AdventureSettings setCanUseDoorsAndSwitches(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.DOORS_AND_SWITCHES);
        } else {
            this.flags.remove(AdventureSetting.DOORS_AND_SWITCHES);
        }
        this.send();
        return this;
    }

    @Override
    public boolean canOpenContainers() {
        return this.flags.contains(AdventureSetting.OPEN_CONTAINERS);
    }

    @Override
    public AdventureSettings setCanOpenContainers(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.OPEN_CONTAINERS);
        } else {
            this.flags.remove(AdventureSetting.OPEN_CONTAINERS);
        }
        this.send();
        return this;
    }

    @Override
    public boolean canAttackPlayers() {
        return this.flags.contains(AdventureSetting.ATTACK_PLAYERS);
    }

    @Override
    public AdventureSettings setCanAttackPlayers(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.ATTACK_PLAYERS);
        } else {
            this.flags.remove(AdventureSetting.ATTACK_PLAYERS);
        }
        this.send();
        return this;
    }

    @Override
    public boolean canAttackMobs() {
        return this.flags.contains(AdventureSetting.ATTACK_MOBS);
    }

    @Override
    public AdventureSettings setCanAttackMobs(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.ATTACK_MOBS);
        } else {
            this.flags.remove(AdventureSetting.ATTACK_MOBS);
        }
        this.send();
        return this;
    }

    @Override
    public boolean isOperator() {
        return this.flags.contains(AdventureSetting.OPERATOR);
    }

    @Override
    public AdventureSettings setIsOperator(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.OPERATOR);
        } else {
            this.flags.remove(AdventureSetting.OPERATOR);
        }
        this.send();
        return this;
    }

    @Override
    public boolean canTeleport() {
        return this.flags.contains(AdventureSetting.TELEPORT);
    }

    @Override
    public AdventureSettings setCanTeleport(boolean enabled) {
        if (enabled) {
            this.flags.add(AdventureSetting.TELEPORT);
        } else {
            this.flags.remove(AdventureSetting.TELEPORT);
        }
        this.send();
        return this;
    }

    @Override
    public CommandPermission getCommandPermission() {
        return this.commandPermission;
    }

    @Override
    public AdventureSettings setCommandPermission(CommandPermission commandPermission) {
        this.commandPermission = commandPermission;
        this.send();
        return this;
    }

    @Override
    public PlayerPermission getPlayerPermission() {
        return this.playerPermission;
    }

    @Override
    public AdventureSettings setPlayerPermission(PlayerPermission playerPermission) {
        this.playerPermission = playerPermission;
        this.send();
        return this;
    }

    public void send() {
        if (this.player.hasSpawned()) {
            AdventureSettingsPacket adventureSettingsPacket = new AdventureSettingsPacket();
            adventureSettingsPacket.setUniqueEntityId(this.player.getId());
            adventureSettingsPacket.setPlayerPermission(this.getPlayerPermission());
            adventureSettingsPacket.setCommandPermission(this.getCommandPermission());
            adventureSettingsPacket.getSettings().addAll(this.getSettings());

            this.player.sendPacket(adventureSettingsPacket);
        }
    }

}
