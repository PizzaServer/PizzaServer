package io.github.pizzaserver.server.network.protocol.version;

import io.github.pizzaserver.server.network.utils.MinecraftNamespaceComparator;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503;

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
    public BedrockCodec getPacketCodec() {
        return Bedrock_v503.CODEC;
    }

    @Override
    protected Comparator<String> getBlockIdComparator() {
        return MinecraftNamespaceComparator::compareFNV;
    }

}
