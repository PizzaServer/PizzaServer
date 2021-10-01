package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.server.network.protocol.packets.TextPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class V419TextPacketHandler extends BaseProtocolPacketHandler<TextPacket> {

    // In case a new text type is introduced in later versions
    protected final BiMap<TextPacket.TextType, Byte> typeValues = HashBiMap.create(new HashMap<TextPacket.TextType, Byte>() {
        {
            this.put(TextPacket.TextType.RAW, (byte) 0);
            this.put(TextPacket.TextType.CHAT, (byte) 1);
            this.put(TextPacket.TextType.TRANSLATION, (byte) 2);
            this.put(TextPacket.TextType.POPUP, (byte) 3);
            this.put(TextPacket.TextType.JUKEBOX_POPUP, (byte) 4);
            this.put(TextPacket.TextType.TIP, (byte) 5);
            this.put(TextPacket.TextType.SYSTEM, (byte) 6);
            this.put(TextPacket.TextType.WHISPER, (byte) 7);
            this.put(TextPacket.TextType.ANNOUNCEMENT, (byte) 8);
            this.put(TextPacket.TextType.OBJECT, (byte) 9);
            this.put(TextPacket.TextType.OBJECT_WHISPER, (byte) 10);
        }
    });

    @Override
    public TextPacket decode(BasePacketBuffer buffer) {
        TextPacket textPacket = new TextPacket();

        byte textTypeByte = buffer.readByte();
        if (!this.typeValues.inverse().containsKey(textTypeByte)) {
            throw new UnsupportedOperationException("Unsupported text type: " + textTypeByte);
        }
        textPacket.setType(this.typeValues.inverse().get(textTypeByte));
        textPacket.setRequiresTranslation(buffer.readBoolean());

        boolean hasSourceName = textPacket.getType() == TextPacket.TextType.CHAT
                || textPacket.getType() == TextPacket.TextType.WHISPER
                || textPacket.getType() == TextPacket.TextType.ANNOUNCEMENT;
        if (hasSourceName) {
            textPacket.setSourceName(buffer.readString());
        }

        textPacket.setMessage(buffer.readString());

        boolean hasParameters = textPacket.getType() == TextPacket.TextType.TRANSLATION
                || textPacket.getType() == TextPacket.TextType.POPUP
                || textPacket.getType() == TextPacket.TextType.JUKEBOX_POPUP;
        if (hasParameters) {
            int parametersSize = buffer.readUnsignedVarInt();
            List<String> parameters = new ArrayList<>(parametersSize);
            for (int i = 0; i < parametersSize; i++) {
                parameters.add(buffer.readString());
            }
            textPacket.setParameters(parameters);
        }

        textPacket.setXuid(buffer.readString());
        textPacket.setPlatformChatId(buffer.readString());

        return textPacket;
    }

    @Override
    public void encode(TextPacket packet, BasePacketBuffer buffer) {
        if (!this.typeValues.containsKey(packet.getType())) {
            packet.setType(TextPacket.TextType.RAW);
        }

        buffer.writeByte(this.typeValues.get(packet.getType()));
        buffer.writeBoolean(packet.requiresTranslation());

        boolean hasSourceName = packet.getType() == TextPacket.TextType.CHAT
                || packet.getType() == TextPacket.TextType.WHISPER
                || packet.getType() == TextPacket.TextType.ANNOUNCEMENT;
        if (hasSourceName) {
            buffer.writeString(packet.getSourceName());
        }

        buffer.writeString(packet.getMessage());

        boolean hasParameters = packet.getType() == TextPacket.TextType.TRANSLATION
                || packet.getType() == TextPacket.TextType.POPUP
                || packet.getType() == TextPacket.TextType.JUKEBOX_POPUP;
        if (hasParameters) {
            buffer.writeUnsignedVarInt(packet.getParameters().size());
            for (String parameter : packet.getParameters()) {
                buffer.writeString(parameter);
            }
        }

        buffer.writeString(packet.getXuid());
        buffer.writeString(packet.getPlatformChatId());
    }

}
