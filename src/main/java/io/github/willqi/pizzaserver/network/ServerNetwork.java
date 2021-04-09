package io.github.willqi.pizzaserver.network;

import com.nukkitx.protocol.bedrock.*;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import io.github.willqi.pizzaserver.Server;
import io.github.willqi.pizzaserver.network.handlers.PlayerInitializationPacketHandler;
import io.github.willqi.pizzaserver.network.handlers.PlayerPacketHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerNetwork implements BedrockServerEventHandler {

    private BedrockServer bedrockServer;
    private final Server server;
    private final BedrockPong pong = new BedrockPong();

    private final Map<BedrockServerSession, PacketQueueManager> incomingPacketQueues = new ConcurrentHashMap<>();
    private final Map<BedrockServerSession, PacketQueueManager> outgoingPacketQueues = new ConcurrentHashMap<>();
    private final Map<BedrockServerSession, BedrockPacketHandler> packetHandlers = new ConcurrentHashMap<>();

    public ServerNetwork(Server server) {
        this.server = server;
    }

    /**
     * Called internally to start the Bedrock server
     * @param ip
     * @param port
     */
    public void boot(String ip, int port) {

        this.pong.setMotd(this.server.getMotd());
        this.pong.setEdition("MCPE");
        this.pong.setPlayerCount(server.getPlayerCount());
        this.pong.setMaximumPlayerCount(server.getMaximumPlayerCount());
        this.pong.setIpv4Port(this.server.getPort());
        this.pong.setIpv6Port(this.server.getPort());
        this.pong.setProtocolVersion(ServerProtocol.LATEST_SUPPORTED_PROTOCOL);

        this.bedrockServer = new BedrockServer(new InetSocketAddress(ip, port));
        this.bedrockServer.setHandler(this);
        this.bedrockServer.bind().join();
    }

    /**
     * Called internally to stop the Bedrock server
     */
    public void stop() {
        this.bedrockServer.close("Server Shutdown");
        this.packetHandlers.clear();
        this.incomingPacketQueues.clear();
        this.outgoingPacketQueues.clear();
    }

    public void queueClientboundPacket(BedrockServerSession session, BedrockPacket packet) {
        this.outgoingPacketQueues.get(session).queue(packet);
    }

    public void queueServerboundPacket(BedrockServerSession session, BedrockPacket packet) {
        this.incomingPacketQueues.get(session).queue(packet);
    }

    /**
     * Send all of the packets queued for this tick to the respective clients
     * Called internally on every tick.
     */
    public void executeClientboundQueue() {
        Map<BedrockServerSession, List<BedrockPacket>> packetsToProcess = new HashMap<>();
        synchronized (this.outgoingPacketQueues) {
            for (Map.Entry<BedrockServerSession, PacketQueueManager> entry : this.outgoingPacketQueues.entrySet()) {
                packetsToProcess.put(entry.getKey(), entry.getValue().clear());
            }
        }

        for (Map.Entry<BedrockServerSession, List<BedrockPacket>> entry : packetsToProcess.entrySet()) {
            for (BedrockPacket packet : entry.getValue()) {
                entry.getKey().sendPacket(packet);
            }
        }
    }

    /**
     * Read all of the packets queued to be processed.
     * Called internally on every tick.
     */
    public void executeServerboundQueue() {
        Map<BedrockServerSession, List<BedrockPacket>> packetsToProcess = new HashMap<>();
        synchronized (this.incomingPacketQueues) {
            for (Map.Entry<BedrockServerSession, PacketQueueManager> entry : this.incomingPacketQueues.entrySet()) {
                packetsToProcess.put(entry.getKey(), entry.getValue().clear());
            }
        }

        for (Map.Entry<BedrockServerSession, List<BedrockPacket>> entry : packetsToProcess.entrySet()) {
            BedrockPacketHandler handler = this.getPacketHandler(entry.getKey());
            for (BedrockPacket packet : entry.getValue()) {
                Optional<Method> method = Arrays.stream(handler.getClass().getMethods())
                        .filter(m -> m.getName().equals("handle") && m.getParameterTypes()[0].getName().equals(packet.getClass().getName()))
                        .findAny();
                method.ifPresent(m -> {
                    try {
                        m.invoke(handler, packet);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        // TODO: proper error handling.
                    }
                });
            }
        }
    }

    public void setPacketHandler(BedrockServerSession session, BedrockPacketHandler handler) {
        this.packetHandlers.put(session, handler);
    }

    public BedrockPacketHandler getPacketHandler(BedrockServerSession session) {
        return this.packetHandlers.get(session);
    }

    @Override
    public boolean onConnectionRequest(InetSocketAddress address, InetSocketAddress realAddress) {
        return true;
    }

    @Override
    public boolean onConnectionRequest(InetSocketAddress address) {
        return true;
    }

    @Override
    public BedrockPong onQuery(InetSocketAddress inetSocketAddress) {
        this.pong.setPlayerCount(server.getPlayerCount());
        return this.pong;
    }

    @Override
    public void onSessionCreation(BedrockServerSession bedrockServerSession) {
        this.incomingPacketQueues.put(bedrockServerSession, new PacketQueueManager());
        this.outgoingPacketQueues.put(bedrockServerSession, new PacketQueueManager());
        this.setPacketHandler(bedrockServerSession, new PlayerInitializationPacketHandler(bedrockServerSession, this.server));
        bedrockServerSession.setPacketHandler(new PlayerPacketHandler(bedrockServerSession, this.server));
        bedrockServerSession.addDisconnectHandler(disconnectReason -> {
            this.incomingPacketQueues.remove(bedrockServerSession);
            this.outgoingPacketQueues.remove(bedrockServerSession);
            this.packetHandlers.remove(bedrockServerSession);
        });
    }

}
