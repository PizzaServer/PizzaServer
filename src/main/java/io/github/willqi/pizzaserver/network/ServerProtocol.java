package io.github.willqi.pizzaserver.network;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v419.Bedrock_v419;
import com.nukkitx.protocol.bedrock.v422.Bedrock_v422;
import com.nukkitx.protocol.bedrock.v428.Bedrock_v428;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerProtocol {

    public static final int MINECRAFT_VERSION_1_16_100_PROTOCOL = 419;
    public static final int MINECRAFT_VERSION_1_16_200_PROTOCOL = 422;
    public static final int MINECRAFT_VERSION_1_16_210_PROTOCOL = 428;

    public static final int LATEST_SUPPORTED_PROTOCOL = MINECRAFT_VERSION_1_16_210_PROTOCOL;

    public static final Map<Integer, BedrockPacketCodec> SUPPORTED_PROTOCOL_CODEC = Collections.unmodifiableMap(new HashMap<Integer, BedrockPacketCodec>(){
        {
            put(MINECRAFT_VERSION_1_16_100_PROTOCOL, Bedrock_v419.V419_CODEC);
            put(MINECRAFT_VERSION_1_16_200_PROTOCOL, Bedrock_v422.V422_CODEC);
            put(MINECRAFT_VERSION_1_16_210_PROTOCOL, Bedrock_v428.V428_CODEC);
        }
    });

}
