package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.server.entity.inventory.BaseEntityInventory;
import io.github.willqi.pizzaserver.server.entity.inventory.ImplPlayerInventory;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventorySlot;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions.InventoryAction;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions.InventoryActionTake;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ContainerClosePacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InteractPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackRequestPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackResponsePacket;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        Set<ItemStackResponsePacket.Response> responses = new HashSet<>();

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
                        continueActions = this.handleTakeInventoryAction(response, (InventoryActionTake)action);
                        break;
                    case PLACE:
                        break;
                    case SWAP:
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

    /**
     * Called when the player tries to transfer an item stack from another inventory to their cursor
     * @param action the taking action
     * @return if the action should go through
     */
    private boolean handleTakeInventoryAction(ItemStackResponsePacket.Response response, InventoryActionTake action) {
        boolean isActionValidated = this.stackExists(action.getSource()) &&
                (action.getDestination().getInventorySlotType() == InventorySlotType.CURSOR) &&
                this.player.getInventory().getCursor().getItemType().getItemId().equals(BlockTypeID.AIR);
        if (isActionValidated) {
            BaseEntityInventory sourceInventory = (BaseEntityInventory)this.getInventory(action.getSource()).get();  // We verified that the slot exists
            ItemStack newCursorItem;

            // Offhand/armor slots are treated differently from normal inventory slots
            // so we need to handle them differently.
            boolean isUniquePlayerSlot = sourceInventory instanceof PlayerInventory &&
                    (action.getSource().getInventorySlotType() != InventorySlotType.INVENTORY &&
                            action.getSource().getInventorySlotType() != InventorySlotType.HOTBAR);
            if (isUniquePlayerSlot) {
                // Handle the slot differently
                ImplPlayerInventory playerInventory = (ImplPlayerInventory)sourceInventory;

                switch (action.getSource().getInventorySlotType()) {
                    case OFFHAND:
                        newCursorItem = playerInventory.getOffhandItem();
                        playerInventory.setOffhandItem(null);
                        break;
                    case ARMOR:
                        switch (action.getSource().getSlot()) {
                            case 0:
                                newCursorItem = playerInventory.getHelmet();
                                playerInventory.setHelmet(this.player, null, false);
                                break;
                            case 1:
                                newCursorItem = playerInventory.getChestplate();
                                playerInventory.setChestplate(this.player, null, false);
                                break;
                            case 2:
                                newCursorItem = playerInventory.getLeggings();
                                playerInventory.setLeggings(this.player, null, false);
                                break;
                            case 3:
                                newCursorItem = playerInventory.getBoots();
                                playerInventory.setBoots(this.player, null, false);
                                break;
                            default:
                                this.player.getServer().getLogger().warn("Missing armor slot handler for take request in InventoryPacketHandler");
                                return false;
                        }
                        break;
                    default:
                        this.player.getServer().getLogger().warn("Missing player handler for take request in InventoryPacketHandler");
                        return false;
                }
            } else {
                // Handle the slot like a generic inventory slot
                newCursorItem = sourceInventory.getSlot(action.getSource().getSlot());
                sourceInventory.setSlot(this.player, action.getSource().getSlot(), null, false);
            }

            // Assign/register the slot change to the cursor of the player
            this.player.getInventory().setCursor(newCursorItem, true);
            response.addSlotChange(
                    InventorySlotType.CURSOR,
                    new ItemStackResponsePacket.Response.SlotInfo.Builder()
                            .setItemStackNetworkId(newCursorItem.getNetworkId())
                            .setItemStackCount(newCursorItem.getCount())
                            .build());

            // Register the slot change removing the item from the inventory
            response.addSlotChange(
                    action.getSource().getInventorySlotType(),
                    new ItemStackResponsePacket.Response.SlotInfo.Builder()
                            .setItemStackNetworkId(0)
                            .setItemStackCount(0)
                            .setSlot(action.getSource().getSlot())
                            .setHotbarSlot(action.getSource().getSlot())
                            .build());
        }

        return isActionValidated;
    }

    private Optional<EntityInventory> getInventory(InventorySlot slot) {
        Optional<EntityInventory> openInventory = this.player.getOpenInventory();
        if (this.player.getInventory().getSlotTypes().contains(slot.getInventorySlotType())) {
            return Optional.of(this.player.getInventory());
        } else if (openInventory.isPresent() && openInventory.get().getSlotTypes().contains(slot.getInventorySlotType())) {
            return openInventory;
        }
        return Optional.empty();
    }

    private boolean stackExists(InventorySlot slot) {
        Optional<EntityInventory> openInventory = this.player.getOpenInventory();
        if (this.player.getInventory().getSlotTypes().contains(slot.getInventorySlotType())) {
            // only consider the main inventory
            return this.playerInventoryStackExists(slot);
        } else if (openInventory.isPresent() && openInventory.get().getSlotTypes().contains(slot.getInventorySlotType())) {
            // only consider the open inventory
            // Check that the slot exists and matches
            return slot.getSlot() >= 0 &&
                    slot.getSlot() < openInventory.get().getSize() &&
                    openInventory.get().getSlot(slot.getSlot()).getNetworkId() == slot.getNetworkStackId();
        }
        return false;
    }

    private boolean playerInventoryStackExists(InventorySlot slot) {
        switch (slot.getInventorySlotType()) {
            case ARMOR:
                switch (slot.getSlot()) {
                    case 0:
                        return this.player.getInventory().getHelmet().getNetworkId() == slot.getNetworkStackId();
                    case 1:
                        return this.player.getInventory().getChestplate().getNetworkId() == slot.getNetworkStackId();
                    case 2:
                        return this.player.getInventory().getLeggings().getNetworkId() == slot.getNetworkStackId();
                    case 3:
                        return this.player.getInventory().getBoots().getNetworkId() == slot.getNetworkStackId();
                    default:
                        return false;   // Armor slots are only 0-3
                }
            case OFFHAND:
                return slot.getSlot() == 1 && this.player.getInventory().getOffhandItem().getNetworkId() == slot.getNetworkStackId();
            case CURSOR:
                return slot.getSlot() == 0 && this.player.getInventory().getCursor().getNetworkId() == slot.getNetworkStackId();
            default:
                return slot.getSlot() >= 0 &&
                        slot.getSlot() < this.player.getInventory().getSize() &&
                        this.player.getInventory().getSlot(slot.getSlot()).getNetworkId() == slot.getNetworkStackId();
        }
    }

}
