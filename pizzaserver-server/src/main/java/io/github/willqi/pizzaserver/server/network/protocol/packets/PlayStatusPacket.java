package io.github.willqi.pizzaserver.server.network.protocol.packets;

public class PlayStatusPacket extends ImplBedrockPacket {

    public static final int ID = 0x02;

    private PlayStatus status;


    public PlayStatusPacket() {
        super(ID);
    }

    public PlayStatus getStatus() {
        return this.status;
    }

    public void setStatus(PlayStatus status) {
        this.status = status;
    }

    public enum PlayStatus {
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
