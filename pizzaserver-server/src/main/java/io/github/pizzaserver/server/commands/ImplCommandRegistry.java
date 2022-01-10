package io.github.pizzaserver.server.commands;

import com.nukkitx.protocol.bedrock.data.command.CommandData;
import com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.commands.CommandRegistry;
import io.github.pizzaserver.api.commands.ImplCommand;
import io.github.pizzaserver.server.commands.defaults.SecondaryTestCommand;
import io.github.pizzaserver.server.commands.defaults.TestCommand;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class ImplCommandRegistry implements CommandRegistry {

    private final Map<String, ImplCommand> commands = new HashMap<>();

    private static Server server;

    public ImplCommandRegistry(Server server) {
        ImplCommandRegistry.server = server;
    }

    public static void registerDefaults() {
        server.getCommandRegistry().register(new TestCommand());
        server.getCommandRegistry().register(new SecondaryTestCommand());
    }

    @Override
    public void register(ImplCommand command) {
        this.register(command, command.getName());
    }

    @Override
    public void register(ImplCommand command, String label) {
        if (label == null) label = command.getName();
        label = label.trim().toLowerCase(Locale.ROOT);

        if (!commands.containsKey(label)) {
            commands.put(label, command);
        } else {
            //TODO: Show the plugin name of the command that has been overwritten
            server.getLogger().error("A command with the name " + label + " already exists!");
        }

        for(String alias : command.getAliases()) {
            alias = alias.trim().toLowerCase(Locale.ROOT);
            if(commands.containsKey(alias))
                server.getLogger().error("A command with the name " + label + " already exists!");
        }
    }

    @Override
    public void registerAll(List<ImplCommand> commands) {
        for(ImplCommand command : commands) {
            this.register(command, command.getName());
        }
    }

    @Override
    public void removeCommand(String name) {
        if(!commands.containsKey(name)) {
            throw new NullPointerException("That command doesn't exist");
        }
        commands.remove(name);
    }

    @Override
    public ImplCommand getCommand(String name) {
        if(commands.containsKey(name)) return commands.get(name);
        return null;
    }

    @Override
    public Map<String, ImplCommand> getCommands() {
        return new HashMap<>(commands);
    }

    @Override
    public AvailableCommandsPacket getAvailableCommands() {
        AvailableCommandsPacket availableCommandsPacket = new AvailableCommandsPacket();
        for(ImplCommand implCommand : commands.values()) {
            availableCommandsPacket.getCommands().add(implCommand.asCommandData());
        }
        return availableCommandsPacket;
    }
}
