package io.github.pizzaserver.server.commands.defaults;

import io.github.pizzaserver.api.commands.CommandSender;
import io.github.pizzaserver.server.commands.ImplCommand;

public class AsyncTestingCommand extends ImplCommand {

    /**
     * Small and quick example of an async command
     */
    public AsyncTestingCommand() {
        super("async");
        this.setAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args, String s) {
        if(sender != null)
            sender.sendMessage("Hellooo!");
    }
}
