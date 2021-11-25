package io.github.pizzaserver.server.network.protocol.versions;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v471.Bedrock_v471;

import java.io.IOException;

public class V471MinecraftVersion extends V448MinecraftVersion {

    public static final int PROTOCOL = 471;
    public static final String VERSION = "1.17.40";


    public V471MinecraftVersion() throws IOException {}

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
        return Bedrock_v471.V471_CODEC;
    }

}
