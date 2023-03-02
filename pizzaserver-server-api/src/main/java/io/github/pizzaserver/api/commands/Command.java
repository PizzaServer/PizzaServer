package io.github.pizzaserver.api.commands;

import com.nukkitx.protocol.bedrock.data.command.*;
import io.github.pizzaserver.api.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface Command {

    /**
     * This multidimensional array is where parameters are made for commands
     * The first index signifies what branch the parameter should be related to
     * The second index signifies what position the parameter should be in (Show as first parameter, second, etc.)
     * The {@link CommandParamData#getPostfix()} variable should be set as null, unless the param type is an integer
     * If it is not null and the type is not a numerical value, the client will crash
     */
    CommandParamData[][] overloads = new CommandParamData[0][0];

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    String[] getAliases();

    void setAliases(String[] aliases);

    ArrayList<CommandData.Flag> getFlags();

    void setFlags(ArrayList<CommandData.Flag> flags);

    int getPermission();

    void setPermission(int permission);

    void registerParameter(int path, int position, CommandParamData commandParamData);

    CommandParamData[][] getOverloads();

    void setParameters(CommandParamData[][] overloads);

    /**
     * The {@link CommandData} class is needed by the {@link com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket}
     * to be sent to the player. Since the class is also marked as final, there's no way to extend it, so this method
     * returns an instance of one to be used in the packet
     * @return Command information for the AvailableCommandsPacket
     */
    CommandData asCommandData();

    void execute(Player player, String[] args, String label);

    boolean isAsync();

    void setAsync(boolean async);
}
