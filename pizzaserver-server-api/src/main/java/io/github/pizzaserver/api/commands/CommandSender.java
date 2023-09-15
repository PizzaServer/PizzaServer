package io.github.pizzaserver.api.commands;

import io.github.pizzaserver.api.Server;

public interface CommandSender {
    void sendMessage(String message);
    void sendError(String message);
    void sendError(String message, Exception exception);
    String getName();

    Server getServer();
}
