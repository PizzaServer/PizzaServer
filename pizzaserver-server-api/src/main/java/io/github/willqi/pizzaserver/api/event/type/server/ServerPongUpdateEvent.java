package io.github.willqi.pizzaserver.api.event.type.server;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.network.BedrockPong;

/**
 * Called every time the server's pong is being updated
 */
public class ServerPongUpdateEvent extends BaseServerEvent.Cancellable {

    private BedrockPong pong;


    public ServerPongUpdateEvent(Server server, BedrockPong pong) {
        super(server);
        this.pong = pong;
    }

    public BedrockPong getPong() {
        return this.pong;
    }

    public void setPong(BedrockPong pong) {
        this.pong = pong;
    }

}
