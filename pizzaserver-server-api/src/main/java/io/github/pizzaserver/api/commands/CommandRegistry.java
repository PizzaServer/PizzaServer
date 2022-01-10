package io.github.pizzaserver.api.commands;

import com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket;

import java.util.List;
import java.util.Map;

public interface CommandRegistry {

    void register(ImplCommand command);

    void register(ImplCommand command, String label);

    void registerAll(List<ImplCommand> commands);

    void removeCommand(String name);

    ImplCommand getCommand(String name);

    Map<String, ImplCommand> getCommands();

    AvailableCommandsPacket getAvailableCommands();
}
