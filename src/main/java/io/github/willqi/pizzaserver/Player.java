package io.github.willqi.pizzaserver;

import com.nukkitx.protocol.bedrock.BedrockServerSession;

public class Player {

    private Server server;
    private BedrockServerSession session;

    public Player(Server server, BedrockServerSession session) {
        this.server = server;
        this.session = session;
    }

}
