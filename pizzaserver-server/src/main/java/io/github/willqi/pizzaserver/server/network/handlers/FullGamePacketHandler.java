package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.event.type.player.PlayerAnimationEvent;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerSkinUpdateEvent;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.event.type.world.WorldSoundEvent;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerChatEvent;

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
        if (this.player.hasSpawned()) {
            this.player.setPitch(packet.getPitch());
            this.player.setYaw(packet.getYaw());
            this.player.setHeadYaw(packet.getHeadYaw());
            this.player.moveTo(packet.getPosition().getX(), packet.getPosition().getY() - this.player.getEyeHeight(), packet.getPosition().getZ());
        }
    }

    @Override
    public void onPacket(PlayerAnimatePacket packet) {
        PlayerAnimationEvent event = new PlayerAnimationEvent(this.player, packet.getAction());
        this.player.getServer().getEventManager().call(event);

        if (!event.isCancelled()) {
            PlayerAnimatePacket animatePacket = new PlayerAnimatePacket();
            animatePacket.setEntityRuntimeID(this.player.getId());
            animatePacket.setAction(packet.getAction());
            animatePacket.setRowingTime(packet.getRowingTime());
            for (Player viewer : this.player.getViewers()) {
                viewer.sendPacket(animatePacket);
            }
        }
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
    public void onPacket(PlayerSkinPacket packet) {
        PlayerSkinUpdateEvent event = new PlayerSkinUpdateEvent(this.player, packet.getSkin());
        this.player.getServer().getEventManager().call(event);

        if (!event.isCancelled()) {
            this.player.setSkin(event.getNewSkin());
        }
    }

    @Override
    public void onPacket(WorldSoundEventPacket packet) {
        WorldSoundEvent event = new WorldSoundEvent(this.player.getLocation().getWorld(),
                packet.getSound(),
                packet.getVector3(),
                packet.isGlobal(),
                packet.isBaby(),
                packet.getEntityType(),
                packet.getBlockID());
        this.player.getServer().getEventManager().call(event);

        if (!event.isCancelled()) {
            event.getWorld().playSound(packet.getSound(),
                    packet.getVector3(),
                    packet.isGlobal(),
                    packet.isBaby(),
                    packet.getEntityType(),
                    packet.getBlockID());

            for (Player viewer : this.player.getViewers()) {
                if (!viewer.getUUID().equals(this.player.getUUID())) {
                    viewer.sendPacket(packet);
                }
            }
        }
    }
}
