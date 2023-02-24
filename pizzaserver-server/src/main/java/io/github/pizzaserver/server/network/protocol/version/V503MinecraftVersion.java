package io.github.pizzaserver.server.network.protocol.version;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v503.Bedrock_v503;
import io.github.pizzaserver.server.network.utils.MinecraftNamespaceComparator;

import java.io.*;
import java.util.*;

public class V503MinecraftVersion extends V486MinecraftVersion {

    public static final int PROTOCOL = 503;
    public static final String VERSION = "1.18.30";

    public V503MinecraftVersion() throws IOException {}

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
        return Bedrock_v503.V503_CODEC;
    }

    @Override
    protected Comparator<String> getBlockIdComparator() {
        return MinecraftNamespaceComparator::compareFNV;
    }

}
