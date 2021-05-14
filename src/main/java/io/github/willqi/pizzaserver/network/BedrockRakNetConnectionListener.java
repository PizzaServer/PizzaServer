package io.github.willqi.pizzaserver.network;

import com.nukkitx.network.VarInts;
import com.nukkitx.network.raknet.EncapsulatedPacket;
import com.nukkitx.network.raknet.RakNetSessionListener;
import com.nukkitx.network.raknet.RakNetState;
import com.nukkitx.network.util.DisconnectReason;
import io.github.willqi.pizzaserver.network.utils.Zlib;
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

    }

    @Override
    public void onEncapsulated(EncapsulatedPacket encapsulatedPacket) {
        ByteBuf buffer = encapsulatedPacket.getBuffer();
        boolean isGamePacket = buffer.readUnsignedByte() == 0xfe;
        if (isGamePacket) {
            ByteBuf inflatedBuffer;
            try {
                inflatedBuffer = Zlib.inflateBuffer(buffer);
            } catch (DataFormatException exception) {
                throw new RuntimeException(exception);
            }
            int packetBytes = VarInts.readUnsignedInt(inflatedBuffer);
            ByteBuf packet = inflatedBuffer.readSlice(packetBytes);
            int packetId = packet.readUnsignedByte();
            try {
                session.handlePacket(packetId, packet);
            } catch (Exception exception) {
                throw new RuntimeException("Failed to parse packet (id: " + packetId + ")", exception);
            }

        }
    }

    @Override
    public void onDirect(ByteBuf byteBuf) {

    }

}