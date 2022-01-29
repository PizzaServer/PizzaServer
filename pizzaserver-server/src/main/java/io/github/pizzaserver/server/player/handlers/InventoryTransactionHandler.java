package io.github.pizzaserver.server.player.handlers;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.inventory.*;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.*;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.entity.data.DamageCause;
import io.github.pizzaserver.api.event.type.entity.EntityDamageByEntityEvent;
import io.github.pizzaserver.api.event.type.inventory.InventoryDropItemEvent;
import io.github.pizzaserver.api.event.type.player.PlayerEntityInteractEvent;
import io.github.pizzaserver.api.event.type.player.PlayerHotbarSelectEvent;
import io.github.pizzaserver.api.event.type.player.PlayerInteractEvent;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;
import io.github.pizzaserver.api.player.AdventureSettings;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.entity.ImplEntity;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.network.data.inventory.InventoryTransactionAction;
import io.github.pizzaserver.server.network.data.inventory.StackResponse;
import io.github.pizzaserver.server.network.data.inventory.actions.PlaceStackRequestActionDataWrapper;
import io.github.pizzaserver.server.network.data.inventory.actions.SwapStackRequestActionDataWrapper;
import io.github.pizzaserver.server.network.data.inventory.actions.TakeStackRequestActionDataWrapper;
import io.github.pizzaserver.server.player.ImplPlayer;
import io.github.pizzaserver.server.player.handlers.inventory.InventoryActionPlaceHandler;
import io.github.pizzaserver.server.player.handlers.inventory.InventoryActionSwapHandler;
import io.github.pizzaserver.server.player.handlers.inventory.InventoryActionTakeHandler;

import java.util.Optional;

public class InventoryTransactionHandler implements BedrockPacketHandler {

    private final ImplPlayer player;

    public InventoryTransactionHandler(ImplPlayer player) {
        this.player = player;
    }

    @Override
    public boolean handle(ItemStackRequestPacket packet) {
        if (this.player.isAlive()) {
            ItemStackResponsePacket itemStackResponsePacket = new ItemStackResponsePacket();

            boolean resendInventory = false;
            for (ItemStackRequest request : packet.getRequests()) {
                int requestId = request.getRequestId();
                StackResponse response = new StackResponse(requestId);

                boolean continueActions = true;
                for (StackRequestActionData action : request.getActions()) {
                    if (!continueActions) {  // e.g. if the inventory action is incorrect
                        resendInventory = true;
                        break;
                    }

                    switch (action.getType()) {
                        case TAKE:
                            TakeStackRequestActionDataWrapper takeStackWrapper = new TakeStackRequestActionDataWrapper(this.player, (TakeStackRequestActionData) action);
                            continueActions = InventoryActionTakeHandler.INSTANCE.tryAction(this.player, takeStackWrapper);
                            if (continueActions) {
                                response.addChange(takeStackWrapper.getSource());
                                response.addChange(takeStackWrapper.getDestination());
                            }
                            break;
                        case PLACE:
                            PlaceStackRequestActionDataWrapper placeStackWrapper = new PlaceStackRequestActionDataWrapper(this.player, (PlaceStackRequestActionData) action);
                            continueActions = InventoryActionPlaceHandler.INSTANCE.tryAction(this.player, placeStackWrapper);
                            if (continueActions) {
                                response.addChange(placeStackWrapper.getSource());
                                response.addChange(placeStackWrapper.getDestination());
                            }
                            break;
                        case SWAP:
                            SwapStackRequestActionDataWrapper swapStackWrapper = new SwapStackRequestActionDataWrapper(this.player, (SwapStackRequestActionData) action);
                            continueActions = InventoryActionSwapHandler.INSTANCE.tryAction(this.player, swapStackWrapper);
                            if (continueActions) {
                                response.addChange(swapStackWrapper.getSource());
                                response.addChange(swapStackWrapper.getDestination());
                            }
                            break;
                        case MINE_BLOCK:
                            response.addChange(new InventorySlotContainer(this.player,
                                    ContainerSlotType.HOTBAR,
                                    ((MineBlockStackRequestActionData) action).getHotbarSlot()));
                            break;
                        case DROP:
                        case DESTROY:
                        case CONSUME:
                        case LAB_TABLE_COMBINE:
                        case BEACON_PAYMENT:
                        case CRAFT_RECIPE:
                        case CRAFT_RECIPE_AUTO:
                        case CRAFT_CREATIVE:
                        case CRAFT_RECIPE_OPTIONAL:
                        case CRAFT_REPAIR_AND_DISENCHANT:
                        case CRAFT_LOOM:
                        case CRAFT_NON_IMPLEMENTED_DEPRECATED:
                        case CRAFT_RESULTS_DEPRECATED:
                            break;
                    }
                    itemStackResponsePacket.getEntries().add(response.serialize());
                }
            }
            this.player.sendPacket(itemStackResponsePacket);

            if (resendInventory) {
                // If a desync does occur, send the inventory
                if (this.player.getOpenInventory().isPresent()) {
                    this.player.getOpenInventory().get().sendSlots(this.player);
                }
                this.player.getInventory().sendSlots(this.player);
            }
        }
        return true;
    }

    @Override
    public boolean handle(MobEquipmentPacket packet) {
        boolean isHotbarSlot = packet.getHotbarSlot() >= 0 && packet.getHotbarSlot() < 9;
        if (isHotbarSlot && packet.getContainerId() == ContainerId.INVENTORY) {
            // Handle hotbar slot change
            if (packet.getHotbarSlot() != this.player.getInventory().getSelectedSlot()) {
                PlayerHotbarSelectEvent playerHotbarSelectEvent = new PlayerHotbarSelectEvent(this.player, packet.getHotbarSlot());
                this.player.getServer().getEventManager().call(playerHotbarSelectEvent);

                if (!playerHotbarSelectEvent.isCancelled()) {
                    // Server approves of hotbar slot change.

                    // Check if we need to recalculate our break time for the block we may be breaking.
                    boolean differentItemTypes = !this.player.getInventory().getHeldItem().equals(this.player.getInventory().getSlot(packet.getHotbarSlot()));
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
        if (this.player.isAlive()) {
            switch (packet.getTransactionType()) {
                case NORMAL -> this.handleDropInventoryTransaction(packet);
                case ITEM_USE -> this.handleUseInventoryTransaction(packet.getBlockPosition(),
                        BlockFace.resolve(packet.getBlockFace()),
                        packet.getClickPosition(),
                        packet.getHotbarSlot(),
                        packet.getActionType(),
                        packet.getItemInHand());
                case ITEM_USE_ON_ENTITY -> this.handleUseEntityInventoryTransaction(packet);
                case ITEM_RELEASE -> this.handleReleaseInventoryTransaction(packet);
            }
        }
        return true;
    }

    // TODO: move this to the AuthInputHandler once ALL inventory transactions are handled there
    // at the moment, only the use inventory transaction is handled
    @Override
    public boolean handle(PlayerAuthInputPacket packet) {
        if (this.player.isAlive() && this.player.hasSpawned() && packet.getItemUseTransaction() != null) {
            this.handleUseInventoryTransaction(packet.getItemUseTransaction().getBlockPosition(),
                    BlockFace.resolve(packet.getItemUseTransaction().getBlockFace()),
                    packet.getItemUseTransaction().getClickPosition(),
                    packet.getItemUseTransaction().getHotbarSlot(),
                    packet.getItemUseTransaction().getActionType(),
                    packet.getItemUseTransaction().getItemInHand());
        }
        return false;   // HACK: to get around packet handler not calling PlayerAuthInputHandler.
    }

    private void handleDropInventoryTransaction(InventoryTransactionPacket packet) {
        // Dropping items is still handled by this packet despite server authoritative inventories
        for (int actionIndex = 0; actionIndex < packet.getActions().size() - 1; actionIndex++) {
            // The slot of the dropped item is sent in the action right after the world interaction
            InventoryActionData action = packet.getActions().get(actionIndex);
            InventoryActionData nextAction = packet.getActions().get(actionIndex + 1);

            boolean isDropAction = action.getSource().getType() == InventorySource.Type.WORLD_INTERACTION
                    && action.getSource().getFlag() == InventorySource.Flag.DROP_ITEM
                    && nextAction.getSlot() >= 0 && nextAction.getSlot() < 9;

            if (isDropAction) {
                Item itemStack = this.player.getInventory().getSlot(nextAction.getSlot());
                if (!itemStack.isEmpty()) {
                    // Update stack with amount dropped
                    int amountDropped = Math.max(0, Math.min(itemStack.getCount(), action.getToItem().getCount()));

                    Item droppedStack = itemStack.clone();
                    droppedStack.setCount(amountDropped);

                    InventoryDropItemEvent dropItemEvent = new InventoryDropItemEvent(this.player.getInventory(), this.player, droppedStack);
                    this.player.getServer().getEventManager().call(dropItemEvent);
                    if (!dropItemEvent.isCancelled()) {
                        // update server inventory to reflect dropped count
                        itemStack.setCount(itemStack.getCount() - amountDropped);
                        this.player.getInventory().setSlot(this.player, nextAction.getSlot(), itemStack, true);

                        // Drop item
                        EntityItem itemEntity = EntityRegistry.getInstance().getItemEntity(droppedStack);
                        itemEntity.setPickupDelay(40);
                        this.player.getWorld().addItemEntity(itemEntity, this.player.getLocation().toVector3f().add(0, 1.3f, 0), this.player.getDirectionVector().mul(0.25f, 0.6f, 0.25f));
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
    }

    private void handleUseInventoryTransaction(Vector3i blockCoordinates, BlockFace blockFace, Vector3f clickPosition, int hotbarSlot, int actionType, ItemData itemData) {
        double distanceToBlock = this.player.getLocation().toVector3f().distance(blockCoordinates.toFloat());
        if (distanceToBlock > this.player.getChunkRadius() * 16) {
            // Prevent malicious clients from causing an OutOfMemory error by sending a transaction
            // to every single chunk regardless of the distance which would load chunks unnecessarily
            return;
        }

        if (this.player.canReach(blockCoordinates, this.player.isCreativeMode() ? 13 : 7)) {
            Block block = this.player.getWorld().getBlock(blockCoordinates);

            boolean isCurrentSelectedSlot = hotbarSlot == this.player.getInventory().getSelectedSlot();
            if (isCurrentSelectedSlot) {
                PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(this.player, block, blockFace);
                this.player.getServer().getEventManager().call(playerInteractEvent);

                if (!playerInteractEvent.isCancelled()) {
                    Item heldItemStack = this.player.getInventory().getHeldItem();
                    switch (actionType) {
                        case InventoryTransactionAction.USE_CLICK_BLOCK:
                        case InventoryTransactionAction.USE_CLICK_AIR:
                            // the block can cancel the item interaction for cases such as crafting tables being right-clicked with a block
                            boolean callItemInteractAllowedByBlock = block.getBehavior().onInteract(this.player, block, blockFace, clickPosition);
                            boolean callItemInteractAllowedByBlockEntity = block.getWorld().getBlockEntity(blockCoordinates).isEmpty()
                                    || block.getWorld().getBlockEntity(blockCoordinates).get().onInteract(this.player);
                            if (callItemInteractAllowedByBlock && callItemInteractAllowedByBlockEntity) {
                                // an unsuccessful interaction will resend the blocks/slot used
                                boolean successfulInteraction = heldItemStack.getBehavior().onInteract(this.player, heldItemStack, block, blockFace, clickPosition);
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

        // By getting to this point, it means that the action failed/was not valid.
        // Resend the data stored server-side.
        this.player.getWorld().sendBlock(this.player, blockCoordinates);
        this.player.getWorld().sendBlock(this.player, blockCoordinates.add(blockFace.getOffset()));

        // This packet is spammed by the client and sending the inventory slot EVERY single time causes ugly visual effects
        // while towering up with blocks. This fixes it by only sending the item again if the counts do not match up.
        // However, durable items bypass this check as the count will not change despite usage of the item.
        if ((itemData.getCount() != this.player.getInventory().getHeldItem().getCount())
                || (this.player.getInventory().getHeldItem() instanceof DurableItemComponent)) {
            this.player.getInventory().sendSlot(this.player, this.player.getInventory().getSelectedSlot());
        }
    }

    private void handleUseEntityInventoryTransaction(InventoryTransactionPacket packet) {
        // Get the entity targeted
        Optional<Entity> entity = this.player.getWorld().getEntity(packet.getRuntimeEntityId());
        if (entity.isEmpty() || packet.getRuntimeEntityId() == this.player.getId()) {
            return;
        }
        if (entity.get().isHiddenFrom(this.player)) {
            this.player.getServer().getLogger().warn(this.player.getUsername() + " tried to hit a entity that was hidden to them");
            return;
        }

        if (this.player.canReach(entity.get().getLocation().toVector3f(), this.player.isCreativeMode() ? 9 : 6)) {
            ImplEntity implEntity = (ImplEntity) entity.get();
            switch (packet.getActionType()) {
                case InventoryTransactionAction.USE_ENTITY_INTERACT:
                    PlayerEntityInteractEvent playerEntityInteractEvent = new PlayerEntityInteractEvent(this.player, entity.get());
                    this.player.getServer().getEventManager().call(playerEntityInteractEvent);

                    if (!playerEntityInteractEvent.isCancelled()) {
                        Item heldItemStack = this.player.getInventory().getHeldItem();
                        heldItemStack.getBehavior().onInteract(this.player, heldItemStack, entity.get());
                    }
                    break;
                case InventoryTransactionAction.USE_ENTITY_ATTACK:
                    AdventureSettings adventureSettings = this.player.getAdventureSettings();
                    if (((implEntity instanceof Player) && !adventureSettings.canAttackPlayers()) || !((implEntity instanceof Player) || adventureSettings.canAttackMobs())) {
                        return;
                    }

                    float damage = this.player.getInventory().getHeldItem().getDamage();
                    EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(entity.get(),
                            this.player,
                            DamageCause.ATTACK,
                            damage,
                            ImplEntity.NO_HIT_TICKS,
                            Vector3f.from(0.6f, 0.4f, 0.6f));
                    implEntity.damage(damageEvent);
                    break;
            }
        }
    }

    private void handleReleaseInventoryTransaction(InventoryTransactionPacket packet) {

    }

}
