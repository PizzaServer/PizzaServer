package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.server.event.type.player.PlayerChatEvent;

import java.util.concurrent.CompletionException;

public class FullGamePacketHandler extends BaseBedrockPacketHandler {

    private final ImplPlayer player;


    public FullGamePacketHandler(ImplPlayer player) {
        this.player = player;
        this.completeLogin();
    }

    /**
     * Send all remaining packets required before the player can see the world
     */
    private void completeLogin() {
        // Load the chunks around the player before we spawn them in
        Location location = this.player.getLocation();
        int playerChunkX = location.getChunkX();
        int playerChunkZ = location.getChunkZ();

        this.player.getServer().getScheduler().prepareTask(() -> {
            for (int chunkX = playerChunkX - this.player.getChunkRadius(); chunkX <= playerChunkX + this.player.getChunkRadius(); chunkX++) {
                for (int chunkZ = playerChunkZ - this.player.getChunkRadius(); chunkZ <= playerChunkZ + this.player.getChunkRadius(); chunkZ++) {
                    try {
                        this.player.getLocation().getWorld().getChunkManager().fetchChunk(chunkX, chunkZ).join();
                    } catch (CompletionException exception) {
                        this.player.getServer().getLogger().error(String.format("Failed to load chunk (%s, %s)", chunkX, chunkZ));
                    }
                }
            }

            // All chunks around the player have been sent. Spawn the player
            this.player.getServer()
                    .getScheduler()
                    .prepareTask(() -> this.player.getLocation().getWorld().addEntity(this.player, location))
                    .schedule();
        }).setAsynchronous(true).schedule();
    }
    @Override
    public void onPacket(RequestChunkRadiusPacket packet) {
        this.player.setChunkRadiusRequested(packet.getChunkRadiusRequested());

        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setRadius(this.player.getChunkRadius());
        this.player.sendPacket(chunkRadiusUpdatedPacket);
    }

    @Override
    public void onPacket(MovePlayerPacket packet) {
        Location newLocation = new Location(this.player.getLocation().getWorld(), packet.getPosition());
        this.player.setLocation(newLocation);
    }

    @Override
    public void onPacket(TextPacket packet) {
        if (packet.getType() == TextPacket.TextType.CHAT) {
            PlayerChatEvent event = new PlayerChatEvent(this.player, packet.getMessage(), this.player.getServer().getPlayers());

            this.player.getServer().getEventManager().call(event);
            if (!event.isCancelled()) {
                for (Player recipient : event.getRecipients()) {
                    recipient.sendPlayerMessage(this.player, event.getMessage());
                }
            }

        }
    }
}
