package io.github.willqi.pizzaserver.network;

import com.nukkitx.network.raknet.*;
import io.github.willqi.pizzaserver.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.utils.Gamemode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class BedrockServer {

    private RakNetServer rakNetServer;

    private volatile BedrockPong pong;

    public void boot(String ip, int port) throws ExecutionException, InterruptedException {
        this.rakNetServer = new RakNetServer(new InetSocketAddress(ip, port));
        this.rakNetServer.setListener(new BedrockServerEventListener());
        this.rakNetServer.bind().get();

        this.setPong(
                new BedrockPong.Builder()
                    .setEdition(BedrockPong.Edition.MCPE)
                    .setGamemode(Gamemode.CREATIVE)
                    .setGameVersion(ServerProtocol.GAME_VERSION)
                    .setMotd("MOTD 1")
                    .setPlayerCount(10)
                    .setPort(19132)
                    .setProtocol(431)
                    .setSubMotd("MOTD 2")
                    .setMaximumPlayerCount(20)
                    .build()
        );
    }

    public void setPong(BedrockPong pong) {
        this.pong = pong;
    }

    public BedrockPong getPong() {
        return this.pong;
    }

    private class BedrockServerEventListener implements RakNetServerListener {

        @Override
        public boolean onConnectionRequest(InetSocketAddress inetSocketAddress) {
            return true;
        }

        @Override
        public byte[] onQuery(InetSocketAddress inetSocketAddress) {
            BedrockPong pong = BedrockServer.this.getPong();
            return (pong.getEdition() + ";" +
                    pong.getMotd() + ";" +
                    pong.getProtocol() + ";" +
                    pong.getGameVersion() + ";" +
                    pong.getOnlinePlayerCount() + ";" +
                    pong.getMaxPlayerCount() + ";" +
                    BedrockServer.this.rakNetServer.getGuid() + ";" +
                    pong.getSubMotd() + ";" +
                    pong.getGamemode().getName() + ";" +
                    pong.getGamemode().getId() + ";" +
                    pong.getPort() + ";" +
                    pong.getPort() + ";").getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public void onSessionCreation(RakNetServerSession rakNetServerSession) {
            BedrockClientSession session = new BedrockClientSession(rakNetServerSession);
            rakNetServerSession.setListener(new BedrockRakNetConnectionListener(session));
        }

        // The Bedrock client does not seem to care about this as far as I can tell
        @Override
        public void onUnhandledDatagram(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) {}

    }

}
