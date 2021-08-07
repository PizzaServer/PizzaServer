package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.player.Player;
<<<<<<< HEAD
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.server.event.type.world.WorldSoundEvent;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.server.event.type.player.PlayerChatEvent;
=======
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.world.Dimension;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.api.event.type.world.WorldSoundEvent;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerChatEvent;
import io.github.willqi.pizzaserver.server.utils.ImplLocation;
>>>>>>> feat/levels

public class FullGamePacketHandler extends BaseBedrockPacketHandler {

    private final ImplPlayer player;


    public FullGamePacketHandler(ImplPlayer player) {
        this.player = player;
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
        this.player.setPitch(packet.getPitch());
        this.player.setYaw(packet.getYaw());
        this.player.setHeadYaw(packet.getHeadYaw());
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

    @Override
    public void onPacket(WorldSoundEventPacket packet) {
        WorldSoundEvent event = new WorldSoundEvent(player.getLocation().getWorld(), packet.getSound(), packet.getVector3(), packet.isGlobal(), packet.isBaby(), packet.getEntityType(), packet.getBlockID());
        this.player.getServer().getEventManager().call(event);
        if(!event.isCancelled()) {
            event.getWorld().playSound(packet.getSound(), packet.getVector3(), packet.isGlobal(), packet.isBaby(), packet.getEntityType(), packet.getBlockID());
            for (Player player : event.getWorld().getPlayers()) {
                if(!player.getUUID().equals(this.player.getUUID())) player.sendPacket(packet);
            }
        }
    }
}
