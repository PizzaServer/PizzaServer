package io.github.willqi.pizzaserver.server.network;

import com.nukkitx.network.raknet.RakNetServerSession;
import com.nukkitx.network.util.DisconnectReason;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LoginPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.network.utils.Zlib;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BedrockClientSession extends Thread {

    private final BedrockNetworkServer server;
    private final RakNetServerSession serverSession;
    private volatile boolean disconnected;

    private BaseMinecraftVersion version;
    private volatile ImplPlayer player = null;
    private volatile BaseBedrockPacketHandler handler = null;

    private final Queue<BaseBedrockPacket> queuedIncomingPackets = new ConcurrentLinkedQueue<>();
    private final Queue<BaseBedrockPacket> queuedOutgoingPackets = new ConcurrentLinkedQueue<>();

    public BedrockClientSession(BedrockNetworkServer server, RakNetServerSession rakNetServerSession) {
        this.server = server;
        this.serverSession = rakNetServerSession;
    }

    public BedrockNetworkServer getServer() {
        return this.server;
    }

    public BaseMinecraftVersion getVersion() {
        return this.version;
    }

    public void setVersion(BaseMinecraftVersion version) {
        this.version = version;
    }

    public ImplPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(ImplPlayer player) {
        this.player = player;
    }

    public void setPacketHandler(BaseBedrockPacketHandler packetHandler) {
        this.handler = packetHandler;
    }

    public void disconnect() {
        if (!this.disconnected) {
            this.processOutgoingPackets();

            this.disconnected = true;
            this.serverSession.disconnect();
        }
    }

    public boolean isDisconnected() {
        return this.disconnected;
    }

    public void queueSendPacket(BaseBedrockPacket packet) {
        this.queuedOutgoingPackets.add(packet);
    }


    public void sendPacket(BaseBedrockPacket packet) {
        if (!this.disconnected) {
            // Packets begin with the RakNet game packet id
            ByteBuf rakNetBuffer = ByteBufAllocator.DEFAULT.buffer();
            rakNetBuffer.writeByte(0xfe);

            // The Minecraft packet buffer begins a header that contains the client id (used for split screen)
            // and the packet id.
            BasePacketBuffer minecraftPacketBuffer = this.version.createPacketBuffer();
            int header = packet.getPacketId() & 0x3ff;
            minecraftPacketBuffer.writeUnsignedVarInt(header);

            // Serialize the BedrockPacket to the minecraftPacketBuffer
            BaseProtocolPacketHandler<BaseBedrockPacket> handler = (BaseProtocolPacketHandler<BaseBedrockPacket>)this.version.getPacketRegistry().getPacketHandler(packet.getPacketId());
            if (this.handler == null) {
                this.server.getPizzaServer().getLogger().error("Missing packet handler when encoding packet id " + packet.getPacketId());
                return;
            }
            handler.encode(packet, minecraftPacketBuffer);

            // The minecraftPacketBuffer is prefixed with the length of the buffer
            BasePacketBuffer packetWrapperBuffer = this.version.createPacketBuffer();
            packetWrapperBuffer.writeUnsignedVarInt(minecraftPacketBuffer.readableBytes());
            packetWrapperBuffer.writeBytes(minecraftPacketBuffer);

            // Compress the prefixed buffer and write it to the raknet buffer to send off!
            ByteBuf compressedBuffer = Zlib.compressBuffer(packetWrapperBuffer);
            rakNetBuffer.writeBytes(compressedBuffer);
            this.serverSession.send(rakNetBuffer);

            compressedBuffer.release();
            packetWrapperBuffer.release();
            minecraftPacketBuffer.release();
        }
    }

    public void processPackets() {
        this.processIncomingPackets();
        this.processOutgoingPackets();
    }

    private void processIncomingPackets() {
        if (this.handler != null) {
            while (this.queuedIncomingPackets.peek() != null) {
                BaseBedrockPacket packet = this.queuedIncomingPackets.poll();
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
    }

    private void processOutgoingPackets() {
        while (this.queuedOutgoingPackets.peek() != null) {
            BaseBedrockPacket packet = this.queuedOutgoingPackets.poll();
            this.sendPacket(packet);
        }
    }

    /**
     * Handles incoming packets from the client to the server
     * @param packetId id of the packet sent
     * @param minecraftPacket contents of the packet we need to deserialize
     */
    public void handlePacket(int packetId, BasePacketBuffer minecraftPacket) {
        if (this.version != null) {
            BaseProtocolPacketHandler<? extends BaseBedrockPacket> packetHandler = this.version.getPacketRegistry().getPacketHandler(packetId);
            if (packetHandler == null) {
                this.server.getPizzaServer().getLogger().error("Missing packet handler when decoding packet id " + packetId);
                return;
            }
            BaseBedrockPacket bedrockPacket = packetHandler.decode(minecraftPacket);
            this.queuedIncomingPackets.add(bedrockPacket);
        } else if (packetId == LoginPacket.ID) {
            // We do not have a version assigned yet, so we need to use the LoginPacket to get the protocol version we are to use.
            int index = minecraftPacket.readerIndex();
            int protocol = minecraftPacket.readInt();
            minecraftPacket.setIndex(index, minecraftPacket.writerIndex()); // Reset so that the LoginPacket serializer can serialize it correctly

            LoginPacket loginPacket;
            if (ServerProtocol.VERSIONS.containsKey(protocol)) {    // Supported version
                this.version = ServerProtocol.VERSIONS.get(protocol);
                try {
                    loginPacket = (LoginPacket)this.version.getPacketRegistry().getPacketHandler(packetId).decode(minecraftPacket);
                } catch (RuntimeException exception) {
                    this.server.getPizzaServer().getLogger().error("Error while decoding packet from client.", exception);
                    this.serverSession.disconnect(DisconnectReason.BAD_PACKET);
                    return;
                }
            } else {
                // Unable to find packet handler, so pretend that we're on the latest version so that it can serialize
                // a outdated server/client play status.
                // This should be changed if Microsoft changes the PlayStatus packet format.
                this.version = ServerProtocol.VERSIONS.get(ServerProtocol.LATEST_PROTOCOL_VERISON);
                loginPacket = new LoginPacket();
                loginPacket.setProtocol(protocol);
            }
            this.queuedIncomingPackets.add(loginPacket);

        } else {
            // Client tried to send us a packet without sending the login packet first.
            this.serverSession.disconnect(DisconnectReason.BAD_PACKET);
        }

        if (minecraftPacket.readableBytes() > 0) {
            this.server.getPizzaServer().getLogger().warn("There were bytes that were left unread while parsing a packet with the id: " + packetId);
            minecraftPacket.release();
        }
    }

}
