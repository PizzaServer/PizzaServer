package io.github.willqi.pizzaserver.server.network;

import com.nukkitx.network.VarInts;
import com.nukkitx.network.raknet.RakNetServerSession;
import com.nukkitx.network.util.DisconnectReason;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LoginPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.network.utils.Zlib;
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
    private volatile boolean disconnected;

    private MinecraftVersion version;
    private volatile BedrockPacketHandler handler;

    private final List<BedrockPacket> queuedIncomingPackets = Collections.synchronizedList(new LinkedList<>());
    private final List<BedrockPacket> queuedOutgoingPackets = Collections.synchronizedList(new LinkedList<>());

    public BedrockClientSession(BedrockServer server, RakNetServerSession rakNetServerSession) {
        this.server = server;
        this.serverSession = rakNetServerSession;
    }

    public BedrockServer getServer() {
        return this.server;
    }

    public MinecraftVersion getVersion() {
        return this.version;
    }

    public void setVersion(MinecraftVersion version) {
        this.version = version;
    }

    public void setPacketHandler(BedrockPacketHandler packetHandler) {
        this.handler = packetHandler;
    }

    public void disconnect() {
        if (!this.disconnected) {
            this.disconnected = true;
            this.serverSession.disconnect();
        }
    }

    public boolean isDisconnected() {
        return this.disconnected;
    }

    public void queueSendPacket(BedrockPacket packet) {
        this.queuedOutgoingPackets.add(packet);
    }

    public void sendPacket(BedrockPacket packet) {
        if (!this.disconnected) {
            ByteBuf rakNetBuffer = ByteBufAllocator.DEFAULT.buffer();
            rakNetBuffer.writeByte(0xfe); // Game packet

            ByteBuf packetBuffer = ByteBufAllocator.DEFAULT.buffer();
            // https://github.com/CloudburstMC/Protocol/blob/develop/bedrock/bedrock-common/src/main/java/com/nukkitx/protocol/bedrock/wrapper/BedrockWrapperSerializerV9_10.java#L34
            // Packets start with a header int rather than just a byte. (used fo split screen but we don't support that atm)
            int header = packet.getPacketId() & 0x3ff;
            VarInts.writeUnsignedInt(packetBuffer, header);

            ProtocolPacketHandler<BedrockPacket> hander = (ProtocolPacketHandler<BedrockPacket>)this.version.getPacketRegistry().getPacketHandler(packet.getPacketId());
            if (handler == null) {
                this.server.getPizzaServer().getLogger().error("Missing packet handler when encoding packet id " + packet.getPacketId());
                return;
            }
            hander.encode(packet, packetBuffer, this.version.getPacketRegistry().getPacketHelper());

            // Wrap the packet before sending it off
            ByteBuf packetWrapperBuffer = ByteBufAllocator.DEFAULT.buffer();
            VarInts.writeUnsignedInt(packetWrapperBuffer, packetBuffer.readableBytes());
            packetWrapperBuffer.writeBytes(packetBuffer);
            ByteBuf compressedBuffer = Zlib.compressBuffer(packetWrapperBuffer);
            rakNetBuffer.writeBytes(compressedBuffer);
            this.serverSession.send(rakNetBuffer);

            compressedBuffer.release();
            packetWrapperBuffer.release();
            packetBuffer.release();
        }
    }

    public void processPackets() {
        // Incoming
        if (this.handler != null) {
            List<BedrockPacket> packets;
            synchronized (this.queuedIncomingPackets) {
                packets = new ArrayList<>(this.queuedIncomingPackets);
                this.queuedIncomingPackets.clear();
            }
            for (BedrockPacket packet : packets) {
                this.handler.onPacket(packet);

                // Now we call the specific packet handler
                try {
                    Method method = this.handler.getClass().getMethod("onPacket", packet.getClass());
                    method.invoke(this.handler, packet);
                } catch (NoSuchMethodException exception) {
                    this.server.getPizzaServer().getLogger().error("Missing onPacket callback for " + packet.getPacketId(), exception);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    this.server.getPizzaServer().getLogger().error("Failed to call packet handler for " + packet.getPacketId(), exception);
                }
            }
        }

        // Outgoing
        List<BedrockPacket> packets;
        synchronized (this.queuedOutgoingPackets) {
            packets = new ArrayList<>(this.queuedIncomingPackets);
            this.queuedOutgoingPackets.clear();
        }
        for (BedrockPacket packet : packets) {
            this.sendPacket(packet);
        }
    }

    public void handlePacket(int packetId, ByteBuf buffer) {

        if (this.version != null) {
            ProtocolPacketHandler<? extends BedrockPacket> packetHandler = this.version.getPacketRegistry().getPacketHandler(packetId);
            if (packetHandler == null) {
                this.server.getPizzaServer().getLogger().error("Missing packet handler when decoding packet id " + packetId);
                return;
            }
            BedrockPacket bedrockPacket = packetHandler.decode(buffer, this.version.getPacketRegistry().getPacketHelper());
            this.queuedIncomingPackets.add(bedrockPacket);
        } else if (packetId == LoginPacket.ID) {

            // First packet, we need to find their protocol to find the correct packet handler.
            int index = buffer.readerIndex();
            int protocol = buffer.readInt();
            buffer.setIndex(index, buffer.writerIndex());

            // Parse login packet given protocol if available
            if (ServerProtocol.VERSIONS.containsKey(protocol)) {
                MinecraftVersion version = ServerProtocol.VERSIONS.get(protocol);
                this.version = version;
                BedrockPacket loginPacket;
                try {
                    loginPacket = this.version.getPacketRegistry().getPacketHandler(packetId).decode(buffer, this.version.getPacketRegistry().getPacketHelper());
                } catch (RuntimeException exception) {
                    this.server.getPizzaServer().getLogger().error("Error while decoding packet from client.", exception);
                    this.serverSession.disconnect(DisconnectReason.BAD_PACKET);
                    return;
                }
                this.queuedIncomingPackets.add(loginPacket);

            } else {
                // Unable to find packet handler.
                LoginPacket loginPacket = new LoginPacket();
                loginPacket.setProtocol(protocol);
                this.queuedIncomingPackets.add(loginPacket);
            }

        } else {
            // Client tried to send us a packet without sending the login packet first.
            this.serverSession.disconnect(DisconnectReason.BAD_PACKET);
        }
    }

}
