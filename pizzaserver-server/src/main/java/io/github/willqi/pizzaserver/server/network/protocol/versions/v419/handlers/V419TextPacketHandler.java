package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.api.network.protocol.packets.TextPacket;
import io.github.willqi.pizzaserver.api.utils.TextType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class V419TextPacketHandler extends BaseProtocolPacketHandler<TextPacket> {

    // In case a new text type is introduced in later versions
    protected final BiMap<TextType, Byte> typeValues = HashBiMap.create(new HashMap<TextType, Byte>() {
        {
            this.put(TextType.RAW, (byte) 0);
            this.put(TextType.CHAT, (byte) 1);
            this.put(TextType.TRANSLATION, (byte) 2);
            this.put(TextType.POPUP, (byte) 3);
            this.put(TextType.JUKEBOX_POPUP, (byte) 4);
            this.put(TextType.TIP, (byte) 5);
            this.put(TextType.SYSTEM, (byte) 6);
            this.put(TextType.WHISPER, (byte) 7);
            this.put(TextType.ANNOUNCEMENT, (byte) 8);
            this.put(TextType.OBJECT, (byte) 9);
            this.put(TextType.OBJECT_WHISPER, (byte) 10);
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

        boolean hasSourceName = textPacket.getType() == TextType.CHAT
                || textPacket.getType() == TextType.WHISPER
                || textPacket.getType() == TextType.ANNOUNCEMENT;
        if (hasSourceName) {
            textPacket.setSourceName(buffer.readString());
        }

        textPacket.setMessage(buffer.readString());

        boolean hasParameters = textPacket.getType() == TextType.TRANSLATION
                || textPacket.getType() == TextType.POPUP
                || textPacket.getType() == TextType.JUKEBOX_POPUP;
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
            packet.setType(TextType.RAW);
        }

        buffer.writeByte(this.typeValues.get(packet.getType()));
        buffer.writeBoolean(packet.requiresTranslation());

        boolean hasSourceName = packet.getType() == TextType.CHAT
                || packet.getType() == TextType.WHISPER
                || packet.getType() == TextType.ANNOUNCEMENT;
        if (hasSourceName) {
            buffer.writeString(packet.getSourceName());
        }

        buffer.writeString(packet.getMessage());

        boolean hasParameters = packet.getType() == TextType.TRANSLATION
                || packet.getType() == TextType.POPUP
                || packet.getType() == TextType.JUKEBOX_POPUP;
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
