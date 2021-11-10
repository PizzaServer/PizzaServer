package io.github.willqi.pizzaserver.server.network;

import com.nukkitx.network.raknet.RakNetServerSession;
import com.nukkitx.network.util.DisconnectReason;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.MoveEntityAbsolutePacket;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.api.network.protocol.packets.LoginPacket;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class BedrockClientSession {

    private final BedrockNetworkServer server;
    private final RakNetServerSession serverSession;
    private volatile boolean disconnected;

    private BaseMinecraftVersion version;
    private volatile ImplPlayer player = null;
    private final Set<BaseBedrockPacketHandler> handlers = ConcurrentHashMap.newKeySet();

    private final Queue<BaseBedrockPacket> queuedIncomingPackets = new ConcurrentLinkedQueue<>();
    private final Queue<BaseBedrockPacket> queuedOutgoingPackets = new ConcurrentLinkedQueue<>();

    public BedrockClientSession(BedrockNetworkServer server, RakNetServerSession rakNetServerSession) {
        this.server = server;
        this.serverSession = rakNetServerSession;

        this.serverSession.getEventLoop().scheduleAtFixedRate(() -> this.processOutgoingPackets(false), 0, 50L, TimeUnit.MILLISECONDS);
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

    public void addPacketHandler(BaseBedrockPacketHandler packetHandler) {
        this.handlers.add(packetHandler);
    }

    public void removePacketHandler(BaseBedrockPacketHandler packetHandler) {
        this.handlers.remove(packetHandler);
    }

    public void disconnect() {
        if (!this.disconnected) {
            this.processOutgoingPackets(true);

            this.disconnected = true;
            this.serverSession.disconnect();
        }
    }

    public boolean isDisconnected() {
        return this.disconnected;
    }

    public long getPing() {
        return this.serverSession.getPing();
    }

    public void sendPacket(BaseBedrockPacket packet) {
        this.sendPacket(packet, false);
    }

    public void sendPacket(BaseBedrockPacket packet, boolean immediate) {
        if (immediate) {
            this.sendPacketsOverNetwork(Collections.singletonList(packet), true);
        } else {
            this.queuedOutgoingPackets.add(packet);
        }
    }

    @SuppressWarnings("unchecked")
    private void sendPacketsOverNetwork(List<BaseBedrockPacket> packets, boolean immediate) {
        // Packets begin with the RakNet game packet id
        ByteBuf rakNetBuffer = ByteBufAllocator.DEFAULT.ioBuffer();
        rakNetBuffer.writeByte(0xfe);

        ByteBuf uncompressedPacketsBuffer = ByteBufAllocator.DEFAULT.ioBuffer();
        for (BaseBedrockPacket packet : packets) {
            // The Minecraft packet buffer begins a header that contains the client id (used for split screen)
            // and the packet id.
            BasePacketBuffer minecraftPacketBuffer = this.version.createPacketBuffer();
            BasePacketBuffer packetWrapperBuffer = this.version.createPacketBuffer();
            try {
                int header = packet.getPacketId() & 0x3ff;
                minecraftPacketBuffer.writeUnsignedVarInt(header);

                // Serialize the BedrockPacket to the minecraftPacketBuffer
                BaseProtocolPacketHandler<BaseBedrockPacket> handler = (BaseProtocolPacketHandler<BaseBedrockPacket>) this.version.getPacketRegistry().getPacketHandler(packet.getPacketId());
                if (handler == null) {
                    this.server.getPizzaServer().getLogger().error("Missing packet handler when encoding packet id " + packet.getPacketId());
                    continue;
                }
                handler.encode(packet, minecraftPacketBuffer);

                // The minecraftPacketBuffer is prefixed with the length of the buffer
                packetWrapperBuffer.writeUnsignedVarInt(minecraftPacketBuffer.readableBytes());
                packetWrapperBuffer.writeBytes(minecraftPacketBuffer);

                uncompressedPacketsBuffer.writeBytes(packetWrapperBuffer);
            } finally {
                minecraftPacketBuffer.release();
                packetWrapperBuffer.release();
            }
        }

        // Compress the prefixed buffer and write it to the raknet buffer to send off!
        ByteBuf compressedBuffer = Zlib.compressBuffer(uncompressedPacketsBuffer, this.getServer().getPizzaServer().getConfig().getNetworkCompressionLevel());
        rakNetBuffer.writeBytes(compressedBuffer);

        uncompressedPacketsBuffer.release();
        compressedBuffer.release();

        if (immediate) {
            this.serverSession.sendImmediate(rakNetBuffer);
        } else {
            this.serverSession.send(rakNetBuffer);
        }
    }

    public void processIncomingPackets() {
        while (this.queuedIncomingPackets.peek() != null) {
            BaseBedrockPacket packet = this.queuedIncomingPackets.poll();
            for (BaseBedrockPacketHandler handler : this.handlers) {
                handler.onPacket(packet);

                // Now we call the specific packet handler
                try {
                    Method method = handler.getClass().getMethod("onPacket", packet.getClass());
                    method.invoke(handler, packet);
                } catch (NoSuchMethodException exception) {
                    this.server.getPizzaServer().getLogger().error("Missing onPacket callback for " + packet.getPacketId(), exception);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    this.server.getPizzaServer().getLogger().error("Failed to call packet handler for " + packet.getPacketId(), exception);
                }
            }
        }
    }

    private void processOutgoingPackets(boolean immediate) {
        BaseBedrockPacket packet;
        List<BaseBedrockPacket> packets = new ArrayList<>();
        while ((packet = this.queuedOutgoingPackets.poll()) != null) {
            packets.add(packet);
        }
        this.sendPacketsOverNetwork(packets, immediate);
    }

    /**
     * Handles incoming packets from the client to the server.
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
                minecraftPacket.setVersion(this.version);
                try {
                    loginPacket = (LoginPacket) this.version.getPacketRegistry().getPacketHandler(packetId).decode(minecraftPacket);
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
                minecraftPacket.setVersion(this.version);
                loginPacket = new LoginPacket();
                loginPacket.setProtocol(protocol);
            }
            this.queuedIncomingPackets.add(loginPacket);

        } else {
            // Client tried to send us a packet without sending the login packet first.
            this.serverSession.disconnect(DisconnectReason.BAD_PACKET);
            return;
        }

        if (minecraftPacket.readableBytes() > 0) {
            this.server.getPizzaServer().getLogger().warn("There were " + minecraftPacket.readableBytes() + " bytes that were left unread while parsing a packet with the id: " + packetId);
        }
    }

}
