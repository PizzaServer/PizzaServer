package io.github.pizzaserver.server.commands.defaults;

import io.github.pizzaserver.api.commands.ImplCommand;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.ServerState;
import io.github.pizzaserver.server.ImplServer;

import java.util.Scanner;

public class StopCommand extends ImplCommand {
    public StopCommand() {
        super("stop", "Stops the server (safely)", new String[]{});
    }

    @Override
    public void execute(Player player, String[] strings, String s) {
        ImplServer.getInstance().running = false;
        ImplServer.getInstance().state = ServerState.STOPPING;
    }
}
