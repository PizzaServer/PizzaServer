package io.github.pizzaserver.server.network.protocol;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import io.github.pizzaserver.api.network.protocol.PacketHandlerPipeline;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;
import io.github.pizzaserver.server.player.ImplPlayer;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerSession {

    private final BedrockServerSession connection;
    private final PacketHandlerPipeline packetHandlerPipeline = new ImplPacketHandlerPipeline();
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

    public PacketHandlerPipeline getPacketHandlerPipeline() {
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
            ReferenceCountUtil.release(packet);
        }
    }

}
