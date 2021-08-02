package io.github.willqi.pizzaserver.api.commands;

import java.util.List;
import java.util.Map;

public interface CommandMap {

    void register(ImplCommand command);

    void register(ImplCommand command, String label);

    void registerAll(List<ImplCommand> commands);

    void removeCommand(String name);

    ImplCommand getCommand(String name);

    Map<String, ImplCommand> getCommands();

}
