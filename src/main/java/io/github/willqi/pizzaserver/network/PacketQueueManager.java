package io.github.willqi.pizzaserver.network;

import com.nukkitx.protocol.bedrock.BedrockPacket;

import java.util.*;

/**
 * Handles storing packets sent by the player that should be executed on next tick
 */
public class PacketQueueManager {

    private final List<BedrockPacket> queue;

    public PacketQueueManager() {
        this.queue = Collections.synchronizedList(new ArrayList<>());
    }

    public List<BedrockPacket> clear() {
        List<BedrockPacket> packets;
        synchronized (this.queue) {
            packets = new ArrayList<>(this.queue);
        }
        this.queue.clear();
        return Collections.unmodifiableList(packets);
    }

    public void queue(BedrockPacket packet) {
        this.queue.add(packet);
    }

}
