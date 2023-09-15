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

    /**
     * This is called when CTRL + C is pressed, like calling Server#stop()
     */
    void shutdown();

    /**
     * This method should be used for what to do when Server#stop() is called
     */
    void close();
}
