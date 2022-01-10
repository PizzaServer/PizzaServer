package io.github.pizzaserver.server.commands.defaults;

import com.nukkitx.protocol.bedrock.data.command.CommandData;
import io.github.pizzaserver.api.commands.CommandEnum;
import io.github.pizzaserver.api.commands.ImplCommand;
import io.github.pizzaserver.api.player.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class TestCommand extends ImplCommand {

    public TestCommand() {
        super("test");
/*        getParameters().add(CommandEnum.COMMAND_ENUM_BOOLEAN);
        getParameters().add(new CommandEnum("secondarypath", new LinkedHashSet<String>(){{
            this.add("option1");
            this.add("option2");
        }}));*/
        setDescription("This is a test, please I beg you to work");
        setAliases(new HashSet<>(Collections.singleton("alias1")));
    }

    @Override
    public void execute(Player player, String[] args, String label) {
        player.sendMessage("Hello! Args: " + Arrays.toString(args));
    }
}
