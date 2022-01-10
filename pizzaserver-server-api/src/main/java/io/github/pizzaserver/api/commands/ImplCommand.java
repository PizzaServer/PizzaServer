package io.github.pizzaserver.api.commands;

import com.nukkitx.protocol.bedrock.data.command.CommandData;
import com.nukkitx.protocol.bedrock.data.command.CommandEnumData;
import com.nukkitx.protocol.bedrock.data.command.CommandParamData;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.player.Player;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

public abstract class ImplCommand {

    private String name;
    private String description;
    private String[] aliases;
    private short flags = 0;
    private int permission = 0;
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

    public CommandParamData[][] getOverloads() {
        return overloads;
    }

    public void setParameters(CommandParamData[][] overloads) {
        this.overloads = overloads;
    }

    public CommandData asCommandData() {
        return new CommandData(
                name, description, new ArrayList<>(), 0,
                new CommandEnumData(name, addCmdName(aliases), true), overloads
        );
    }

    // For some reason, when the alias CommandEnumData doesn't contain the command's name, the command itself doesn't appear but it's aliases do
    private String[] addCmdName(String[] aliases) {
        ArrayList<String> newAliases = new ArrayList<>(Arrays.asList(aliases));
        newAliases.add(name);
        return newAliases.toArray(new String[0]);
    }

    //TODO: Implement a CommandSender object, have player and console use it
    public abstract void execute(Player player, String[] args, String label);
}
