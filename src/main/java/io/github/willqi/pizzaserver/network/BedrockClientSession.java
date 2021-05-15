package io.github.willqi.pizzaserver.network;

import com.nukkitx.network.VarInts;
import com.nukkitx.network.raknet.RakNetServerSession;
import com.nukkitx.network.util.DisconnectReason;
import io.github.willqi.pizzaserver.Server;
import io.github.willqi.pizzaserver.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.network.protocol.packets.LoginPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.PacketRegistry;
import io.github.willqi.pizzaserver.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.network.utils.Zlib;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BedrockClientSession {

    private final BedrockServer server;
    private final RakNetServerSession serverSession;

    private PacketRegistry packetRegistry;
    private volatile BedrockPacketHandler handler;

    private final List<BedrockPacket> queuedPackets = Collections.synchronizedList(new LinkedList<>());

    public BedrockClientSession(BedrockServer server, RakNetServerSession rakNetServerSession) {
        this.server = server;
        this.serverSession = rakNetServerSession;
    }

    public BedrockServer getServer() {
        return this.server;
    }

    public void setPacketRegistry(PacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    public void setPacketHandler(BedrockPacketHandler packetHandler) {
        this.handler = packetHandler;
    }

    public void sendPacket(BedrockPacket packet) {
        ByteBuf rakNetBuffer = ByteBufAllocator.DEFAULT.buffer();
        rakNetBuffer.writeByte(0xfe); // Game packet


        ByteBuf packetBuffer = ByteBufAllocator.DEFAULT.buffer();
        packetBuffer.writeByte(packet.getPacketId());
        ((ProtocolPacketHandler<BedrockPacket>)this.packetRegistry.getPacketHandler(packet.getPacketId())).encode(packet, packetBuffer);

        ByteBuf packetWrapperBuffer = ByteBufAllocator.DEFAULT.buffer();
        VarInts.writeUnsignedInt(packetWrapperBuffer, packetBuffer.readableBytes());
        packetWrapperBuffer.writeBytes(packetBuffer);

        rakNetBuffer.writeBytes(Zlib.compressBuffer(packetWrapperBuffer));
        this.serverSession.send(rakNetBuffer);
    }

    public void processPackets() {
        List<BedrockPacket> packets;
        synchronized (this.queuedPackets) {
            packets = new ArrayList<>(this.queuedPackets);
            this.queuedPackets.clear();
        }
        for (BedrockPacket packet : packets) {
            if (this.handler != null) {
                this.handler.onPacket(packet);

                // Now we call the specific packet handler
                try {
                    Method method = this.handler.getClass().getMethod("onPacket", packet.getClass());
                    method.invoke(this.handler, packet);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
                    Server.getInstance().getLogger().error("Failed to call packet handler for " + packet);
                    Server.getInstance().getLogger().error(exception);
                    exception.printStackTrace();
                }

            }
        }
    }

    public void handlePacket(int packetId, ByteBuf buffer) {
        if (this.packetRegistry != null) {
            BedrockPacket bedrockPacket = this.packetRegistry.getPacketHandler(packetId).decode(buffer);
            this.queuedPackets.add(bedrockPacket);
        } else if (packetId == LoginPacket.ID) {
            // Parse the protocol the client uses in order to parse future packets.
            int index = buffer.readerIndex();
            int protocol = buffer.readInt();
            buffer.setIndex(index, buffer.writerIndex());

            // Parse login packet given protocol if available
            if (ServerProtocol.PACKET_REGISTRIES.containsKey(protocol)) {
                this.packetRegistry = ServerProtocol.PACKET_REGISTRIES.get(protocol);
                BedrockPacket loginPacket;
                try {
                    loginPacket = this.packetRegistry.getPacketHandler(packetId).decode(buffer);
                } catch (RuntimeException exception) {
                    Server.getInstance().getLogger().error("Error while decoding packet from client.");
                    Server.getInstance().getLogger().error(exception);
                    this.serverSession.disconnect(DisconnectReason.BAD_PACKET);
                    return;
                }
                this.queuedPackets.add(loginPacket);
            } else {
                LoginPacket loginPacket = new LoginPacket();
                loginPacket.setProtocol(protocol);
                this.queuedPackets.add(loginPacket);
            }
        } else {
            // Client tried to send us a packet without sending the login packet first.
            this.serverSession.disconnect(DisconnectReason.BAD_PACKET);
        }
    }

}