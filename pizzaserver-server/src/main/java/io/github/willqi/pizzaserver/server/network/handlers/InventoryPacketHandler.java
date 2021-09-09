package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerEntityInteractEvent;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerInteractEvent;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.item.types.BlockItemType;
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
                        continueActions = InventoryActionTakeHandler.INSTANCE.tryAction(response, this.player, (InventoryActionTake)action);
                        break;
                    case PLACE:
                        continueActions = InventoryActionPlaceHandler.INSTANCE.tryAction(response, this.player, (InventoryActionPlace)action);
                        break;
                    case SWAP:
                        continueActions = InventoryActionSwapHandler.INSTANCE.tryAction(response, this.player, (InventoryActionSwap)action);
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
                    default:
                        this.player.getServer().getLogger().warn("Unhandled inventory item stack request type: " + action.getType());
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
        if (packet.getSlot() >= 0 && packet.getSlot() <= 8 && packet.getInventoryId() == InventoryID.MAIN_INVENTORY) {
            this.player.getInventory().setSelectedSlot(packet.getSlot(), false);
        }
    }

    @Override
    public void onPacket(InventoryTransactionPacket packet) {
        ItemStack heldItemStack = this.player.getInventory().getHeldItem();
        switch (packet.getType()) {
            case NORMAL:
                for (InventoryTransactionAction action : packet.getActions()) {
                    // Dropping items is still handled by this packet despite server authoritative inventories
                    boolean isDropAction = action.getSource().getType() == InventoryTransactionSourceType.WORLD &&
                            ((InventoryTransactionWorldSource)action.getSource()).getFlag() == InventoryTransactionWorldSource.Flag.DROP_ITEM &&
                            action.getSlot() >= 0 && action.getSlot() < 9;

                    if (isDropAction) {
                        if (!heldItemStack.isEmpty()) {
                            heldItemStack.setCount(heldItemStack.getCount() - 1);
                            this.player.getInventory().setSlot(action.getSlot(), heldItemStack);
                        } else {
                            // Player is attempting to drop an item they don't have. Sync their inventory with the server
                            this.player.getInventory().sendSlots(this.player);
                        }
                    }
                }
                break;
            case ITEM_USE:
                InventoryTransactionUseItemData useItemData = (InventoryTransactionUseItemData)packet.getData();
                // TODO: account for creative mode reach when gamemodes are implemented
                // Mobile clients have a reach of 6 whereas regular clients have a range of 5.
                // For interacting with the world we don't really care about the difference.
                double distanceToBlock = this.player.getLocation().distanceTo(useItemData.getBlockCoordinates());
                if (distanceToBlock <= 6) {
                    Block block = this.player.getWorld().getBlock(useItemData.getBlockCoordinates());

                    PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(this.player, block, useItemData.getBlockFace());
                    this.player.getServer().getEventManager().call(playerInteractEvent);

                    if (playerInteractEvent.isCancelled()) {
                        // Reset any clientside modifications to the block interacted with
                        this.player.getWorld().sendBlock(this.player, useItemData.getBlockCoordinates());
                        this.player.getWorld().sendBlock(this.player, useItemData.getBlockCoordinates().add(useItemData.getBlockFace().getOffset()));
                        this.player.getInventory().sendSlot(this.player, this.player.getInventory().getSelectedSlot());
                        return;
                    }

                    switch (useItemData.getAction()) {
                        case CLICK_BLOCK:
                        case CLICK_AIR:
                            boolean callItemInteract = block.getBlockType().onInteract(this.player, block);
                            if (callItemInteract) {
                                boolean successfulInteraction = heldItemStack.getItemType().onInteract(this.player, heldItemStack, block, useItemData.getBlockFace());
                                if (!successfulInteraction) {
                                    // Reset any clientside modifications to the block interacted with
                                    this.player.getWorld().sendBlock(this.player, useItemData.getBlockCoordinates());
                                    this.player.getWorld().sendBlock(this.player, useItemData.getBlockCoordinates().add(useItemData.getBlockFace().getOffset()));
                                    this.player.getInventory().sendSlot(this.player, this.player.getInventory().getSelectedSlot());
                                }
                            }
                            break;
                    }
                }
                break;
            case ITEM_USE_ON_ENTITY:
                InventoryTransactionUseItemOnEntityData useItemOnEntityData = (InventoryTransactionUseItemOnEntityData)packet.getData();

                // Get the entity targeted
                Optional<Entity> entity = this.player.getWorld().getEntity(useItemOnEntityData.getEntityRuntimeId());
                if (!entity.isPresent() || useItemOnEntityData.getEntityRuntimeId() == this.player.getId()) {
                    return;
                }

                // Mobile clients have a reach of 6 whereas regular clients have a range of 5.
                // but we want to be fair. So reach must be under or equal to 5 blocks
                if (entity.get().getLocation().distanceTo(this.player.getLocation()) <= 5) {
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
                InventoryTransactionReleaseItemData releaseItemData = (InventoryTransactionReleaseItemData)packet.getData();
                // TODO: Implement
                break;
        }
    }

}
