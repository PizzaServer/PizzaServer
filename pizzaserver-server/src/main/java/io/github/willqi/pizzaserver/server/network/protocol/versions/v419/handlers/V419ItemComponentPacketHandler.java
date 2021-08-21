package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.item.types.BaseItemType;
import io.github.willqi.pizzaserver.api.item.types.components.*;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemComponentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419ItemComponentPacketHandler extends BaseProtocolPacketHandler<ItemComponentPacket> {

    @Override
    public void encode(ItemComponentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getEntries().size());
        for (ItemComponentPacket.Entry entry : packet.getEntries()) {
            buffer.writeString(entry.getCustomItemType().getItemId());

            NBTCompound containerCompound = new NBTCompound();
            containerCompound.setInteger("id", entry.getRuntimeId());
            containerCompound.setString("name", entry.getCustomItemType().getItemId());

            NBTCompound components = new NBTCompound();
            this.writeComponents(entry.getCustomItemType(), components);

            containerCompound.setCompound("components", components);
            buffer.writeNBTCompound(containerCompound);
        }
    }

    protected void writeComponents(BaseItemType itemType, NBTCompound components) {
        NBTCompound properties = new NBTCompound();
        this.writeItemProperties(itemType, properties);
        components.setCompound("item_properties", properties);

        components.setCompound("minecraft:icon", new NBTCompound()
                .setString("texture", itemType.getIconName()));

        // Write non-required components if present
        if (itemType instanceof ArmorItemComponent) {
            ArmorItemComponent armorItemComponent = (ArmorItemComponent)itemType;
            components.setCompound("minecraft:armor", new NBTCompound()
                    .setInteger("protection", armorItemComponent.getProtection()));
        }
        if (itemType instanceof CooldownItemComponent) {
            CooldownItemComponent cooldownItemComponent = (CooldownItemComponent)itemType;
            components.setCompound("minecraft:cooldown", new NBTCompound()
                    .setString("category", cooldownItemComponent.getCooldownCategory())
                    .setFloat("duration", (cooldownItemComponent.getCooldownTicks() * 20) / 20f));
        }
        if (itemType instanceof DurableItemComponent) {
            DurableItemComponent durableItemComponent = (DurableItemComponent)itemType;
            components.setCompound("minecraft:durability", new NBTCompound()
                    .setInteger("max_durability", durableItemComponent.getMaxDurability()));
        }
        if (itemType instanceof FoodItemComponent) {
            FoodItemComponent foodItemComponent = (FoodItemComponent)itemType;
            components.setCompound("minecraft:food", new NBTCompound()
                    .setByte("can_always_eat", foodItemComponent.canAlwaysBeEaten() ? (byte)0x01 : (byte)0x00));
        }
        if (itemType instanceof PlantableItemComponent) {
            components.setCompound("minecraft:block_placer", new NBTCompound());
        }
    }

    /**
     * Writes all item properties for an item
     * https://wiki.vg/Bedrock_Protocol#Item_Component
     * @param itemType
     * @param properties
     */
    protected void writeItemProperties(BaseItemType itemType, NBTCompound properties) {
        properties.setByte("allow_off_hand", itemType.isAllowedInOffHand() ? (byte)0x01 : (byte)0x00)
                .setByte("hand_equipped", itemType.isHandEquipped() ? (byte)0x01 : (byte)0x00)
                .setByte("liquid_clipped", itemType.canClickOnLiquids() ? (byte)0x01 : (byte)0x00)
                .setInteger("max_stack_size", itemType.getMaxStackSize());
    }

}
