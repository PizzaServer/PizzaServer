package io.github.pizzaserver.server.network;

import com.nukkitx.protocol.bedrock.*;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.network.protocol.PlayerSession;
import io.github.pizzaserver.api.event.type.server.ServerPongUpdateEvent;
import io.github.pizzaserver.server.network.protocol.ServerProtocol;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class BedrockNetworkServer {

    private final ImplServer server;

    private BedrockServer bedrockServer;
    private volatile BedrockPong pong;

    private InetSocketAddress address;


    public BedrockNetworkServer(ImplServer server) {
        this.server = server;
    }

    public ImplServer getPizzaServer() {
        return this.server;
    }

    public void stop() {
        if (this.bedrockServer == null) {
            throw new AssertionError("Tried to stop BedrockServer when server was not booted.");
        }
        this.bedrockServer.close();
    }

    public void boot(String ip, int port) throws ExecutionException, InterruptedException {
        this.address = new InetSocketAddress(ip, port);
        this.getPizzaServer().getLogger().info("Booting server up on " + ip + ":" + port);
        this.bedrockServer = new BedrockServer(this.address);
        this.bedrockServer.setHandler(new BedrockServerEventHandler() {
            @Override
            public boolean onConnectionRequest(InetSocketAddress address) {
                return true;
            }

            @Override
            public BedrockPong onQuery(InetSocketAddress inetSocketAddress) {
                return BedrockNetworkServer.this.getPong();
            }

            @Override
            public void onSessionCreation(BedrockServerSession connection) {
                PlayerSession playerSession = new PlayerSession(connection);
                BedrockNetworkServer.this.getPizzaServer().registerSession(playerSession);

                connection.setBatchHandler((bedrockSession, byteBuf, packets) -> {
                    for (BedrockPacket packet : packets) {
                        playerSession.queueIncomingPacket(packet);
                    }
                });
                connection.addDisconnectHandler(disconnectReason -> BedrockNetworkServer.this.getPizzaServer().getScheduler().prepareTask(() -> {
                    BedrockNetworkServer.this.getPizzaServer().unregisterSession(playerSession);
                    if (playerSession.getPlayer() != null) {
                        playerSession.getPlayer().onDisconnect();
                        BedrockNetworkServer.this.updatePong();
                    }
                }).schedule());
            }
        });
        this.bedrockServer.bind().get();
        this.updatePong();
    }

    public void updatePong() {
        BedrockPong pong = new BedrockPong();
        pong.setEdition("MCPE");
        pong.setGameType("SURVIVAL");
        pong.setMotd(this.server.getMotd());
        pong.setSubMotd(this.server.getMotd());
        pong.setPlayerCount(this.server.getPlayerCount());
        pong.setMaximumPlayerCount(this.server.getMaximumPlayerCount());
        pong.setProtocolVersion(-1);
        pong.setVersion("");
        pong.setIpv4Port(this.address.getPort());
        pong.setIpv6Port(this.address.getPort());

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

}
