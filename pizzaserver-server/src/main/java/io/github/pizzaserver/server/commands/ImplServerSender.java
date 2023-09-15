package io.github.pizzaserver.server.commands;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.commands.CommandSender;

public class ImplServerSender implements CommandSender {

    protected final Server server;

    public ImplServerSender(Server server) {
        this.server = server;
    }

    @Override
    public void sendMessage(String message) {
        server.getLogger().info(message);
    }

    @Override
    public void sendError(String message) {
        this.server.getLogger().error(message);
    }

    @Override
    public void sendError(String message, Exception exception) {
        this.server.getLogger().error(message, exception);
    }

    @Override
    public String getName() {
        return "Server";
    }

    @Override
    public Server getServer() {
        return server;
    }
}
