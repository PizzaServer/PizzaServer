package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.event.type.player.*;
import io.github.willqi.pizzaserver.api.event.type.world.WorldSoundEvent;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.level.world.data.Dimension;
import io.github.willqi.pizzaserver.api.player.AdventureSettings;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.TextType;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.api.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;

/**
 * Handles any packets regarding the player entity itself.
 */
public class PlayerEntityPacketHandler extends BaseBedrockPacketHandler {

    private final ImplPlayer player;


    public PlayerEntityPacketHandler(ImplPlayer player) {
        this.player = player;
    }

    @Override
    public void onPacket(SetLocalPlayerAsInitializedPacket packet) {
        if (!this.player.isLocallyInitialized()) {
            this.player.onInitialized();

            PlayerLocallyInitializedEvent playerLocallyInitializedEvent = new PlayerLocallyInitializedEvent(this.player);
            this.player.getServer().getEventManager().call(playerLocallyInitializedEvent);
        }
    }

    @Override
    public void onPacket(MovePlayerPacket packet) {
        if (!this.player.isAlive()) {
            return;
        }

        if (this.player.hasSpawned()) {
            this.player.setPitch(packet.getPitch());
            this.player.setYaw(packet.getYaw());
            this.player.setHeadYaw(packet.getHeadYaw());
            this.player.moveTo(packet.getPosition().getX(), packet.getPosition().getY() - this.player.getEyeHeight(), packet.getPosition().getZ());
        }
    }

    @Override
    public void onPacket(PlayerAnimatePacket packet) {
        if (!this.player.isAlive()) {
            return;
        }

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
    public void onPacket(PlayerActionPacket packet) {
        switch (packet.getActionType()) {
            case RESPAWN:
                if (!this.player.isAlive()) {
                    this.player.respawn();
                }
                break;
            case START_SNEAK:
                if (!this.player.isSneaking() && this.player.isAlive()) {
                    PlayerStartSneakingEvent startSneakingEvent = new PlayerStartSneakingEvent(this.player);
                    this.player.getServer().getEventManager().call(startSneakingEvent);
                    this.player.setSneaking(true);
                }
                break;
            case STOP_SNEAK:
                if (this.player.isSneaking() && this.player.isAlive()) {
                    PlayerStartSneakingEvent stopSneakingEvent = new PlayerStartSneakingEvent(this.player);
                    this.player.getServer().getEventManager().call(stopSneakingEvent);
                    this.player.setSneaking(false);
                }
                break;
            case DIMENSION_CHANGE_ACK:
                if (this.player.getDimensionTransferScreen().isPresent()) {
                    Dimension dimensionTransferScreen = this.player.getDimensionTransferScreen().get();
                    if (!dimensionTransferScreen.equals(this.player.getWorld().getDimension())) {
                        this.player.setDimensionTransferScreen(this.player.getWorld().getDimension());
                    } else {
                        this.player.setDimensionTransferScreen(null);

                        PlayStatusPacket dimensionChangeComplete = new PlayStatusPacket();
                        dimensionChangeComplete.setStatus(PlayStatusPacket.PlayStatus.PLAYER_SPAWN);
                        this.player.sendPacket(dimensionChangeComplete);

                        this.player.getChunkManager().onDimensionTransferComplete();
                    }
                }
                break;
        }
    }

    @Override
    public void onPacket(RespawnPacket packet) {
        RespawnPacket respawnPacket = new RespawnPacket();
        respawnPacket.setPosition(this.player.getSpawn());
        respawnPacket.setState(RespawnPacket.State.SERVER_READY);
        respawnPacket.setEntityRuntimeId(this.player.getId());
        this.player.sendPacket(respawnPacket);
    }

    @Override
    public void onPacket(TextPacket packet) {
        this.player.getInventory().addItem(ItemRegistry.getItem("minecraft:dirt", 10));
        this.player.getWorld().addEntity(EntityRegistry.getEntity("minecraft:cow"), this.player.getLocation());
        if (packet.getType() == TextType.CHAT) {
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
                packet.getBlock().orElse(null));
        this.player.getServer().getEventManager().call(event);

        if (!event.isCancelled()) {
            event.getWorld().playSound(packet.getSound(),
                    packet.getVector3(),
                    packet.isGlobal(),
                    packet.isBaby(),
                    packet.getEntityType(),
                    packet.getBlock().orElse(null));

            for (Player viewer : this.player.getViewers()) {
                if (!viewer.getUUID().equals(this.player.getUUID())) {
                    viewer.sendPacket(packet);
                }
            }
        }
    }

    @Override
    public void onPacket(AdventureSettingsPacket packet) {
        if (packet.getUniqueEntityRuntimeId() == this.player.getId()) {
            AdventureSettings adventureSettings = this.player.getAdventureSettings();

            if (packet.getFlags().contains(AdventureSettingsPacket.Flag.FLYING)) {
                if (adventureSettings.canFly() && !adventureSettings.isFlying()) {
                    PlayerToggleFlightEvent toggleFlightEvent = new PlayerToggleFlightEvent(this.player, true);
                    this.player.getServer().getEventManager().call(toggleFlightEvent);

                    if (!toggleFlightEvent.isCancelled()) {
                        adventureSettings.setIsFlying(true);
                    }
                }
            } else if (adventureSettings.isFlying()) {
                PlayerToggleFlightEvent toggleFlightEvent = new PlayerToggleFlightEvent(this.player, false);
                this.player.getServer().getEventManager().call(toggleFlightEvent);

                if (!toggleFlightEvent.isCancelled()) {
                    adventureSettings.setIsFlying(false);
                }
            }
            this.player.setAdventureSettings(adventureSettings);
        }
    }

}
