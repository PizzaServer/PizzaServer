package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.errorprone.annotations.Var;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.AvailableCommandsPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.ObjIntConsumer;

public class V419AvailableCommandsPacket extends BaseProtocolPacketHandler<AvailableCommandsPacket> {

    public static final int ARG_FLAG_VALID = 0x100000;
    public static final int ARG_FLAG_ENUM = 0x200000;
    public static final int ARG_FLAG_POSTFIX = 0x1000000;

    private static final ObjIntConsumer<ByteBuf> WRITE_BYTE = (s, v) -> s.writeByte((byte) v);
    private static final ObjIntConsumer<ByteBuf> WRITE_SHORT = ByteBuf::writeShortLE;
    private static final ObjIntConsumer<ByteBuf> WRITE_INT = ByteBuf::writeIntLE;

    @Override
    public void encode(AvailableCommandsPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        LinkedHashSet<String> enumValuesSet = new LinkedHashSet<>();
        LinkedHashSet<String> postFixesSet = new LinkedHashSet<>();
        LinkedHashSet<String> enumsSet = new LinkedHashSet<>();

        enumsSet.add("test");// Enum Name
        enumValuesSet.addAll(new ArrayList<String>(){{this.add("oponeddd");this.add("opotwoddd");}}); // Enum Values

        List<String> enumValues = new ArrayList<>(enumValuesSet);
        List<String> enums = new ArrayList<>(enumsSet); //CommandEnum
        List<String> postFixes = new ArrayList<>(postFixesSet);

        VarInts.writeUnsignedInt(buffer, enumValues.size());
        enumValues.forEach(enumValue -> helper.writeString(enumValue, buffer));

        VarInts.writeUnsignedInt(buffer, postFixes.size());
        postFixes.forEach(postFix -> helper.writeString(postFix, buffer));

        ObjIntConsumer<ByteBuf> indexWriter;
        if (enumValues.size() < 256) {
            indexWriter = WRITE_BYTE;
        } else if (enumValues.size() < 65536) {
            indexWriter = WRITE_SHORT;
        } else {
            indexWriter = WRITE_INT;
        }

        VarInts.writeUnsignedInt(buffer, enums.size());
        enums.forEach((cmdEnum) -> {
            helper.writeString(cmdEnum, buffer);

            List<String> values = new ArrayList<>(enumValues);
            VarInts.writeUnsignedInt(buffer, values.size());

            for (String val : values) {
                int i = enumValues.indexOf(val);

                if (i < 0) {
                    throw new IllegalStateException("Enum value '" + val + "' not found");
                }

                indexWriter.accept(buffer, i);
            }
        });

        VarInts.writeUnsignedInt(buffer, 1); //Commands Size
        helper.writeString("nameeee", buffer);
        helper.writeString("This description worked nicely last time", buffer);
        buffer.writeShortLE(0); //Flags
        buffer.writeByte(0); //Permission

        buffer.writeIntLE(0); //Aliases
        VarInts.writeUnsignedInt(buffer, 0); //Overloads Size
/*        VarInts.writeUnsignedInt(buffer, 1); //Overload Parameter Length
        helper.writeString("oponeddd", buffer);
        int type = 0;
        type |= ARG_FLAG_VALID;
        type |= ARG_FLAG_ENUM | enums.indexOf("oponeddd");

        buffer.writeIntLE(type);
        buffer.writeBoolean(false);
        buffer.writeByte(0);*/

        VarInts.writeUnsignedInt(buffer, 1);
        helper.writeString("testenum", buffer);
        VarInts.writeUnsignedInt(buffer, 1);
        helper.writeString("desc", buffer);

        VarInts.writeUnsignedInt(buffer, 0);
    }
}
