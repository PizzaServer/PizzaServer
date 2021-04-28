package io.github.willqi.pizzaserver.player;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import io.github.willqi.pizzaserver.Server;
import io.github.willqi.pizzaserver.entity.Entity;
import io.github.willqi.pizzaserver.player.data.LoginData;

public class Player extends Entity {

    protected Server server;
    protected BedrockServerSession session;
    protected LoginData loginData;
    public Player(Server server, BedrockServerSession session, LoginData loginData) {
        this.server = server;
        this.session = session;
        this.loginData = loginData;
    }

    public Server getServer() {
        return this.server;
    }

    public LoginData getLoginData() {
        return this.loginData;
    }

    public void sendPacket(BedrockPacket packet) {
        this.getServer().getNetwork().queueClientboundPacket(this.session, packet);
    }

}
