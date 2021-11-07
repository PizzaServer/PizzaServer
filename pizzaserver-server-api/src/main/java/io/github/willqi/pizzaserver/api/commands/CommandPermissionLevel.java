package io.github.willqi.pizzaserver.api.commands;

/**
 * Permission needed to execute a command.
 */
public enum CommandPermissionLevel {
    NORMAL,
    OPERATOR,
    HOST,
    AUTOMATION,
    ADMIN
}
