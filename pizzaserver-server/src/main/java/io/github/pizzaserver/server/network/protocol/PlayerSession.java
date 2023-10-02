package io.github.pizzaserver.server.network.protocol;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;
import io.github.pizzaserver.server.player.ImplPlayer;
import io.netty.util.ReferenceCountUtil;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerSession {

    private final BedrockServerSession connection;
    private final ImplPacketHandlerPipeline packetHandlerPipeline = new ImplPacketHandlerPipeline();
    private MinecraftVersion version;
    private ImplPlayer player;

    private final Queue<BedrockPacket> incomingPacketsQueue = new ConcurrentLinkedQueue<>();

    public PlayerSession(BedrockServerSession connection) {
        this.connection = connection;
    }

    public BedrockServerSession getConnection() {
        return this.connection;
    }

    public ImplPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(ImplPlayer player) {
        this.player = player;
    }

    public ImplPacketHandlerPipeline getPacketHandlerPipeline() {
        return this.packetHandlerPipeline;
    }

    public MinecraftVersion getVersion() {
        return this.version;
    }

    public void setVersion(MinecraftVersion version) {
        this.version = version;
    }

    public void queueIncomingPacket(BedrockPacket packet) {
        this.incomingPacketsQueue.add(packet);
    }

    public void processIncomingPackets() {
        BedrockPacket packet;
        while ((packet = this.incomingPacketsQueue.poll()) != null) {
            this.getPacketHandlerPipeline().accept(packet);
            Server.getInstance().getLogger().info("Polled packet into packet handler pipeline");
            //ReferenceCountUtil.release(packet);
        }
    }

}
