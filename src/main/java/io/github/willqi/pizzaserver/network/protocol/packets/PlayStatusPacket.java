package io.github.willqi.pizzaserver.network.protocol.packets;

public class PlayStatusPacket extends BedrockPacket {

    public static final int ID = 0x02;

    private Status status;

    public PlayStatusPacket() {
        super(ID);
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        LOGIN_SUCCESS,
        OUTDATED_CLIENT,
        OUTDATED_SERVER,
        PLAYER_SPAWN,

        INVALID_TENANT,
        NOT_EDU_SERVER,
        INVALID_EDU_VERSION,

        SERVER_FULL
    }


}
