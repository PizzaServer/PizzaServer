package io.github.pizzaserver.server.commands;

import com.nukkitx.protocol.bedrock.data.command.*;
import io.github.pizzaserver.api.commands.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class ImplCommand implements Command {

    private String name;
    private String description;
    private String[] aliases;
    private ArrayList<CommandData.Flag> flags = new ArrayList<>();
    private int permission = 0;
    private boolean async = false;

    /**
     * This multidimensional array is where parameters are made for commands
     * The first index signifies what branch the parameter should be related to
     * The second index signifies what position the parameter should be in (Show as first parameter, second, etc.)
     * The {@link CommandParamData#getPostfix()} variable should be set as null, unless the param type is an integer
     * If it is not null and the type is not a numerical value, the client will crash
     */
    public CommandParamData[][] overloads = new CommandParamData[0][0];

    public ImplCommand(String name) {
        this(name, "");
    }

    public ImplCommand(String name, String description) {
        this(name, description, new String[0]);
    }

    public ImplCommand(String name, String description, String[] aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public ImplCommand(String name, String description, String[] aliases, boolean async) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.async = async;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim().toLowerCase(Locale.ROOT);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public ArrayList<CommandData.Flag> getFlags() {
        return flags;
    }

    public void setFlags(ArrayList<CommandData.Flag> flags) {
        this.flags = flags;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    /**
     * This is a long list of helper functions for making your commands auto-completable
     * @param path The path of the command to take (take execute as an example: execute as/at (as = path 0, at = path 1)
     * @param position The position of the parameter in a list (execute at (position=0) xCoord (position=1) yCoord (position=2...)
     * @param name Name of the parameter (parameter0)
     * @param values Like an alias of the name (parameter0, parameter0Alias). I believe the name needs to be included in these values
     */
    public void registerParameter(int path, int position, String name, String[] values) {
        registerParameter(path, position,  new CommandParamData(name, true, new CommandEnumData(name, values, true), CommandParam.STRING, null, new ArrayList<>()));
    }
    public void registerParameter(int path, int position, String name, String[] values, CommandParam paramType) {
        registerParameter(path, position,  new CommandParamData(name, true, new CommandEnumData(name, values, true), paramType, null, new ArrayList<>()));
    }
    public void registerParameter(int path, int position, String name, String[] values, CommandParam paramType, boolean optional) {
        registerParameter(path, position,  new CommandParamData(name, optional, new CommandEnumData(name, values, true), paramType, null, new ArrayList<>()));
    }

    public void registerParameter(int path, int position, String name, CommandEnumData commandEnum) {
        registerParameter(path, position,  new CommandParamData(name, true, commandEnum, CommandParam.STRING, null, new ArrayList<>()));
    }
    public void registerParameter(int path, int position, String name, CommandEnumData commandEnum, CommandParam paramType) {
        registerParameter(path, position, new CommandParamData(name, true, commandEnum, paramType, null, new ArrayList<>()));
    }
    public void registerParameter(int path, int position, String name, CommandEnumData commandEnum, CommandParam paramType, boolean optional) {
        registerParameter(path, position, new CommandParamData(name, optional, commandEnum, paramType, null, new ArrayList<>()));
    }
    public void registerParameter(int path, int position, String name, CommandEnumData commandEnum, CommandParam paramType, boolean optional, String postFix) {
        registerParameter(path, position, new CommandParamData(name, optional, commandEnum, paramType, postFix, new ArrayList<>()));
    }
    public void registerParameter(int path, int position, String name, CommandEnumData commandEnum, CommandParam paramType, boolean optional, List<CommandParamOption> options) {
        registerParameter(path, position, new CommandParamData(name, optional, commandEnum, paramType, null, options));
    }
    public void registerParameter(int path, int position, String name, CommandEnumData commandEnum, CommandParam paramType, boolean optional, String postFix, List<CommandParamOption> options) {
        registerParameter(path, position, new CommandParamData(name, optional, commandEnum, paramType, postFix, options));
    }
    public void registerParameter(int path, int position, CommandParamData commandParamData) {
        if(path > overloads.length-1) {
            CommandParamData[][] newOverloads = new CommandParamData[path+1][];
            System.arraycopy(overloads, 0, newOverloads, 0, overloads.length);
            for(int i = 0; i < newOverloads.length; i++) {
                if(newOverloads[i] == null)
                    newOverloads[i] = new CommandParamData[0];
            }
            overloads = newOverloads;
        }

        if(position > overloads[path].length-1) {
            CommandParamData[] newPos = new CommandParamData[position+1];
            System.arraycopy(overloads[path], 0, newPos, 0, overloads[path].length);
            overloads[path] = newPos;
        }

        overloads[path][position] = commandParamData;
    }

    public CommandParamData[][] getOverloads() {
        return overloads;
    }

    public void setParameters(CommandParamData[][] overloads) {
        this.overloads = overloads;
    }

    /**
     * The {@link CommandData} class is needed by the {@link com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket}
     * to be sent to the player. Since the class is also marked as final, there's no way to extend it, so this method
     * returns an instance of one to be used in the packet
     * @return Command information for the AvailableCommandsPacket
     */
    @Override
    public CommandData asCommandData() {
        return new CommandData(
                name, description, flags, permission,
                new CommandEnumData(name, addCmdName(aliases), true), overloads
        );
    }

    // For some reason, when the alias CommandEnumData doesn't contain the command's name, the command itself doesn't appear but it's aliases do
    private String[] addCmdName(String[] aliases) {
        ArrayList<String> newAliases = new ArrayList<>(Arrays.asList(aliases));
        newAliases.add(name);
        return newAliases.toArray(new String[0]);
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
