package io.github.willqi.pizzaserver.server.commands.defaults;

import io.github.willqi.pizzaserver.api.commands.ImplCommand;
import io.github.willqi.pizzaserver.api.commands.CommandEnum;
import io.github.willqi.pizzaserver.api.player.Player;

import java.util.Arrays;

public class TestCommand extends ImplCommand {

    public TestCommand() {
        super("test");
        getParameters().add(CommandEnum.COMMAND_ENUM_BOOLEAN);
    }

    @Override
    public void execute(Player player, String[] args, String label) {
        player.sendMessage("Hello! Args: " + Arrays.toString(args));
    }
}
