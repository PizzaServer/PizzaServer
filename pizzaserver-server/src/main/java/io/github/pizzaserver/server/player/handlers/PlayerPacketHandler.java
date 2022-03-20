package io.github.pizzaserver.server.player.handlers;

import com.nukkitx.protocol.bedrock.data.AdventureSetting;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.data.LeaveType;
import io.github.pizzaserver.api.block.data.LitType;
import io.github.pizzaserver.api.block.impl.*;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.EntityHuman;
import io.github.pizzaserver.api.entity.definition.impl.EntityHumanDefinition;
import io.github.pizzaserver.api.event.type.inventory.InventoryOpenEvent;
import io.github.pizzaserver.api.event.type.player.*;
import io.github.pizzaserver.api.item.impl.*;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.player.AdventureSettings;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Skin;
import io.github.pizzaserver.server.player.ImplPlayer;

public class PlayerPacketHandler implements BedrockPacketHandler {

    private final ImplPlayer player;


    public PlayerPacketHandler(ImplPlayer player) {
        this.player = player;
    }

    @Override
    public boolean handle(SetLocalPlayerAsInitializedPacket packet) {
        if (!this.player.isLocallyInitialized()) {
            this.player.onLocallyInitialized();

            PlayerLocallyInitializedEvent playerLocallyInitializedEvent = new PlayerLocallyInitializedEvent(this.player);
            this.player.getServer().getEventManager().call(playerLocallyInitializedEvent);
        }
        return true;
    }

    @Override
    public boolean handle(AnimatePacket packet) {
        if (this.player.isAlive()) {
            PlayerAnimationEvent event = new PlayerAnimationEvent(this.player, packet.getAction());
            this.player.getServer().getEventManager().call(event);

            if (!event.isCancelled()) {
                AnimatePacket animatePacket = new AnimatePacket();
                animatePacket.setRuntimeEntityId(this.player.getId());
                animatePacket.setAction(packet.getAction());
                animatePacket.setRowingTime(packet.getRowingTime());
                for (Player viewer : this.player.getViewers()) {
                    viewer.sendPacket(animatePacket);
                }
            }
        }
        return true;
    }

    @Override
    public boolean handle(PlayerActionPacket packet) {
        switch (packet.getAction()) {
            case RESPAWN:
                if (!this.player.isAlive()) {
                    this.player.respawn();
                }
                break;
            case DIMENSION_CHANGE_SUCCESS:
                if (this.player.getDimensionTransferScreen().isPresent()) {
                    Dimension dimensionTransferScreen = this.player.getDimensionTransferScreen().get();
                    if (!dimensionTransferScreen.equals(this.player.getWorld().getDimension())) {
                        this.player.setDimensionTransferScreen(this.player.getWorld().getDimension());
                    } else {
                        this.player.setDimensionTransferScreen(null);

                        PlayStatusPacket dimensionChangeComplete = new PlayStatusPacket();
                        dimensionChangeComplete.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
                        this.player.sendPacket(dimensionChangeComplete);

                        this.player.getChunkManager().onDimensionTransferComplete();
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public boolean handle(RespawnPacket packet) {
        RespawnPacket respawnPacket = new RespawnPacket();
        respawnPacket.setPosition(this.player.getSpawn().toVector3f());
        respawnPacket.setState(RespawnPacket.State.SERVER_READY);
        respawnPacket.setRuntimeEntityId(this.player.getId());
        this.player.sendPacket(respawnPacket);
        return true;
    }

    @Override
    public boolean handle(TextPacket packet) {
        this.player.getInventory().addItem(new ItemBlock(new BlockPumpkin(), 64));
        this.player.getInventory().addItem(new ItemBlock(new BlockCarvedPumpkin(), 64));
        this.player.getInventory().addItem(new ItemShears());
        this.player.getInventory().addItem(new ItemBlock(new BlockLeaves(LeaveType.OAK), 64));
        this.player.getInventory().addItem(new ItemBlock(new BlockLeaves(LeaveType.JUNGLE), 64));
        this.player.getInventory().addItem(new ItemBlock(new BlockChest(), 64));
        this.player.getInventory().addItem(new ItemBlock(new BlockCarvedPumpkin(LitType.LIT), 64));
        EntityHuman entityHuman = (EntityHuman) EntityRegistry.getInstance().getEntity(EntityHumanDefinition.ID);
        this.player.getWorld().addEntity(entityHuman, this.player.getLocation().toVector3f());
        if (packet.getType() == TextPacket.Type.CHAT) {
            String message = packet.getMessage().strip();
            if (message.length() > 512) {
                message = message.substring(0, 512);
            }
            PlayerChatEvent event = new PlayerChatEvent(this.player, message, this.player.getServer().getPlayers());

            this.player.getServer().getEventManager().call(event);
            if (!event.isCancelled()) {
                for (Player recipient : event.getRecipients()) {
                    recipient.sendPlayerMessage(this.player, event.getMessage());
                }
            }
        }
        return true;
    }

    @Override
    public boolean handle(PlayerSkinPacket packet) {
        PlayerSkinUpdateEvent event = new PlayerSkinUpdateEvent(this.player, Skin.deserialize(packet.getSkin()));
        this.player.getServer().getEventManager().call(event);

        if (!event.isCancelled()) {
            this.player.setSkin(event.getNewSkin());
        }
        return true;
    }

    @Override
    public boolean handle(AdventureSettingsPacket packet) {
        if (packet.getUniqueEntityId() == this.player.getId()) {
            AdventureSettings adventureSettings = this.player.getAdventureSettings();

            if (packet.getSettings().contains(AdventureSetting.FLYING)) {
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
        }
        return true;
    }

    @Override
    public boolean handle(ModalFormResponsePacket packet) {
        this.player.getPopupManager().onFormResponse(packet.getFormId(), packet.getFormData());
        return true;
    }

    @Override
    public boolean handle(RequestChunkRadiusPacket packet) {
        int newChunkRadius = Math.min(packet.getRadius(), this.player.getServer().getConfig().getChunkRadius());
        this.player.setChunkRadius(newChunkRadius);

        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setRadius(this.player.getChunkRadius());
        this.player.sendPacket(chunkRadiusUpdatedPacket);
        return true;
    }

    @Override
    public boolean handle(InteractPacket packet) {
        if (this.player.isAlive()) {
            switch (packet.getAction()) {
                case OPEN_INVENTORY:
                    if (this.player.getOpenInventory().isEmpty() && this.player.getInventory().canBeOpenedBy(this.player)) {
                        InventoryOpenEvent inventoryOpenEvent = new InventoryOpenEvent(this.player, this.player.getInventory());
                        Server.getInstance().getEventManager().call(inventoryOpenEvent);
                        if (!inventoryOpenEvent.isCancelled()) {
                            this.player.openInventory(this.player.getInventory());
                        }
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean handle(ContainerClosePacket packet) {
        this.player.closeOpenInventory();
        return true;
    }

}
