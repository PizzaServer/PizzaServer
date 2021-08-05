package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.api.network.protocol.data.ItemState;
import io.github.willqi.pizzaserver.api.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.network.protocol.packets.StartGamePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.commons.world.gamerules.GameRule;
import io.netty.buffer.ByteBuf;

public class V419StartGamePacketHandler extends BaseProtocolPacketHandler<StartGamePacket> {

    @Override
    public void encode(StartGamePacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeLong(buffer, packet.getEntityId());
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        VarInts.writeInt(buffer, packet.getPlayerGamemode().ordinal());

        helper.writeVector3(buffer, packet.getPlayerSpawn());

        buffer.writeFloatLE(packet.getPlayerRotation().getX());
        buffer.writeFloatLE(packet.getPlayerRotation().getY());

        VarInts.writeInt(buffer, packet.getSeed());
        buffer.writeShortLE(packet.getBiomeType());
        helper.writeString(packet.getCustomBiomeName(), buffer);
        VarInts.writeInt(buffer, packet.getDimension().ordinal());
        VarInts.writeInt(buffer, packet.getWorldType().ordinal());
        VarInts.writeInt(buffer, packet.getDefaultGamemode().ordinal());
        VarInts.writeInt(buffer, packet.getDifficulty().ordinal());

        helper.writeBlockVector(buffer, packet.getWorldSpawn());

        buffer.writeBoolean(!packet.areAchievementsEnabled());
        VarInts.writeInt(buffer, packet.getWorldTime());
        VarInts.writeInt(buffer, packet.getServerOrigin().ordinal());
        buffer.writeBoolean(packet.areEduFeaturedEnabled());
        helper.writeString((packet.getEduUuid() != null ? packet.getEduUuid() : "").toString(), buffer);
        buffer.writeFloatLE(packet.getRainLevel());
        buffer.writeFloatLE(packet.getLightingLevel());
        buffer.writeBoolean(packet.hasPlatformLockedContent());
        buffer.writeBoolean(packet.isMultiplayer());
        buffer.writeBoolean(packet.broadcastingToLan());
        VarInts.writeInt(buffer, packet.getXboxLiveBroadcastMode());
        VarInts.writeInt(buffer, packet.getPlatformBroadcastMode());
        buffer.writeBoolean(packet.areCommandsEnabled());
        buffer.writeBoolean(packet.areResourcePacksRequired());

        // Game rules
        VarInts.writeUnsignedInt(buffer, packet.getGameRules().size());
        for (GameRule<?> rule : packet.getGameRules()) {
            helper.writeString(rule.getId().getName(), buffer);
            VarInts.writeUnsignedInt(buffer, rule.getType().getId());
            switch (rule.getType()) {
                case BOOLEAN:
                    buffer.writeBoolean(((GameRule<Boolean>)rule).getValue());
                    break;
                case INT:
                    VarInts.writeUnsignedInt(buffer, ((GameRule<Integer>)rule).getValue());
                    break;
                case FLOAT:
                    buffer.writeFloatLE(((GameRule<Float>)rule).getValue());
                    break;
                default:
                    throw new AssertionError("Tried to encode unknown gamerule type: " + rule.getType());
            }
        }

        helper.writeExperiments(packet.getExperiments(), buffer);
        buffer.writeBoolean(packet.isExperimentsPreviouslyEnabled());
        buffer.writeBoolean(packet.isBonusChestEnabled());
        buffer.writeBoolean(packet.isBonusMapEnabled());
        VarInts.writeInt(buffer, packet.getPlayerPermissionLevel().ordinal());
        buffer.writeIntLE(packet.getChunkTickRange());
        buffer.writeBoolean(packet.hasLockedBehaviorPacks());
        buffer.writeBoolean(packet.hasLockedResourcePacks());
        buffer.writeBoolean(packet.isFromLockedWorldTemplate());
        buffer.writeBoolean(packet.useMsaGamerTagsOnly());
        buffer.writeBoolean(packet.isFromWorldTemplate());
        buffer.writeBoolean(packet.isWorldTemplateOptionsLocked());
        buffer.writeBoolean(packet.isSpawningOnlyV1VillagersAllowed());
        helper.writeString(packet.getGameVersion(), buffer);
        buffer.writeIntLE(packet.getLimitedWorldWidth());
        buffer.writeIntLE(packet.getLimitedWorldHeight());
        buffer.writeBoolean(packet.isNetherType());
        buffer.writeBoolean(packet.isExperimentalGameplayForced());
        if (packet.isExperimentalGameplayForced()) {
            buffer.writeBoolean(true);
        }

        helper.writeString(packet.getWorldId(), buffer);
        helper.writeString(packet.getServerName(), buffer);
        helper.writeString(packet.getPremiumWorldTemplateId(), buffer);
        buffer.writeBoolean(packet.isTrial());
        VarInts.writeInt(buffer, packet.getMovementType().ordinal());
        buffer.writeLongLE(packet.getCurrentTick());
        VarInts.writeInt(buffer, packet.getEnchantmentSeed());

        // custom blocks
        VarInts.writeUnsignedInt(buffer, packet.getBlockProperties().size());
        for (BaseBlockType blockType : packet.getBlockProperties()) {
            this.writeBlockProperty(blockType, buffer, helper);
        }

        // Item states
        VarInts.writeUnsignedInt(buffer, packet.getItemStates().size());
        for (ItemState itemState : packet.getItemStates()) {
            helper.writeString(itemState.getItemId(), buffer);
            buffer.writeShortLE(itemState.getId());
            buffer.writeBoolean(itemState.isComponentBased());
        }

        helper.writeString(packet.getMultiplayerId().toString(), buffer);
        buffer.writeBoolean(packet.isServerAuthoritativeInventory());

    }

    protected void writeBlockProperty(BaseBlockType blockType, ByteBuf buffer, BasePacketHelper helper) {
        helper.writeString(blockType.getBlockId(), buffer);

        NBTCompound blockContainer = new NBTCompound();

        NBTCompound components = new NBTCompound();

        blockContainer.put("components", components);
        helper.writeNBTCompound(blockContainer, buffer);
    }

}
