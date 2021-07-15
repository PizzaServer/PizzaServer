package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.TextPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class V419TextPacketHandler extends ProtocolPacketHandler<TextPacket> {

    // In case a new text type is introduced in later versions
    protected final BiMap<TextPacket.TextType, Byte> typeValues = HashBiMap.create(new HashMap<TextPacket.TextType, Byte>(){
        {
            this.put(TextPacket.TextType.RAW, (byte)0);
            this.put(TextPacket.TextType.CHAT, (byte)1);
            this.put(TextPacket.TextType.TRANSLATION, (byte)2);
            this.put(TextPacket.TextType.POPUP, (byte)3);
            this.put(TextPacket.TextType.JUKEBOX_POPUP, (byte)4);
            this.put(TextPacket.TextType.TIP, (byte)5);
            this.put(TextPacket.TextType.SYSTEM, (byte)6);
            this.put(TextPacket.TextType.WHISPER, (byte)7);
            this.put(TextPacket.TextType.ANNOUNCEMENT, (byte)8);
            this.put(TextPacket.TextType.OBJECT, (byte)9);
            this.put(TextPacket.TextType.OBJECT_WHISPER, (byte)10);
        }
    });

    @Override
    public TextPacket decode(ByteBuf buffer, PacketHelper helper) {
        TextPacket textPacket = new TextPacket();

        byte textTypeByte = buffer.readByte();
        if (!this.typeValues.inverse().containsKey(textTypeByte)) {
            throw new UnsupportedOperationException("Unsupported text type: " + textTypeByte);
        }
        textPacket.setType(this.typeValues.inverse().get(textTypeByte));
        textPacket.setRequiresTranslation(buffer.readBoolean());

        boolean hasSourceName = textPacket.getType() == TextPacket.TextType.CHAT ||
                textPacket.getType() == TextPacket.TextType.WHISPER ||
                textPacket.getType() == TextPacket.TextType.ANNOUNCEMENT;
        if (hasSourceName) {
            textPacket.setSourceName(helper.readString(buffer));
        }

        textPacket.setMessage(helper.readString(buffer));

        boolean hasParameters = textPacket.getType() == TextPacket.TextType.TRANSLATION ||
                textPacket.getType() == TextPacket.TextType.POPUP ||
                textPacket.getType() == TextPacket.TextType.JUKEBOX_POPUP;
        if (hasParameters) {
            int parametersSize = VarInts.readUnsignedInt(buffer);
            List<String> parameters = new ArrayList<>(parametersSize);
            for (int i = 0; i < parametersSize; i++) {
                parameters.add(helper.readString(buffer));
            }
            textPacket.setParameters(parameters);
        }

        textPacket.setXuid(helper.readString(buffer));
        textPacket.setPlatformChatId(helper.readString(buffer));

        return textPacket;
    }

    @Override
    public void encode(TextPacket packet, ByteBuf buffer, PacketHelper helper) {
        if (!this.typeValues.containsKey(packet.getType())) {
            packet.setType(TextPacket.TextType.RAW);
        }

        buffer.writeByte(this.typeValues.get(packet.getType()));
        buffer.writeBoolean(packet.requiresTranslation());

        boolean hasSourceName = packet.getType() == TextPacket.TextType.CHAT ||
                packet.getType() == TextPacket.TextType.WHISPER ||
                packet.getType() == TextPacket.TextType.ANNOUNCEMENT;
        if (hasSourceName) {
            helper.writeString(packet.getSourceName(), buffer);
        }

        helper.writeString(packet.getMessage(), buffer);

        boolean hasParameters = packet.getType() == TextPacket.TextType.TRANSLATION ||
                packet.getType() == TextPacket.TextType.POPUP ||
                packet.getType() == TextPacket.TextType.JUKEBOX_POPUP;
        if (hasParameters) {
            VarInts.writeUnsignedInt(buffer, packet.getParameters().size());
            for (String parameter : packet.getParameters()) {
                helper.writeString(parameter, buffer);
            }
        }

        helper.writeString(packet.getXuid(), buffer);
        helper.writeString(packet.getPlatformChatId(), buffer);
    }

}
