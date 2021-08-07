package io.github.willqi.pizzaserver.server.network;

import com.nukkitx.network.raknet.*;
import io.github.willqi.pizzaserver.api.event.type.server.ServerPongUpdateEvent;
import io.github.willqi.pizzaserver.api.network.BedrockPong;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.commons.server.Gamemode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class BedrockNetworkServer {

    private final ImplServer server;

    private RakNetServer rakNetServer;
    private volatile BedrockPong pong;


    public BedrockNetworkServer(ImplServer server) {
        this.server = server;
    }

    public ImplServer getPizzaServer() {
        return this.server;
    }

    public void stop() {
        if (this.rakNetServer == null) {
            throw new AssertionError("Tried to stop BedrockServer when server was not booted.");
        }
        this.rakNetServer.close();
    }

    public void boot(String ip, int port) throws ExecutionException, InterruptedException {
        this.getPizzaServer().getLogger().info("Booting server up on " + ip + ":" + port);
        this.rakNetServer = new RakNetServer(new InetSocketAddress(ip, port));
        this.rakNetServer.setListener(new BedrockServerEventListener());
        this.rakNetServer.bind().get();
        this.updatePong();
    }

    public void updatePong() {
        BedrockPong pong = new BedrockPong.Builder()
                .setEdition(BedrockPong.Edition.MCPE)
                .setGamemode(Gamemode.SURVIVAL)
                .setGameVersion(ServerProtocol.GAME_VERSION)
                .setMotd(this.server.getMotd())
                .setPlayerCount(this.server.getPlayerCount())
                .setProtocol(ServerProtocol.LATEST_PROTOCOL_VERISON)
                .setSubMotd("MOTD 2")
                .setMaximumPlayerCount(this.server.getMaximumPlayerCount())
                .build();
        ServerPongUpdateEvent serverPongUpdateEvent = new ServerPongUpdateEvent(this.getPizzaServer(), pong);
        this.getPizzaServer().getEventManager().call(serverPongUpdateEvent);
        if (!serverPongUpdateEvent.isCancelled()) {
            this.setPong(serverPongUpdateEvent.getPong());
        }
    }

    public void setPong(BedrockPong pong) {
        this.pong = pong;
    }

    public BedrockPong getPong() {
        return this.pong;
    }

    private class BedrockServerEventListener implements RakNetServerListener {

        @Override
        public boolean onConnectionRequest(InetSocketAddress inetSocketAddress) {
            return true;
        }

        @Override
        public byte[] onQuery(InetSocketAddress inetSocketAddress) {
            BedrockPong pong = BedrockNetworkServer.this.getPong();
            return (pong.getEdition() + ";" +
                    pong.getMotd() + ";" +
                    pong.getProtocol() + ";" +
                    pong.getGameVersion() + ";" +
                    pong.getOnlinePlayerCount() + ";" +
                    pong.getMaxPlayerCount() + ";" +
                    BedrockNetworkServer.this.rakNetServer.getGuid() + ";" +
                    pong.getSubMotd() + ";" +
                    pong.getGamemode().getName()).getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public void onSessionCreation(RakNetServerSession rakNetServerSession) {
            BedrockClientSession session = new BedrockClientSession(BedrockNetworkServer.this, rakNetServerSession);
            rakNetServerSession.setListener(new BedrockRakNetConnectionListener(session));
            BedrockNetworkServer.this.getPizzaServer().registerSession(session);
        }

        // The Bedrock client does not seem to care about this as far as I can tell
        @Override
        public void onUnhandledDatagram(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) {}

    }

}
