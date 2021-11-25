package io.github.pizzaserver.server.network.protocol.versions;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v465.Bedrock_v465;

import java.io.IOException;

public class V465MinecraftVersion extends V448MinecraftVersion {

    public static final int PROTOCOL = 465;
    public static final String VERSION = "1.17.30";


    public V465MinecraftVersion() throws IOException {}

    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public BedrockPacketCodec getPacketCodec() {
        return Bedrock_v465.V465_CODEC;
    }

}
