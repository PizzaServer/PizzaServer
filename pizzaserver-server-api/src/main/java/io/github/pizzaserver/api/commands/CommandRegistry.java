package io.github.pizzaserver.api.commands;

import com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket;

import java.util.List;
import java.util.Map;

public interface CommandRegistry {

    void register(Command command);

    void register(Command command, String label);

    void registerAll(List<Command> commands);

    void removeCommand(String name);

    Command getCommand(String name);

    Map<String, Command> getCommands();

    AvailableCommandsPacket getAvailableCommands();
    void runAsyncCommand(Runnable runnable);
    void processConsoleCommands();

    void shutdown();
}
