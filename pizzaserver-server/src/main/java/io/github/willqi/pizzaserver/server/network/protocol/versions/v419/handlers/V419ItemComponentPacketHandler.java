package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.item.types.ItemType;
import io.github.willqi.pizzaserver.api.item.types.components.*;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.api.network.protocol.packets.ItemComponentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ItemComponentPacketHandler extends BaseProtocolPacketHandler<ItemComponentPacket> {

    @Override
    public void encode(ItemComponentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getEntries().size());
        for (ItemComponentPacket.Entry entry : packet.getEntries()) {
            buffer.writeString(entry.getCustomItemType().getItemId());

            NBTCompound containerCompound = new NBTCompound();
            containerCompound.putInteger("id", entry.getRuntimeId())
                    .putString("name", entry.getCustomItemType().getItemId());

            NBTCompound components = new NBTCompound();
            this.writeComponents(entry.getCustomItemType(), components);
            containerCompound.putCompound("components", components);

            buffer.writeNBTCompound(containerCompound);
        }
    }

    protected void writeComponents(ItemType itemType, NBTCompound components) {
        NBTCompound properties = new NBTCompound();
        this.writeItemProperties(itemType, properties);
        components.putCompound("item_properties", properties);

        components.putCompound("minecraft:icon", new NBTCompound()
                .putString("texture", itemType.getIconName()));

        // Write non-required components if present
        if (itemType instanceof ArmorItemComponent) {
            ArmorItemComponent armorItemComponent = (ArmorItemComponent) itemType;
            components.putCompound("minecraft:armor", new NBTCompound()
                    .putInteger("protection", armorItemComponent.getProtection()));
        }
        if (itemType instanceof CooldownItemComponent) {
            CooldownItemComponent cooldownItemComponent = (CooldownItemComponent) itemType;
            components.putCompound("minecraft:cooldown", new NBTCompound()
                    .putString("category", cooldownItemComponent.getCooldownCategory())
                    .putFloat("duration", (cooldownItemComponent.getCooldownTicks() * 20) / 20f));
        }
        if (itemType instanceof DurableItemComponent) {
            DurableItemComponent durableItemComponent = (DurableItemComponent) itemType;
            components.putCompound("minecraft:durability", new NBTCompound()
                    .putInteger("max_durability", durableItemComponent.getMaxDurability()));
        }
        if (itemType instanceof FoodItemComponent) {
            FoodItemComponent foodItemComponent = (FoodItemComponent) itemType;
            components.putCompound("minecraft:food", new NBTCompound()
                    .putBoolean("can_always_eat", foodItemComponent.canAlwaysBeEaten()));
        }
        if (itemType instanceof PlantableItemComponent) {
            components.putCompound("minecraft:block_placer", new NBTCompound());
        }
    }

    /**
     * Writes all item properties for an item
     * https://wiki.vg/Bedrock_Protocol#Item_Component
     * @param itemType item type to write to use
     * @param properties NBTCompound to write properties to
     */
    protected void writeItemProperties(ItemType itemType, NBTCompound properties) {
        properties.putBoolean("allow_off_hand", itemType.isAllowedInOffHand())
                .putInteger("creative_category", 2)
                .putInteger("damage", itemType.getDamage())
                .putBoolean("foil", itemType.hasFoil())
                .putBoolean("hand_equipped", itemType.isHandEquipped())
                .putBoolean("liquid_clipped", itemType.canClickOnLiquids())
                .putInteger("max_stack_size", itemType.getMaxStackSize())
                .putFloat("mining_speed", 0)  // Block breaking is handled server-side. Doing this gives greater block break control in the item type class
                .putBoolean("mirrored_art", itemType.isMirroredArt())
                .putBoolean("stacked_by_data", itemType.isStackedByDamage())
                .putInteger("use_animation", itemType.getUseAnimationType().ordinal())
                .putInteger("use_duration", itemType.getUseDuration());
    }

}
