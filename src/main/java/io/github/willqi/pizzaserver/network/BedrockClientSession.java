package io.github.willqi.pizzaserver.network;

import com.nukkitx.network.raknet.RakNetServerSession;
import com.nukkitx.network.util.DisconnectReason;
import io.github.willqi.pizzaserver.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.network.protocol.packets.LoginPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.PacketRegistry;
import io.netty.buffer.ByteBuf;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BedrockClientSession {

    private final RakNetServerSession serverSession;
    private PacketRegistry packetRegistry;

    private final List<BedrockPacket> queuedPackets = Collections.synchronizedList(new LinkedList<>());

    public BedrockClientSession(RakNetServerSession rakNetServerSession) {
        this.serverSession = rakNetServerSession;
    }

    public void setPacketRegistry(PacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    public void sendPacket(BedrockPacket packet) {

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
                BedrockPacket loginPacket = this.packetRegistry.getPacketHandler(packetId).decode(buffer);
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
