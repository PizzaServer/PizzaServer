package io.github.willqi.pizzaserver.player;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import io.github.willqi.pizzaserver.Server;
import io.github.willqi.pizzaserver.player.data.LoginData;

public class Player {

    private Server server;
    private BedrockServerSession session;

    public Player(Server server, BedrockServerSession session, LoginData loginData) {
        this.server = server;
        this.session = session;
    }

}
