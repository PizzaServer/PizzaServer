package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.errorprone.annotations.Var;
import io.github.willqi.pizzaserver.api.commands.CommandEnum;
import io.github.willqi.pizzaserver.api.commands.CommandMap;
import io.github.willqi.pizzaserver.api.commands.ImplCommand;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.packets.AvailableCommandsPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.*;
import java.util.function.ObjIntConsumer;

public class V419AvailableCommandsPacket extends BaseProtocolPacketHandler<AvailableCommandsPacket> {

    public static final int ARG_FLAG_VALID = 0x100000;
    public static final int ARG_FLAG_ENUM = 0x200000;
    public static final int ARG_FLAG_POSTFIX = 0x1000000;

    private static final ObjIntConsumer<ByteBuf> WRITE_BYTE = ByteBuf::writeByte;
    private static final ObjIntConsumer<ByteBuf> WRITE_SHORT = ByteBuf::writeShortLE;
    private static final ObjIntConsumer<ByteBuf> WRITE_INT = ByteBuf::writeIntLE;

    @Override
    public void encode(AvailableCommandsPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        CommandMap commandMap = ImplServer.getInstance().getCommandMap();
        Map<String, ImplCommand> commands = commandMap.getCommands();

        LinkedHashSet<CommandEnum> enumsSet = new LinkedHashSet<>();
        LinkedHashSet<String> enumValuesSet = new LinkedHashSet<>();

        commands.forEach((s, implCommand) -> {
            implCommand.getParameters().forEach(commandEnum -> {
                enumsSet.add(commandEnum);
                enumValuesSet.addAll(commandEnum.getValues());
            });
        });

        List<CommandEnum> enums = new ArrayList<>(enumsSet);
        List<String> enumValues = new ArrayList<>(enumValuesSet);

        VarInts.writeUnsignedInt(buffer, enumValues.size());
        enumValues.forEach(s -> helper.writeString(s, buffer));

        VarInts.writeUnsignedInt(buffer, 0); //TODO: Suffixes, add to CommandEnum

        ObjIntConsumer<ByteBuf> indexWriter;
        if (enumValues.size() < 256) {
            indexWriter = WRITE_BYTE;
        } else if (enumValues.size() < 65536) {
            indexWriter = WRITE_SHORT;
        } else {
            indexWriter = WRITE_INT;
        }

        VarInts.writeUnsignedInt(buffer, enums.size());
        for(CommandEnum commandEnum : enums) {
            helper.writeString(commandEnum.getName(), buffer);
            VarInts.writeUnsignedInt(buffer, commandEnum.getValues().size());
            for(String enumValue : commandEnum.getValues()) {
                int index = enumValues.indexOf(enumValue);
                if (index < 0) throw new IllegalStateException("Enum value '" + enumValue + "' not found");
                indexWriter.accept(buffer, index);
            }
        }

        VarInts.writeUnsignedInt(buffer, commands.size());
        for(String name : commands.keySet()) { // Not using an enhanced for loop to count for aliases
            ImplCommand command = commands.get(name);
            helper.writeString(name, buffer);
            helper.writeString(command.getDescription(), buffer);
            //TODO: For 1.17.10 support, make the below flags write a LE short
            buffer.writeByte(command.getFlags());
            buffer.writeByte(command.getPermission());
            buffer.writeIntLE(-1);
            // The below part with parameters isn't entirely finished/accurate
            VarInts.writeUnsignedInt(buffer, command.getParameters().size()); // Overload Size
            for(CommandEnum commandEnum : command.getParameters()) {
                VarInts.writeUnsignedInt(buffer, 1); // Parameter Size
                helper.writeString(commandEnum.getName(), buffer);
                int type = 0;
                type |= ARG_FLAG_VALID;
                type |= ARG_FLAG_ENUM | enums.indexOf(commandEnum);
                buffer.writeIntLE(type);
                buffer.writeBoolean(false);
                buffer.writeByte(0);
            }
        }

        VarInts.writeUnsignedInt(buffer, 0);
        VarInts.writeUnsignedInt(buffer, 0);
    }
}
