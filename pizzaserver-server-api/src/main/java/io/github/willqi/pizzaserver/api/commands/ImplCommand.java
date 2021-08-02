package io.github.willqi.pizzaserver.api.commands;

import io.github.willqi.pizzaserver.api.player.Player;

import java.util.*;

public abstract class ImplCommand {

    private String name;
    private String description;
    private Set<String> aliases;
    private short flags = 0;
    private int permission = 0;
    private List<CommandEnum> parameters = new ArrayList<>();

    public ImplCommand(String name) {
        this(name, "");
    }

    public ImplCommand(String name, String description) {
        this(name, description, new HashSet<>());
    }

    public ImplCommand(String name, String description, Set<String> aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public ImplCommand(String name, String description, String[] aliases) {
        this.name = name.trim().toLowerCase(Locale.ROOT);
        this.description = description;
        this.aliases = new HashSet<>(Arrays.asList(aliases));
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

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public short getFlags() {
        return flags;
    }

    public void setFlags(short flags) {
        this.flags = flags;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public List<CommandEnum> getParameters() {
        return parameters;
    }

    public void setParameters(List<CommandEnum> parameters) {
        this.parameters = parameters;
    }

    //TODO: Implement a CommandSender object, have player and console use it
    public abstract void execute(Player player, String[] args, String label);
}
