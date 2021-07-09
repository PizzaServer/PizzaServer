package io.github.willqi.pizzaserver.server.network;

import io.github.willqi.pizzaserver.commons.server.Gamemode;

public class BedrockPong {

    private final Edition edition;
    private final Gamemode gamemode;
    private final String motd;
    private final String subMotd;

    private final int protocol;
    private final String gameVersion;

    private final int onlinePlayers;
    private final int maxPlayersAllowed;

    private final int port;

    private BedrockPong(Edition edition, Gamemode gamemode, String motd, String subMotd, int protocol, String gameVersion, int onlinePlayers, int maxPlayersAllowed, int port) {
        this.edition = edition;
        this.gamemode = gamemode;
        this.motd = motd;
        this.subMotd = subMotd;

        this.protocol = protocol;
        this.gameVersion = gameVersion;

        this.onlinePlayers = onlinePlayers;
        this.maxPlayersAllowed = maxPlayersAllowed;

        this.port = port;
    }

    public Edition getEdition() {
        return this.edition;
    }

    public Gamemode getGamemode() {
        return this.gamemode;
    }

    public String getMotd() {
        return this.motd;
    }

    public String getSubMotd() {
        return this.subMotd;
    }

    public int getProtocol() {
        return this.protocol;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public int getOnlinePlayerCount() {
        return this.onlinePlayers;
    }

    public int getMaxPlayerCount() {
        return this.maxPlayersAllowed;
    }

    public int getPort() {
        return this.port;
    }


    enum Edition {
        MCPE("MCPE"),
        EDU("MCEE");

        private final String name;

        Edition(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.getName();
        }

    }


    public static class Builder {

        private Edition edition;
        private Gamemode gamemode;
        private String motd;
        private String subMotd;

        private int protocol;
        private String gameVersion;

        private int playerCount;
        private int maxPlayersAllowed;

        private int port;


        public Builder setEdition(Edition edition) {
            this.edition = edition;
            return this;
        }

        public Builder setGamemode(Gamemode gamemode) {
            this.gamemode = gamemode;
            return this;
        }

        public Builder setMotd(String motd) {
            this.motd = motd;
            return this;
        }

        public Builder setSubMotd(String motd) {
            this.subMotd = motd;
            return this;
        }

        public Builder setProtocol(int protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setGameVersion(String gameVersion) {
            this.gameVersion = gameVersion;
            return this;
        }

        public Builder setPlayerCount(int players) {
            this.playerCount = players;
            return this;
        }

        public Builder setMaximumPlayerCount(int players) {
            this.maxPlayersAllowed = players;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public BedrockPong build() {
            return new BedrockPong(
                    this.edition,
                    this.gamemode,
                    this.motd,
                    this.subMotd,
                    this.protocol,
                    this.gameVersion,
                    this.playerCount,
                    this.maxPlayersAllowed,
                    this.port
            );
        }

    }

}
