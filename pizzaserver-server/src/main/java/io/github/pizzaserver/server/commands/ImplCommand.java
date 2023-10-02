package io.github.pizzaserver.server.commands;

import io.github.pizzaserver.api.commands.Command;
import org.cloudburstmc.protocol.bedrock.data.command.*;

import java.util.*;

public abstract class ImplCommand implements Command {

    private String name;
    private String description;
    private Set<CommandData.Flag> flags = new HashSet<>();
    private CommandPermission permissions;
    private CommandEnumData aliases;
    private List<ChainedSubCommandData> subCommands;
    private ArrayList<CommandOverloadData> overloads;
    private boolean async = false;

    public ImplCommand(String name) {
        this(name, "", new String[]{}, false);
    }

    public ImplCommand(String name, String description, String[] aliases, boolean async) {
        this.name = name;
        this.description = description;
        //this.aliases = aliases;
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
        return new String[]{};
    }

    public void setAliases(String[] aliases) {
        //this.aliases = aliases;
    }

    public Set<CommandData.Flag> getFlags() {
        return flags;
    }

    public void setFlags(Set<CommandData.Flag> flags) {
        this.flags = flags;
    }

    public CommandPermission getPermission() {
        return permissions;
    }

    public void setPermission(CommandPermission permission) {
        this.permissions = permission;
    }

    public void registerParameter(CommandOverloadData commandOverloadData) {
        this.overloads.add(commandOverloadData);
    }

    @Override
    public void setParameters(CommandOverloadData[] overloads) {
        //this.overloads = overloads;
    }

    public CommandOverloadData[] getOverloads() {
        return new CommandOverloadData[0];
    }

    public void setOverloads(CommandParamData[] overloads) {
        //this.overloads = overloads;
    }

    /**
     * The {@link CommandData} class is needed by the {@link org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket}
     * to be sent to the player. Since the class is also marked as final, there's no way to extend it, so this method
     * returns an instance of one to be used in the packet
     * @return Command information for the AvailableCommandsPacket
     */
    @Override
    public CommandData asCommandData() {
        return new CommandData(
                name, description, flags, permissions, aliases, subCommands, overloads.toArray(CommandOverloadData[]::new)
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
