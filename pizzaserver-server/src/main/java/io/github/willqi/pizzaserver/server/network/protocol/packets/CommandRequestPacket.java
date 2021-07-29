package io.github.willqi.pizzaserver.server.network.protocol.packets;

import java.util.UUID;

public class CommandRequestPacket extends ImplBedrockPacket {

    public static final int ID = 0x4d;
    private String command;
    private int commandType;
    private UUID uuid;
    private String requestID;
    private boolean unknownBoolean;

    public CommandRequestPacket() {
        super(ID);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public boolean isUnknownBoolean() {
        return unknownBoolean;
    }

    public void setUnknownBoolean(boolean unknownBoolean) {
        this.unknownBoolean = unknownBoolean;
    }
}
