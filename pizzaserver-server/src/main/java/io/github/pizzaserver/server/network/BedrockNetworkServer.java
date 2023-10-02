package io.github.pizzaserver.server.network;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.event.type.server.ServerPongUpdateEvent;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.network.protocol.PlayerSession;
import io.github.pizzaserver.server.network.protocol.ServerProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.protocol.bedrock.BedrockPong;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.codec.v475.Bedrock_v475;
import org.cloudburstmc.protocol.bedrock.netty.initializer.BedrockServerInitializer;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.common.PacketSignal;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class BedrockNetworkServer {

    private final ImplServer server;

    //private BedrockServer bedrockServer;
    private Channel channel;
    private volatile BedrockPong pong;

    private InetSocketAddress address;


    public BedrockNetworkServer(ImplServer server) {
        this.server = server;
    }

    public ImplServer getPizzaServer() {
        return this.server;
    }

    public void stop() {
        if (this.channel == null) {
            throw new AssertionError("Tried to stop BedrockServer when server was not booted.");
        }
        this.channel.close();
    }

    public void boot(String ip, int port) throws ExecutionException, InterruptedException {
        this.address = new InetSocketAddress(ip, port);
        this.getPizzaServer().getLogger().info("Booting server up on " + ip + ":" + port);
        Server.getInstance().getLogger().info("Server is starting boot!");
        this.updatePong();
        this.channel = new ServerBootstrap()
                .channelFactory(RakChannelFactory.server(NioDatagramChannel.class))
                .option(RakChannelOption.RAK_ADVERTISEMENT, pong.toByteBuf())
                .group(new NioEventLoopGroup())
                .childHandler(new BedrockServerInitializer() {
                    @Override
                    protected void initSession(BedrockServerSession session) {
                        session.setLogging(true);
                        session.setCodec(Bedrock_v475.CODEC);
                        //session.setCompressionLevel(BedrockNetworkServer.this.getPizzaServer().getConfig().getNetworkCompressionLevel());
                        PlayerSession playerSession = new PlayerSession(session);
                        BedrockNetworkServer.this.getPizzaServer().registerSession(playerSession);
                        session.setPacketHandler(new BedrockPacketHandler() {
                            @Override
                            public PacketSignal handlePacket(BedrockPacket packet) {
                                playerSession.queueIncomingPacket(packet);
                                Server.getInstance().getLogger().info("Put in a packet queue");
                                return PacketSignal.HANDLED;
                            }
                        });
                    }

                })
                .bind(address)
                .syncUninterruptibly()
                .channel();
        Server.getInstance().getLogger().info("Server is booted!");
/*        this.bedrockServer = new BedrockServer(this.address, Runtime.getRuntime().availableProcessors());
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
            public void onSessionCreated(BedrockServerSession connection) {
                connection.setCompressionLevel(BedrockNetworkServer.this.getPizzaServer().getConfig().getNetworkCompressionLevel());
                PlayerSession playerSession = new PlayerSession(connection);
                BedrockNetworkServer.this.getPizzaServer().registerSession(playerSession);
                connection.setPacketHandler(new BedrockPacketHandler() {
                    @Override
                    public PacketSignal handlePacket(BedrockPacket packet) {
                        playerSession.queueIncomingPacket(packet);
                        return PacketSignal.HANDLED;
                    }
                });
*//*                connection.addDisconnectHandler(disconnectReason -> BedrockNetworkServer.this.getPizzaServer().getScheduler().prepareTask(() -> {
                    BedrockNetworkServer.this.getPizzaServer().unregisterSession(playerSession);
                    if (playerSession.getPlayer() != null) {
                        playerSession.getPlayer().onDisconnect();
                        BedrockNetworkServer.this.updatePong();
                    }
                }).schedule());*//*
            }
        });
        this.bedrockServer.bind().get();*/
    }

    public void updatePong() {
        BedrockPong pong = new BedrockPong()
                .edition("MCPE")
                .gameType("Survival")
                .motd(this.server.getMotd())
                .subMotd(this.server.getMotd())
                .playerCount(this.server.getPlayerCount())
                .maximumPlayerCount(this.server.getMaximumPlayerCount())
                .protocolVersion(ServerProtocol.LATEST_PROTOCOL_VERSION)
                .version(ServerProtocol.LATEST_GAME_VERSION)
                .ipv4Port(this.address.getPort())
                .ipv6Port(this.address.getPort());

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
