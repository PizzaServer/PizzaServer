package io.github.pizzaserver.server.commands.defaults;

import io.github.pizzaserver.api.commands.CommandSender;
import io.github.pizzaserver.api.utils.ServerState;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.commands.ImplCommand;

public class StopCommand extends ImplCommand {
    public StopCommand() {
        super("stop", "Stops the server (safely)", new String[]{});
    }

    @Override
    public void execute(CommandSender sender, String[] strings, String s) {
        ImplServer.getInstance().stop();
    }
}
