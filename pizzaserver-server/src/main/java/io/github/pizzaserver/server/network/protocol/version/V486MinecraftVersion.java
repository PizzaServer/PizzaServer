package io.github.pizzaserver.server.network.protocol.version;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v486.Bedrock_v486;

import java.io.IOException;

public class V486MinecraftVersion extends V475MinecraftVersion {

    public static final int PROTOCOL = 486;
    public static final String VERSION = "1.18.10";

    public V486MinecraftVersion() throws IOException {}

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
        return Bedrock_v486.V486_CODEC;
    }

}
