package io.github.pizzaserver.server.commands.defaults;

import io.github.pizzaserver.api.commands.ImplCommand;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.ImplServer;

public class AsyncTestingCommand extends ImplCommand {

    public AsyncTestingCommand() {
        super("async");
        this.setAsync(true);
    }

    @Override
    public void execute(Player player, String[] args, String s) {
        ImplServer.getInstance().getLogger().warn("Hellooo!");
    }
}
