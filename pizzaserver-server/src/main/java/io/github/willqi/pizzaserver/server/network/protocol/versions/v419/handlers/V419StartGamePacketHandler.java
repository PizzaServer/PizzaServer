package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.level.data.gamerules.GameRule;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.network.protocol.packets.StartGamePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419StartGamePacketHandler extends BaseProtocolPacketHandler<StartGamePacket> {

    @Override
    public void encode(StartGamePacket packet, BasePacketBuffer buffer) {
        buffer.writeVarLong(packet.getEntityId());
        buffer.writeUnsignedVarLong(packet.getRuntimeEntityId());
        buffer.writeVarInt(packet.getPlayerGamemode().ordinal());

        buffer.writeVector3(packet.getPlayerSpawn());

        buffer.writeFloatLE(packet.getPlayerRotation().getX());
        buffer.writeFloatLE(packet.getPlayerRotation().getY());

        buffer.writeVarInt(packet.getSeed());
        buffer.writeShortLE(packet.getBiomeType());
        buffer.writeString(packet.getCustomBiomeName());
        buffer.writeVarInt(packet.getDimension().ordinal());
        buffer.writeVarInt(packet.getWorldType().ordinal());
        buffer.writeVarInt(packet.getDefaultGamemode().ordinal());
        buffer.writeVarInt(packet.getDifficulty().ordinal());

        buffer.writeVector3i(packet.getWorldSpawn());

        buffer.writeBoolean(!packet.areAchievementsEnabled());
        buffer.writeVarInt(packet.getWorldTime());
        buffer.writeVarInt(packet.getServerOrigin().ordinal());
        buffer.writeBoolean(packet.areEduFeaturedEnabled());
        buffer.writeString((packet.getEduUuid() != null ? packet.getEduUuid() : "").toString());
        buffer.writeFloatLE(packet.getRainLevel());
        buffer.writeFloatLE(packet.getLightingLevel());
        buffer.writeBoolean(packet.hasPlatformLockedContent());
        buffer.writeBoolean(packet.isMultiplayer());
        buffer.writeBoolean(packet.broadcastingToLan());
        buffer.writeVarInt(packet.getXboxLiveBroadcastMode());
        buffer.writeVarInt(packet.getPlatformBroadcastMode());
        buffer.writeBoolean(packet.areCommandsEnabled());
        buffer.writeBoolean(packet.areResourcePacksRequired());

        // Game rules
        buffer.writeUnsignedVarInt(packet.getGameRules().size());
        for (GameRule<?> rule : packet.getGameRules()) {
            buffer.writeString(rule.getId().getName());
            buffer.writeUnsignedVarInt(rule.getType().getId());
            switch (rule.getType()) {
                case BOOLEAN:
                    buffer.writeBoolean(((GameRule<Boolean>) rule).getValue());
                    break;
                case INT:
                    buffer.writeUnsignedVarInt(((GameRule<Integer>) rule).getValue());
                    break;
                case FLOAT:
                    buffer.writeFloatLE(((GameRule<Float>) rule).getValue());
                    break;
                default:
                    throw new AssertionError("Tried to encode unknown gamerule type: " + rule.getType());
            }
        }

        buffer.writeExperiments(packet.getExperiments());
        buffer.writeBoolean(packet.isExperimentsPreviouslyEnabled());
        buffer.writeBoolean(packet.isBonusChestEnabled());
        buffer.writeBoolean(packet.isBonusMapEnabled());
        buffer.writeVarInt(packet.getPlayerPermissionLevel().ordinal());
        buffer.writeIntLE(packet.getChunkTickRange());
        buffer.writeBoolean(packet.hasLockedBehaviorPacks());
        buffer.writeBoolean(packet.hasLockedResourcePacks());
        buffer.writeBoolean(packet.isFromLockedWorldTemplate());
        buffer.writeBoolean(packet.useMsaGamerTagsOnly());
        buffer.writeBoolean(packet.isFromWorldTemplate());
        buffer.writeBoolean(packet.isWorldTemplateOptionsLocked());
        buffer.writeBoolean(packet.isSpawningOnlyV1VillagersAllowed());
        buffer.writeString(packet.getGameVersion());
        buffer.writeIntLE(packet.getLimitedWorldWidth());
        buffer.writeIntLE(packet.getLimitedWorldHeight());
        buffer.writeBoolean(packet.isNetherType());
        buffer.writeBoolean(packet.isExperimentalGameplayForced());
        if (packet.isExperimentalGameplayForced()) {
            buffer.writeBoolean(true);
        }

        buffer.writeString(packet.getWorldId());
        buffer.writeString(packet.getServerName());
        buffer.writeString(packet.getPremiumWorldTemplateId());
        buffer.writeBoolean(packet.isTrial());
        buffer.writeVarInt(packet.getMovementType().ordinal());
        buffer.writeLongLE(packet.getCurrentTick());
        buffer.writeVarInt(packet.getEnchantmentSeed());

        // custom blocks
        buffer.writeUnsignedVarInt(packet.getBlockProperties().size());
        for (BaseBlockType blockType : packet.getBlockProperties()) {
            this.writeBlockProperty(blockType, buffer);
        }

        // Item states
        buffer.writeUnsignedVarInt(packet.getItemStates().size());
        for (StartGamePacket.ItemState itemType : packet.getItemStates()) {
            buffer.writeString(itemType.getItemId());
            buffer.writeShortLE(itemType.getRuntimeId());
            buffer.writeBoolean(itemType.isComponentBased());
        }

        buffer.writeString(packet.getMultiplayerId().toString());
        buffer.writeBoolean(packet.isServerAuthoritativeInventory());
    }

    protected void writeBlockProperty(BaseBlockType blockType, BasePacketBuffer buffer) {
        buffer.writeString(blockType.getBlockId());

        NBTCompound blockContainer = new NBTCompound();

        NBTCompound components = new NBTCompound();
        components.putCompound("minecraft:block_light_absorption", new NBTCompound().putInteger("value", blockType.getLightAbsorption()))
                .putCompound("minecraft:block_light_emission", new NBTCompound().putFloat("emission", blockType.getLightEmission()))
                .putCompound("minecraft:destroy_time", new NBTCompound().putFloat("value", blockType.getToughness()))
                .putCompound("minecraft:friction", new NBTCompound().putFloat("value", blockType.getFriction()));
        if (blockType.getGeometry() != null) {
            components.putCompound("minecraft:geometry", new NBTCompound().putString("value", blockType.getGeometry()));
        }
        components.putCompound("minecraft:rotation", new NBTCompound()
                .putFloat("x", blockType.getRotation()[0])
                .putFloat("y", blockType.getRotation()[1])
                .putFloat("z", blockType.getRotation()[2]));

        blockContainer.putCompound("components", components);
        buffer.writeNBTCompound(blockContainer);
    }

}
