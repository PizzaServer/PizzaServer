package io.github.pizzaserver.server.commands;

import io.github.pizzaserver.api.commands.CommandSender;

public class ImplServerSender implements CommandSender {
    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }
}
