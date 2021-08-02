package io.github.willqi.pizzaserver.server.commands.defaults;

import io.github.willqi.pizzaserver.api.commands.CommandEnum;
import io.github.willqi.pizzaserver.api.commands.ImplCommand;
import io.github.willqi.pizzaserver.api.player.Player;

import java.util.LinkedHashSet;

public class SecondaryTestCommand extends ImplCommand {
    public SecondaryTestCommand() {
        super("secondtest");
        getParameters().add(new CommandEnum("YetAnotherPath", new LinkedHashSet<String>(){{
            this.add("aaaaa");
            this.add("bbbbb");
        }}));
        setDescription("No weird packetness?");

    }

    @Override
    public void execute(Player player, String[] args, String label) {
        player.sendMessage("No doubt this will fail");
    }
}
