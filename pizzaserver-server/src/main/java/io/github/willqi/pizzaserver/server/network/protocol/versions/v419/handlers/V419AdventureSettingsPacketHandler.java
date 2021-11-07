package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.commands.CommandPermissionLevel;
import io.github.willqi.pizzaserver.api.network.protocol.packets.AdventureSettingsPacket;
import io.github.willqi.pizzaserver.api.player.data.PlayerPermissionLevel;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.*;

public class V419AdventureSettingsPacketHandler extends BaseProtocolPacketHandler<AdventureSettingsPacket> {

    protected final Map<AdventureSettingsPacket.Flag, Integer> flags = new HashMap<AdventureSettingsPacket.Flag, Integer>() {
        {
            this.put(AdventureSettingsPacket.Flag.WORLD_IMMUTABLE, 0x01);

            this.put(AdventureSettingsPacket.Flag.AUTO_JUMP, 0x20);
            this.put(AdventureSettingsPacket.Flag.CAN_FLY, 0x40);
            this.put(AdventureSettingsPacket.Flag.NO_CLIP, 0x80);
            this.put(AdventureSettingsPacket.Flag.WORLD_BUILDER, 0x100);
            this.put(AdventureSettingsPacket.Flag.FLYING, 0x200);
            this.put(AdventureSettingsPacket.Flag.MUTED, 0x400);
        }
    };

    protected final Map<AdventureSettingsPacket.Flag, Integer> secondaryFlags = new HashMap<AdventureSettingsPacket.Flag, Integer>() {
        {
            this.put(AdventureSettingsPacket.Flag.CAN_MINE, 0x01);
            this.put(AdventureSettingsPacket.Flag.CAN_USE_DOORS_AND_SWITCHES, 0x02);
            this.put(AdventureSettingsPacket.Flag.CAN_OPEN_CONTAINERS, 0x04);
            this.put(AdventureSettingsPacket.Flag.CAN_ATTACK_PLAYERS, 0x08);
            this.put(AdventureSettingsPacket.Flag.CAN_ATTACK_MOBS, 0x10);
            this.put(AdventureSettingsPacket.Flag.IS_OPERATOR, 0x20);
            this.put(AdventureSettingsPacket.Flag.CAN_TELEPORT, 0x80);
            this.put(AdventureSettingsPacket.Flag.CAN_BUILD, 0x100);
        }
    };

    @Override
    public AdventureSettingsPacket decode(BasePacketBuffer buffer) {
        AdventureSettingsPacket adventureSettingsPacket = new AdventureSettingsPacket();

        Set<AdventureSettingsPacket.Flag> flags = new HashSet<>(this.readFlags(buffer));
        adventureSettingsPacket.setCommandPermissionLevel(CommandPermissionLevel.values()[buffer.readUnsignedVarInt()]);
        flags.addAll(this.readSecondaryFlags(buffer));
        adventureSettingsPacket.setPlayerPermissionLevel(PlayerPermissionLevel.values()[buffer.readUnsignedVarInt()]);
        buffer.readUnsignedVarInt();    // custom flags (but it's pointless)

        adventureSettingsPacket.setUniqueEntityRuntimeId(buffer.readLongLE());
        adventureSettingsPacket.setFlags(flags);
        return adventureSettingsPacket;
    }

    @Override
    public void encode(AdventureSettingsPacket packet, BasePacketBuffer buffer) {
        this.writeFlags(packet.getFlags(), buffer);
        buffer.writeUnsignedVarInt(packet.getCommandPermissionLevel().ordinal());
        this.writeSecondaryFlags(packet.getFlags(), buffer);
        buffer.writeUnsignedVarInt(packet.getPlayerPermissionLevel().ordinal());
        buffer.writeUnsignedVarInt(0);  // custom flags (but it's pointless)
        buffer.writeLongLE(packet.getUniqueEntityRuntimeId());
    }

    protected void writeFlags(Set<AdventureSettingsPacket.Flag> flags, BasePacketBuffer buffer) {
        int flagsValue = 0;
        for (AdventureSettingsPacket.Flag flag : flags) {
            if (this.flags.containsKey(flag)) {
                flagsValue |= this.flags.get(flag);
            }
        }
        buffer.writeUnsignedVarInt(flagsValue);
    }

    protected Set<AdventureSettingsPacket.Flag> readFlags(BasePacketBuffer buffer) {
        int flagsValue = buffer.readUnsignedVarInt();
        Set<AdventureSettingsPacket.Flag> flags = new HashSet<>();

        for (AdventureSettingsPacket.Flag flag : this.flags.keySet()) {
            int flagValue = this.flags.get(flag);
            if ((flagsValue & flagValue) != 0) {
                flags.add(flag);
            }
        }
        return flags;
    }

    protected void writeSecondaryFlags(Set<AdventureSettingsPacket.Flag> flags, BasePacketBuffer buffer) {
        int secondaryFlagsValue = 0;
        for (AdventureSettingsPacket.Flag flag : flags) {
            if (this.secondaryFlags.containsKey(flag)) {
                secondaryFlagsValue |= this.secondaryFlags.get(flag);
            }
        }
        buffer.writeUnsignedVarInt(secondaryFlagsValue);
    }

    protected Set<AdventureSettingsPacket.Flag> readSecondaryFlags(BasePacketBuffer buffer) {
        int secondaryFlagsValue = buffer.readUnsignedVarInt();
        Set<AdventureSettingsPacket.Flag> secondaryFlags = new HashSet<>();

        for (AdventureSettingsPacket.Flag flag : this.secondaryFlags.keySet()) {
            int secondaryFlagValue = this.secondaryFlags.get(flag);
            if ((secondaryFlagsValue & secondaryFlagValue) != 0) {
                secondaryFlags.add(flag);
            }
        }
        return secondaryFlags;
    }

}
