package io.github.pizzaserver.server.commands.defaults;

import io.github.pizzaserver.api.commands.CommandSender;
import io.github.pizzaserver.api.commands.ImplCommand;

public class AsyncTestingCommand extends ImplCommand {

    public AsyncTestingCommand() {
        super("async");
        this.setAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args, String s) {
        sender.sendMessage("Hellooo!");
    }
}
