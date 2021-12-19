package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.commands.CommandPermissionLevel;
import io.github.willqi.pizzaserver.api.player.data.PlayerPermissionLevel;

import java.util.HashSet;
import java.util.Set;

public class AdventureSettingsPacket extends BaseBedrockPacket {

    public static final int ID = 0x37;

    private long uniqueEntityRuntimeId;
    private CommandPermissionLevel commandPermissionLevel;
    private PlayerPermissionLevel playerPermissionLevel;
    private Set<Flag> flags = new HashSet<>();


    public AdventureSettingsPacket() {
        super(ID);
    }

    public long getUniqueEntityRuntimeId() {
        return this.uniqueEntityRuntimeId;
    }

    public void setUniqueEntityRuntimeId(long uniqueEntityRuntimeId) {
        this.uniqueEntityRuntimeId = uniqueEntityRuntimeId;
    }

    public CommandPermissionLevel getCommandPermissionLevel() {
        return this.commandPermissionLevel;
    }

    public void setCommandPermissionLevel(CommandPermissionLevel commandPermissionLevel) {
        this.commandPermissionLevel = commandPermissionLevel;
    }

    public PlayerPermissionLevel getPlayerPermissionLevel() {
        return this.playerPermissionLevel;
    }

    public void setPlayerPermissionLevel(PlayerPermissionLevel playerPermissionLevel) {
        this.playerPermissionLevel = playerPermissionLevel;
    }

    public Set<Flag> getFlags() {
        return this.flags;
    }

    public void setFlags(Set<Flag> flags) {
        this.flags = flags;
    }

    public void addFlag(Flag flag) {
        this.flags.add(flag);
    }

    public boolean hasFlag(Flag flag) {
        return this.flags.contains(flag);
    }


    public enum Flag {
        WORLD_IMMUTABLE,
        AUTO_JUMP,
        NO_CLIP,
        CAN_FLY,
        FLYING,
        WORLD_BUILDER,
        MUTED,
        CAN_MINE,
        CAN_BUILD,
        CAN_USE_DOORS_AND_SWITCHES,
        CAN_OPEN_CONTAINERS,
        CAN_ATTACK_PLAYERS,
        CAN_ATTACK_MOBS,
        IS_OPERATOR,
        CAN_TELEPORT
    }

}
