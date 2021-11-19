package io.github.pizzaserver.server.network.handlers;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.AdventureSetting;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ItemStackRequest;
import com.nukkitx.protocol.bedrock.data.inventory.TransactionType;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.PlaceStackRequestActionData;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionData;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.SwapStackRequestActionData;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.TakeStackRequestActionData;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.pizzaserver.api.event.type.player.*;
import io.github.pizzaserver.server.entity.ImplEntity;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.entity.data.DamageCause;
import io.github.pizzaserver.api.entity.definition.impl.CowEntityDefinition;
import io.github.pizzaserver.api.event.type.block.BlockBreakEvent;
import io.github.pizzaserver.api.event.type.block.BlockStartBreakEvent;
import io.github.pizzaserver.api.event.type.block.BlockStopBreakEvent;
import io.github.pizzaserver.api.event.type.entity.EntityDamageByEntityEvent;
import io.github.pizzaserver.api.event.type.inventory.InventoryDropItemEvent;
import io.github.pizzaserver.api.event.type.world.WorldSoundEvent;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.player.AdventureSettings;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;
import io.github.pizzaserver.api.player.data.Skin;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.network.handlers.inventory.InventoryActionPlaceHandler;
import io.github.pizzaserver.server.network.handlers.inventory.InventoryActionSwapHandler;
import io.github.pizzaserver.server.network.handlers.inventory.InventoryActionTakeHandler;
import io.github.pizzaserver.server.player.ImplPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class GamePacketHandler implements BedrockPacketHandler {

    private final ImplPlayer player;


    public GamePacketHandler(ImplPlayer player) {
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
    public boolean handle(MovePlayerPacket packet) {
        if (this.player.isAlive() && this.player.hasSpawned()) {
            this.player.setPitch(packet.getRotation().getX());
            this.player.setYaw(packet.getRotation().getY());
            this.player.setHeadYaw(packet.getRotation().getZ());
            this.player.moveTo(packet.getPosition().getX(), packet.getPosition().getY() - this.player.getEyeHeight(), packet.getPosition().getZ());
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
            case START_SWIMMING:
                Block blockSwimmingIn = this.player.getWorld().getBlock(this.player.getLocation().toVector3i());
                if (this.player.isAlive() && !this.player.isSwimming() && blockSwimmingIn.getBlockType().isLiquid()) {
                    PlayerToggleSwimEvent swimEvent = new PlayerToggleSwimEvent(this.player, true);
                    this.player.getServer().getEventManager().call(swimEvent);

                    if (!swimEvent.isCancelled()) {
                        this.player.setSwimming(true);
                    }
                }
                break;
            case STOP_SWIMMING:
                if (this.player.isAlive() && this.player.isSwimming()) {
                    PlayerToggleSwimEvent swimEvent = new PlayerToggleSwimEvent(this.player, false);
                    this.player.getServer().getEventManager().call(swimEvent);

                    if (!swimEvent.isCancelled()) {
                        this.player.setSwimming(false);
                    }
                }
                break;
            case START_BREAK:
                if (this.player.isAlive() && this.player.canReach(packet.getBlockPosition(), this.player.inCreativeMode() ? 13 : 7)) {
                    BlockLocation blockBreakingLocation = new BlockLocation(this.player.getWorld(), packet.getBlockPosition());

                    if (blockBreakingLocation.getBlock().isSolid() && this.player.getAdventureSettings().canMine()) {
                        BlockStartBreakEvent blockStartBreakEvent = new BlockStartBreakEvent(this.player, blockBreakingLocation.getBlock());
                        this.player.getServer().getEventManager().call(blockStartBreakEvent);
                        if (!blockStartBreakEvent.isCancelled()) {
                            this.player.getBlockBreakingManager().startBreaking(blockBreakingLocation);
                        }
                    } else {
                        // Player was not able to break that block
                        this.player.getWorld().sendBlock(this.player, packet.getBlockPosition());
                    }
                    break;
                }
                break;
            case CONTINUE_BREAK:
                if (this.player.isAlive() && this.player.getBlockBreakingManager().isBreakingBlock()) {
                    this.player.getBlockBreakingManager().sendUpdatedBreakProgress();
                }
                break;
            case ABORT_BREAK:
                if (this.player.getBlockBreakingManager().getBlock().isPresent()) {
                    BlockStopBreakEvent blockStopBreakEvent = new BlockStopBreakEvent(this.player, this.player.getBlockBreakingManager().getBlock().get());
                    this.player.getServer().getEventManager().call(blockStopBreakEvent);

                    this.player.getBlockBreakingManager().stopBreaking();
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
        this.player.getInventory().addItem(ItemRegistry.getItem(BlockTypeID.DIRT, 10));
        this.player.getWorld().addEntity(EntityRegistry.getEntity(CowEntityDefinition.ID), this.player.getLocation().toVector3f());
        if (packet.getType() == TextPacket.Type.CHAT) {
            PlayerChatEvent event = new PlayerChatEvent(this.player, packet.getMessage(), this.player.getServer().getPlayers());

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
    public boolean handle(LevelSoundEventPacket packet) {
        Block block = packet.getExtraData() != -1 ? this.player.getVersion().getBlockFromRuntimeId(packet.getExtraData()) : null;
        WorldSoundEvent event = new WorldSoundEvent(this.player.getLocation(),
                packet.getSound(),
                packet.isRelativeVolumeDisabled(),
                packet.isBabySound(),
                packet.getIdentifier(),
                block);
        this.player.getServer().getEventManager().call(event);

        if (!event.isCancelled()) {
            event.getWorld().playSound(event.getSound(),
                    event.getLocation().toVector3f(),
                    event.isRelativeVolumeDisabled(),
                    event.isBaby(),
                    event.getIdentifier(),
                    event.getBlock().orElse(null));

            for (Player viewer : this.player.getViewers()) {
                if (!viewer.equals(this.player)) {
                    viewer.sendPacket(packet);
                }
            }
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
            this.player.setAdventureSettings(adventureSettings);
        }
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

    // Handles breaking the block
    @Override
    public boolean handle(InventoryTransactionPacket packet) {
        if (packet.getTransactionType() == TransactionType.ITEM_USE && this.player.isAlive()) {
            boolean isValidBreakBlockRequest = this.player.canReach(packet.getBlockPosition(), this.player.inCreativeMode() ? 13 : 7)
                    && packet.getActionType() == InventoryTransactionUseItemData.Action.BREAK_BLOCK;

            if (isValidBreakBlockRequest) {
                Optional<Block> blockMining = this.player.getBlockBreakingManager().getBlock();
                boolean isCorrectBlockCoordinates = blockMining.isPresent() && packet.getBlockPosition().equals(blockMining.get().getLocation().toVector3i());
                boolean canBreakBlock = isCorrectBlockCoordinates && this.player.getBlockBreakingManager().canBreakBlock();

                if (!isCorrectBlockCoordinates) {
                    this.player.getServer().getLogger().debug(String.format("%s tried to destroy a block while not breaking the block.", this.player.getUsername()));
                }

                if (canBreakBlock) {
                    BlockBreakEvent blockBreakEvent = new BlockBreakEvent(this.player, this.player.getWorld().getBlock(packet.getBlockPosition()));
                    this.player.getServer().getEventManager().call(blockBreakEvent);

                    if (!blockBreakEvent.isCancelled()) {
                        if (blockBreakEvent.isBlockDropsEnabled()) {
                            for (ItemStack itemStack : blockMining.get().getDrops()) {
                                Vector3f velocity = Vector3f.from(0.1f * (ThreadLocalRandom.current().nextFloat() * 2 - 1), 0.3f, 0.1f * (ThreadLocalRandom.current().nextFloat() * 2 - 1));
                                Vector3f position = blockMining.get().getLocation().toVector3f().add(0.5f, 0.5f, 0.5f);
                                this.player.getWorld().addItemEntity(itemStack, position, velocity);
                            }
                        }
                        this.player.getBlockBreakingManager().breakBlock();
                        return true;
                    }
                } else {
                    this.player.getServer().getLogger().debug(String.format("%s tried to destroy a block but was not allowed.", this.player.getUsername()));
                }

                this.player.getWorld().sendBlock(this.player, packet.getBlockPosition());
            }
        }

        return true;
    }

    @Override
    public boolean handle(InteractPacket packet) {
        if (this.player.isAlive()) {
            switch (packet.getAction()) {
                case OPEN_INVENTORY:
                    if (!this.player.getOpenInventory().isPresent()) {
                        this.player.openInventory(this.player.getInventory());
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

    @Override
    public boolean handle(ItemStackRequestPacket packet) {
        ItemStackResponsePacket itemStackResponsePacket = new ItemStackResponsePacket();
        List<ItemStackResponsePacket.Response> responses = new ArrayList<>();

        for (ItemStackRequest request : packet.getRequests()) {
            int requestId = request.getRequestId();
            ItemStackResponsePacket.Response response = new ItemStackResponsePacket.Response(requestId);

            boolean continueActions = true;
            for (StackRequestActionData action : request.getActions()) {
                if (!continueActions) {  // e.g. if the inventory action is incorrect
                    break;
                }

                switch (action.getType()) {
                    case TAKE:
                        continueActions = InventoryActionTakeHandler.INSTANCE.tryAction(response, this.player, (TakeStackRequestActionData) action);
                        break;
                    case PLACE:
                        continueActions = InventoryActionPlaceHandler.INSTANCE.tryAction(response, this.player, (PlaceStackRequestActionData) action);
                        break;
                    case SWAP:
                        continueActions = InventoryActionSwapHandler.INSTANCE.tryAction(response, this.player, (SwapStackRequestActionData) action);
                        break;
                    case DROP:
                        break;
                    case DESTROY:
                        break;
                    case CONSUME:
                        break;
                    case LAB_TABLE_COMBINE:
                        break;
                    case BEACON_PAYMENT:
                        break;
                    case MINE_BLOCK:
                        break;
                    case CRAFT_RECIPE:
                        break;
                    case CRAFT_RECIPE_AUTO:
                        break;
                    case CRAFT_CREATIVE:
                        break;
                    case CRAFT_RECIPE_OPTIONAL:
                        break;
                    case CRAFT_REPAIR_AND_DISENCHANT:
                        break;
                    case CRAFT_LOOM:
                        break;
                    case CRAFT_NON_IMPLEMENTED_DEPRECATED:
                        break;
                    case CRAFT_RESULTS_DEPRECATED:
                        break;
                }
                responses.add(response);
            }
        }

        itemStackResponsePacket.getEntries().addAll(responses);
        this.player.sendPacket(itemStackResponsePacket);
        return true;
    }

    @Override
    public boolean handle(MobEquipmentPacket packet) {
        boolean isHotbarSlot = packet.getHotbarSlot() >= 0 && packet.getHotbarSlot() < 9;
        if (isHotbarSlot && packet.getContainerId() == ContainerId.INVENTORY) {
            // If their item does not match up with the server side item resend the server's slot
            if (!ItemStack.deserialize(packet.getItem(), this.player.getVersion()).equals(this.player.getInventory().getSlot(packet.getHotbarSlot()))) {
                this.player.getInventory().sendSlot(this.player, packet.getHotbarSlot());
            }

            // Handle hotbar slot change
            if (packet.getHotbarSlot() != this.player.getInventory().getSelectedSlot()) {
                PlayerHotbarSelectEvent playerHotbarSelectEvent = new PlayerHotbarSelectEvent(this.player, packet.getHotbarSlot());
                this.player.getServer().getEventManager().call(playerHotbarSelectEvent);

                if (!playerHotbarSelectEvent.isCancelled()) {
                    // Server approves of hotbar slot change.

                    // Check if we need to recalculate our break time for the block we may be breaking.
                    boolean differentItemTypes = !this.player.getInventory().getHeldItem().getItemType().equals(this.player.getInventory().getSlot(packet.getSlot()).getItemType());
                    boolean needToRecalculateBlockBreakTime = this.player.getBlockBreakingManager().isBreakingBlock()
                            && differentItemTypes;

                    this.player.getInventory().setSelectedSlot(packet.getHotbarSlot(), true);

                    if (needToRecalculateBlockBreakTime) {
                        this.player.getBlockBreakingManager().onChangedHeldItemWhileBreaking();
                    }
                } else {
                    // Reset their selected slot back to the old slot
                    this.player.getInventory().setSelectedSlot(this.player.getInventory().getSelectedSlot());
                }
            }
        }
        return true;
    }

    @Override
    public boolean handle(InventoryTransactionPacket packet) {
        ItemStack heldItemStack = this.player.getInventory().getHeldItem();

        if (!this.player.isAlive()) {
            return;
        }

        switch (packet.getType()) {
            case NORMAL:
                // Dropping items is still handled by this packet despite server authoritative inventories
                for (int actionIndex = 0; actionIndex < packet.getActions().size() - 1; actionIndex++) {
                    // The slot of the dropped item is sent in the action right after the world interaction
                    InventoryTransactionAction action = packet.getActions().get(actionIndex);
                    InventoryTransactionAction nextAction = packet.getActions().get(actionIndex + 1);

                    boolean isDropAction = action.getSource().getType() == InventoryTransactionSourceType.WORLD
                            && ((InventoryTransactionWorldSource) action.getSource()).getFlag() == InventoryTransactionWorldSource.Flag.DROP_ITEM
                            && nextAction.getSlot() >= 0 && nextAction.getSlot() < 9;

                    if (isDropAction) {
                        ItemStack itemStack = this.player.getInventory().getSlot(nextAction.getSlot());
                        if (!itemStack.isEmpty()) {
                            // Update stack with amount dropped
                            int amountDropped = Math.max(0, Math.min(itemStack.getCount(), action.getNewItemStack().getCount()));

                            ItemStack droppedStack = itemStack.clone();
                            droppedStack.setCount(amountDropped);

                            InventoryDropItemEvent dropItemEvent = new InventoryDropItemEvent(this.player.getInventory(), this.player, droppedStack);
                            this.player.getServer().getEventManager().call(dropItemEvent);
                            if (!dropItemEvent.isCancelled()) {
                                // update server inventory to reflect dropped count
                                itemStack.setCount(itemStack.getCount() - amountDropped);
                                this.player.getInventory().setSlot(this.player, nextAction.getSlot(), itemStack, true);

                                // Drop item
                                ItemEntity itemEntity = EntityRegistry.getItemEntity(droppedStack);
                                itemEntity.setPickupDelay(40);
                                this.player.getWorld().addItemEntity(itemEntity, this.player.getLocation().add(0, 1.3f, 0), this.player.getDirectionVector().multiply(0.25f, 0.6f, 0.25f));
                            } else {
                                // Revert clientside slot change
                                this.player.getInventory().sendSlot(this.player, nextAction.getSlot());
                            }
                        } else {
                            // Player is attempting to drop an item they don't have. Sync their inventory with the server
                            this.player.getInventory().sendSlots(this.player);
                        }
                    }
                }
                break;
            case ITEM_USE:
                InventoryTransactionUseItemData useItemData = (InventoryTransactionUseItemData) packet.getData();

                double distanceToBlock = this.player.getLocation().distanceBetween(useItemData.getBlockCoordinates().toVector3());
                if (distanceToBlock > this.player.getChunkRadius() * 16) {
                    // Prevent malicious clients from causing an OutOfMemory error by sending a transaction
                    // to every single chunk regardless of the distance which would load chunks unnecessarily
                    return;
                }

                if (this.player.canReach(useItemData.getBlockCoordinates(), 7)) {
                    Block block = this.player.getWorld().getBlock(useItemData.getBlockCoordinates());

                    boolean isCurrentSelectedSlot = useItemData.getHotbarSlot() == this.player.getInventory().getSelectedSlot();
                    if (isCurrentSelectedSlot) {
                        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(this.player, block, useItemData.getBlockFace());
                        this.player.getServer().getEventManager().call(playerInteractEvent);

                        if (!playerInteractEvent.isCancelled()) {
                            switch (useItemData.getAction()) {
                                case CLICK_BLOCK:
                                case CLICK_AIR:
                                    // the block can cancel the item interaction for cases such as crafting tables being right-clicked with a block
                                    boolean callItemInteract = block.getBlockType().onInteract(this.player, block);
                                    if (callItemInteract) {
                                        // an unsuccessful interaction will resend the blocks/slot used
                                        boolean successfulInteraction = heldItemStack.getItemType().onInteract(this.player, heldItemStack, block, useItemData.getBlockFace());
                                        if (successfulInteraction) {
                                            return; // Item interaction succeeded. Returning ensures the interaction does not get reset
                                        }
                                    } else {
                                        return; // block cancelled the item interaction. Returning ensures the interaction does not get reset
                                    }
                                    break;
                            }
                        }
                    } else {
                        // Incorrect hotbar slot. Update it
                        this.player.getInventory().setSelectedSlot(this.player.getInventory().getSelectedSlot());
                    }
                }

                // By getting to this point, it means that the action failed/was not valid/they broke a block.
                // Resend the data stored server-side.
                this.player.getWorld().sendBlock(this.player, useItemData.getBlockCoordinates());
                this.player.getWorld().sendBlock(this.player, useItemData.getBlockCoordinates().add(useItemData.getBlockFace().getOffset()));
                this.player.getInventory().sendSlot(this.player, this.player.getInventory().getSelectedSlot());
                break;
            case ITEM_USE_ON_ENTITY:
                InventoryTransactionUseItemOnEntityData useItemOnEntityData = (InventoryTransactionUseItemOnEntityData) packet.getData();

                // Get the entity targeted
                Optional<Entity> entity = this.player.getWorld().getEntity(useItemOnEntityData.getEntityRuntimeId());
                if (!entity.isPresent() || useItemOnEntityData.getEntityRuntimeId() == this.player.getId()) {
                    return;
                }
                if (entity.get().isHiddenFrom(this.player)) {
                    this.player.getServer().getLogger().warn(this.player.getUsername() + " tried to hit a entity that was hidden to them");
                    return;
                }

                if (this.player.canReach(entity.get().getLocation(), this.player.getGamemode().equals(Gamemode.CREATIVE) ? 9 : 6)) {
                    ImplEntity implEntity = (ImplEntity) entity.get();
                    switch (useItemOnEntityData.getAction()) {
                        case ATTACK:
                            AdventureSettings adventureSettings = this.player.getAdventureSettings();
                            if (((implEntity instanceof Player) && !adventureSettings.canAttackPlayers()) || !((implEntity instanceof Player) || adventureSettings.canAttackMobs())) {
                                return;
                            }

                            float damage = this.player.getInventory().getHeldItem().getItemType().getDamage();
                            EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(entity.get(),
                                    this.player,
                                    DamageCause.ATTACK,
                                    damage,
                                    ImplEntity.NO_HIT_TICKS,
                                    new Vector3(0.6f, 0.4f, 0.6f));
                            implEntity.damage(damageEvent);
                            break;
                        case INTERACT:
                            PlayerEntityInteractEvent playerEntityInteractEvent = new PlayerEntityInteractEvent(this.player, entity.get());
                            this.player.getServer().getEventManager().call(playerEntityInteractEvent);

                            if (!playerEntityInteractEvent.isCancelled()) {
                                heldItemStack.getItemType().onInteract(this.player, heldItemStack, entity.get());
                            }
                            break;
                    }
                }
                break;
            case ITEM_RELEASE:
                InventoryTransactionReleaseItemData releaseItemData = (InventoryTransactionReleaseItemData) packet.getData();
                // TODO: Implement
                break;
        }
    }

}
