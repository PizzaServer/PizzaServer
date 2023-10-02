package io.github.pizzaserver.api.commands;

import org.cloudburstmc.protocol.bedrock.data.command.CommandData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOverloadData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;

import java.util.ArrayList;
import java.util.Set;

public interface Command {

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    String[] getAliases();

    void setAliases(String[] aliases);

    Set<CommandData.Flag> getFlags();

    void setFlags(Set<CommandData.Flag> flags);

    CommandPermission getPermission();

    void setPermission(CommandPermission permission);


    CommandOverloadData[] getOverloads();

    void setParameters(CommandOverloadData[] overloads);

    /**
     * The {@link CommandData} class is needed by the {@link org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket}
     * to be sent to the player. Since the class is also marked as final, there's no way to extend it, so this method
     * returns an instance of one to be used in the packet
     * @return Command information for the AvailableCommandsPacket
     */
    CommandData asCommandData();

    /**
     * Executes a command
     * @param sender The sender object that sent the command, the two implemented by default are
     *               the Player class and the ImplServerSender class. The only thing a CommandSender
     *               does without casting is send a message back to the receiver.
     * @param args The spare arguments in the command (excluding the command itself)
     * @param label The command name the player used (if /h is used for /help, "h" is the label)
     */
    void execute(CommandSender sender, String[] args, String label);

    /**
     * @return If the command runs on a separate thread
     */
    boolean isAsync();

    /**
     * @param async Sets the command to run on a separate thread
     */
    void setAsync(boolean async);
}
