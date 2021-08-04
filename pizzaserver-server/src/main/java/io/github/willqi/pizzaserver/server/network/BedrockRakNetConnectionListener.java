package io.github.willqi.pizzaserver.server.network;

import com.nukkitx.network.VarInts;
import com.nukkitx.network.raknet.EncapsulatedPacket;
import com.nukkitx.network.raknet.RakNetSessionListener;
import com.nukkitx.network.raknet.RakNetState;
import com.nukkitx.network.util.DisconnectReason;
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
        boolean isGamePacket = buffer.readUnsignedByte() == 0xfe;
        if (isGamePacket) {
            ByteBuf inflatedBuffer;
            try {
                inflatedBuffer = Zlib.decompressBuffer(buffer);
            } catch (DataFormatException exception) {
                this.session.disconnect();
                return;
            }

            // Multiple packets can be combined in 1 buffer
            while (inflatedBuffer.readableBytes() > 0) {
                int packetBytes = VarInts.readUnsignedInt(inflatedBuffer);
                ByteBuf packet = inflatedBuffer.readSlice(packetBytes);

                // Packets start with a header that contains the client id (used for split screen) and the packet id
                int packetId = VarInts.readUnsignedInt(packet) & 0x3ff;
                try {
                    session.handlePacket(packetId, packet);
                } catch (Exception exception) {
                    throw new RuntimeException("Failed to parse packet (id: " + packetId + ")", exception);
                }
            }
        }
    }

    @Override
    public void onDirect(ByteBuf byteBuf) {

    }

}