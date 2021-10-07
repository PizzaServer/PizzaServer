package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.event.type.inventory.InventoryDropItemEvent;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerEntityInteractEvent;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerHotbarSelectEvent;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerInteractEvent;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.server.entity.inventory.InventoryID;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.handlers.inventory.InventoryActionPlaceHandler;
import io.github.willqi.pizzaserver.server.network.handlers.inventory.InventoryActionSwapHandler;
import io.github.willqi.pizzaserver.server.network.handlers.inventory.InventoryActionTakeHandler;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.*;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.InventoryTransactionAction;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.data.InventoryTransactionReleaseItemData;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.data.InventoryTransactionUseItemData;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.data.InventoryTransactionUseItemOnEntityData;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources.InventoryTransactionSourceType;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources.InventoryTransactionWorldSource;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryPacketHandler extends BaseBedrockPacketHandler {
    private final ImplPlayer player;


    public InventoryPacketHandler(ImplPlayer player) {
        this.player = player;
    }

    @Override
    public void onPacket(InteractPacket packet) {
        if (packet.getAction() == InteractPacket.Type.OPEN_INVENTORY && !this.player.getOpenInventory().isPresent()) {
            this.player.openInventory(this.player.getInventory());
        }
    }

    @Override
    public void onPacket(ContainerClosePacket packet) {
        this.player.closeOpenInventory();
    }

    @Override
    public void onPacket(ItemStackRequestPacket packet) {
        ItemStackResponsePacket itemStackResponsePacket = new ItemStackResponsePacket();
        List<ItemStackResponsePacket.Response> responses = new ArrayList<>();

        for (ItemStackRequestPacket.Request request : packet.getRequests()) {
            int requestId = request.getId();
            ItemStackResponsePacket.Response response = new ItemStackResponsePacket.Response(requestId);

            boolean continueActions = true;
            for (InventoryAction action : request.getActions()) {
                if (!continueActions) {  // e.g. if the inventory action is incorrect
                    break;
                }

                switch (action.getType()) {
                    case TAKE:
                        continueActions = InventoryActionTakeHandler.INSTANCE.tryAction(response, this.player, (InventoryActionTake) action);
                        break;
                    case PLACE:
                        continueActions = InventoryActionPlaceHandler.INSTANCE.tryAction(response, this.player, (InventoryActionPlace) action);
                        break;
                    case SWAP:
                        continueActions = InventoryActionSwapHandler.INSTANCE.tryAction(response, this.player, (InventoryActionSwap) action);
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
                    case AUTO_CRAFT_RECIPE:
                        break;
                    case CRAFT_CREATIVE:
                        break;
                    case CRAFT_RECIPE_OPTIONAL:
                        break;
                    case CRAFT_NOT_IMPLEMENTED:
                        break;
                    case CRAFT_RESULTS_DEPRECATED:
                        break;
                }
                responses.add(response);
            }
        }

        itemStackResponsePacket.setResponses(responses);
        this.player.sendPacket(itemStackResponsePacket);
    }

    @Override
    public void onPacket(MobEquipmentPacket packet) {
        boolean isHotbarSlot = packet.getSlot() >= 0 && packet.getSlot() < 9;
        if (isHotbarSlot && packet.getInventoryId() == InventoryID.MAIN_INVENTORY) {
            // If their item does not match up with the server side item resend the server's slot
            if (!packet.getEquipment().equals(this.player.getInventory().getSlot(packet.getSlot()))) {
                this.player.getInventory().sendSlot(this.player, packet.getSlot());
            }

            // Handle hotbar slot change
            if (packet.getSlot() != this.player.getInventory().getSelectedSlot()) {
                PlayerHotbarSelectEvent playerHotbarSelectEvent = new PlayerHotbarSelectEvent(this.player, packet.getSlot());
                this.player.getServer().getEventManager().call(playerHotbarSelectEvent);

                if (!playerHotbarSelectEvent.isCancelled()) {
                    // Server approves of hotbar slot change.
                    this.player.getInventory().setSelectedSlot(packet.getSlot(), true);
                } else {
                    // Reset their selected slot back to the old slot
                    this.player.getInventory().setSelectedSlot(this.player.getInventory().getSelectedSlot());
                }
            }
        }
    }

    @Override
    public void onPacket(InventoryTransactionPacket packet) {
        ItemStack heldItemStack = this.player.getInventory().getHeldItem();

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
                            // TODO: spawn item entity logic

                            InventoryDropItemEvent dropItemEvent = new InventoryDropItemEvent(this.player.getInventory(), this.player, droppedStack);
                            this.player.getServer().getEventManager().call(dropItemEvent);
                            if (!dropItemEvent.isCancelled()) {
                                // update server inventory to reflect dropped count
                                itemStack.setCount(itemStack.getCount() - amountDropped);
                                this.player.getInventory().setSlot(this.player, nextAction.getSlot(), itemStack, true);
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
                    // Prevent malicious clients from causing a OutOfMemory error by sending a transaction
                    // to every single chunk regardless of the distance which would load chunks unnecessarily
                    return;
                }

                if (this.player.canReach(useItemData.getBlockCoordinates())) {
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
                                case BREAK_BLOCK:
                                    if (this.player.getBlockBreaking().isPresent()) {
                                        this.player.getServer().getLogger().debug(String.format("%s tried to break a block too early.", this.player.getUsername()));
                                    }
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

                if (this.player.canReach(entity.get().getLocation())) {
                    switch (useItemOnEntityData.getAction()) {
                        case ATTACK:
                            // TODO: deal damage
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
