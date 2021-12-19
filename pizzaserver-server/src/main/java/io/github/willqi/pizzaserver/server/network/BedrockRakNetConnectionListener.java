package io.github.willqi.pizzaserver.server.network;

import com.nukkitx.network.raknet.EncapsulatedPacket;
import com.nukkitx.network.raknet.RakNetSessionListener;
import com.nukkitx.network.raknet.RakNetState;
import com.nukkitx.network.util.DisconnectReason;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.utils.Zlib;
import io.netty.buffer.ByteBuf;

import java.util.zip.DataFormatException;

public class BedrockRakNetConnectionListener implements RakNetSessionListener {

    private final BedrockClientSession session;

    public BedrockRakNetConnectionListener(BedrockClientSession session) {
        this.session = session;
    }

    @Override
    public void onSessionChangeState(RakNetState rakNetState) {

    }

    @Override
    public void onDisconnect(DisconnectReason disconnectReason) {
        this.session.disconnect();  // Mark the session as disconnected
    }

    @Override
    public void onEncapsulated(EncapsulatedPacket encapsulatedPacket) {
        ByteBuf buffer = encapsulatedPacket.getBuffer();
        try {
            boolean isGamePacket = buffer.readUnsignedByte() == 0xfe;
            if (isGamePacket) {
                BasePacketBuffer inflatedBuffer;
                try {
                    if (this.session.getVersion() != null) {
                        inflatedBuffer = this.session.getVersion().createPacketBuffer(Zlib.decompressBuffer(buffer));
                    } else {
                        inflatedBuffer = new BasePacketBuffer(null, Zlib.decompressBuffer(buffer));
                    }
                } catch (DataFormatException exception) {
                    this.session.disconnect();
                    return;
                }

                // Multiple packets can be combined in 1 buffer
                while (inflatedBuffer.readableBytes() > 0) {
                    int packetBytes = inflatedBuffer.readUnsignedVarInt();
                    BasePacketBuffer packet = inflatedBuffer.readSlice(packetBytes);

                    // Packets start with a header that contains the client id (used for split screen) and the packet id
                    int packetId = packet.readUnsignedVarInt() & 0x3ff;
                    this.session.handlePacket(packetId, packet);
                }
                inflatedBuffer.release();
            }
        } catch (Exception exception) {
            ImplServer.getInstance().getLogger().error("An exception occurred while processing incoming packets.", exception);
            this.session.disconnect();
        }
    }

    @Override
    public void onDirect(ByteBuf byteBuf) {

    }

}