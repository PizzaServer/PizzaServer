package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaProperty;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyType;
import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.api.player.skin.SkinAnimation;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPiece;
import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPieceTint;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.item.Item;
import io.github.willqi.pizzaserver.server.item.ItemBlock;
import io.github.willqi.pizzaserver.server.item.ItemID;
import io.github.willqi.pizzaserver.server.network.protocol.data.EntityLink;
import io.github.willqi.pizzaserver.server.network.protocol.data.Experiment;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class V419PacketHelper extends BasePacketHelper {

    public final static BasePacketHelper INSTANCE = new V419PacketHelper();


    public V419PacketHelper() {
        this.supportedExperiments.add(Experiment.DATA_DRIVEN_ITEMS);

        this.supportedEntityLinkTypes.add(EntityLink.Type.REMOVE);
        this.supportedEntityLinkTypes.add(EntityLink.Type.RIDER);
        this.supportedEntityLinkTypes.add(EntityLink.Type.PASSENGER);

        this.supportedEntityFlagTypes.put(EntityMetaFlagCategory.DATA_FLAG, 0);
        this.supportedEntityFlagTypes.put(EntityMetaFlagCategory.PLAYER_FLAG, 26);

        this.supportedEntityFlags.put(EntityMetaFlag.IS_ON_FIRE, 0);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SNEAKING, 1);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_RIDING, 2);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SPRINTING, 3);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_USING_ITEM, 4);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_INVISIBLE, 5);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_TEMPTED, 6);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_IN_LOVE, 7);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SADDLED, 8);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_POWERED, 9);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_IGNITED, 10);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_BABY, 11);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_CONVERTING, 12);
        this.supportedEntityFlags.put(EntityMetaFlag.CRITICAL, 13);
        this.supportedEntityFlags.put(EntityMetaFlag.CAN_SHOW_NAMETAG, 14);
        this.supportedEntityFlags.put(EntityMetaFlag.ALWAYS_SHOW_NAMETAG, 15);
        this.supportedEntityFlags.put(EntityMetaFlag.HAS_NO_AI, 16);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SILENT, 17);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_WALL_CLIMBING, 18);
        this.supportedEntityFlags.put(EntityMetaFlag.CAN_WALL_CLIMB, 19);
        this.supportedEntityFlags.put(EntityMetaFlag.CAN_SWIM, 20);
        this.supportedEntityFlags.put(EntityMetaFlag.CAN_FLY, 21);
        this.supportedEntityFlags.put(EntityMetaFlag.CAN_WALK, 22);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_RESTING, 23);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SITTING, 24);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_ANGRY, 25);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_INTERESTED, 26);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_CHARGED, 27);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_TAMED, 28);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_ORPHANED, 29);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_LEASHED, 30);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SHEARED, 31);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_GLIDING, 32);
        this.supportedEntityFlags.put(EntityMetaFlag.ELDER, 33);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_MOVING, 34);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_BREATHING, 35);
        this.supportedEntityFlags.put(EntityMetaFlag.CHESTED, 36);
        this.supportedEntityFlags.put(EntityMetaFlag.STACKABLE, 37);
        this.supportedEntityFlags.put(EntityMetaFlag.SHOW_BASE, 38);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_STANDING, 39);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SHAKING, 40);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_IDLING, 41);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_CASTING, 42);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_CHARGING, 43);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_WASD_CONTROLLED, 44);
        this.supportedEntityFlags.put(EntityMetaFlag.CAN_POWER_JUMP, 45);
        this.supportedEntityFlags.put(EntityMetaFlag.LINGER, 46);
        this.supportedEntityFlags.put(EntityMetaFlag.HAS_COLLISION, 47);
        this.supportedEntityFlags.put(EntityMetaFlag.HAS_GRAVITY, 48);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_FIRE_IMMUNE, 49);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_DANCING, 50);
        this.supportedEntityFlags.put(EntityMetaFlag.ENCHANTED, 51);
        this.supportedEntityFlags.put(EntityMetaFlag.SHOWING_TRIDENT_ROPE, 52);
        this.supportedEntityFlags.put(EntityMetaFlag.HAS_PRIVATE_CONTAINER, 53);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_TRANSFORMING, 54);
        this.supportedEntityFlags.put(EntityMetaFlag.SPIN_ATTACK, 55);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SWIMMING, 56);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_BRIBED, 57);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_PREGNANT, 58);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_LAYING_EGG, 59);
        this.supportedEntityFlags.put(EntityMetaFlag.RIDER_CAN_PICK, 60);
        this.supportedEntityFlags.put(EntityMetaFlag.TRANSITION_SITTING, 61);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_EATING, 62);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_LAYING_DOWN, 63);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SNEEZING, 64);
        this.supportedEntityFlags.put(EntityMetaFlag.TRUSTING, 65);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_ROLLING, 66);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SCARED, 67);
        this.supportedEntityFlags.put(EntityMetaFlag.IN_SCAFFOLDING, 68);
        this.supportedEntityFlags.put(EntityMetaFlag.OVER_SCAFFOLDING, 69);
        this.supportedEntityFlags.put(EntityMetaFlag.FALLING_THROUGH_SCAFFOLDING, 70);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_BLOCKING, 71);
        this.supportedEntityFlags.put(EntityMetaFlag.TRANSITION_BLOCKING, 72);
        this.supportedEntityFlags.put(EntityMetaFlag.BLOCKED_USING_SHIELD, 73);
        this.supportedEntityFlags.put(EntityMetaFlag.BLOCKED_USING_DAMAGED_SHIELD, 74);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_SLEEPING, 75);
        this.supportedEntityFlags.put(EntityMetaFlag.WANTS_TO_AWAKE, 76);
        this.supportedEntityFlags.put(EntityMetaFlag.HAS_TRADE_INTEREST, 77);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_DOOR_BREAKER, 78);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_BREAKING_OBSTRUCTION, 79);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_DOOR_OPENER, 80);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_ILLAGER_CAPTAIN, 81);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_STUNNED, 82);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_ROARING, 83);
        this.supportedEntityFlags.put(EntityMetaFlag.HAS_DELAYED_ATTACK, 84);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_AVOIDING_MOBS, 85);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_AVOIDING_BLOCK, 86);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_FACING_TARGET_TO_RANGE_ATTACK, 87);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_HIDDEN_WHEN_INVISIBLE, 88);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_IN_UI, 89);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_STALKING, 90);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_EMOTING, 91);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_CELEBRATING, 92);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_ADMIRING, 93);
        this.supportedEntityFlags.put(EntityMetaFlag.IS_CELEBRATING_SPECIAL, 94);


        // Commented lines represent properties with unknown types
        this.supportedEntityProperties.put(EntityMetaPropertyName.HEALTH, 1);
        this.supportedEntityProperties.put(EntityMetaPropertyName.VARIANT, 2);
        this.supportedEntityProperties.put(EntityMetaPropertyName.COLOR, 3);
        this.supportedEntityProperties.put(EntityMetaPropertyName.NAMETAG, 4);
        this.supportedEntityProperties.put(EntityMetaPropertyName.OWNER_EID, 5);
        this.supportedEntityProperties.put(EntityMetaPropertyName.TARGET_EID, 6);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AIR, 7);
        this.supportedEntityProperties.put(EntityMetaPropertyName.POTION_COLOR, 8);
        this.supportedEntityProperties.put(EntityMetaPropertyName.POTION_AMBIENT, 9);
        this.supportedEntityProperties.put(EntityMetaPropertyName.JUMP_DURATION, 10);
        this.supportedEntityProperties.put(EntityMetaPropertyName.HURT_TIME, 11);
        this.supportedEntityProperties.put(EntityMetaPropertyName.HURT_DIRECTION, 12);
        this.supportedEntityProperties.put(EntityMetaPropertyName.PADDLE_TIME_LEFT, 13);
        this.supportedEntityProperties.put(EntityMetaPropertyName.PADDLE_TIME_RIGHT, 14);
        this.supportedEntityProperties.put(EntityMetaPropertyName.EXPERIENCE_VALUE, 15);
        this.supportedEntityProperties.put(EntityMetaPropertyName.MINECART_DISPLAY_BLOCK, 16);
        this.supportedEntityProperties.put(EntityMetaPropertyName.MINECART_DISPLAY_OFFSET, 17);
        this.supportedEntityProperties.put(EntityMetaPropertyName.MINECART_HAS_DISPLAY, 18);
        // swell
        // old swell
        // swell dir
        this.supportedEntityProperties.put(EntityMetaPropertyName.CHARGE_AMOUNT, 22);
        this.supportedEntityProperties.put(EntityMetaPropertyName.ENDERMAN_HELD_ITEM_ID, 23);
        this.supportedEntityProperties.put(EntityMetaPropertyName.ENTITY_AGE, 24);
        // ???
        // player flags (used for flags)
        this.supportedEntityProperties.put(EntityMetaPropertyName.PLAYER_INDEX, 27);
        this.supportedEntityProperties.put(EntityMetaPropertyName.PLAYER_BED_POSITION, 28);
        this.supportedEntityProperties.put(EntityMetaPropertyName.FIREBALL_POWER_X, 29);
        this.supportedEntityProperties.put(EntityMetaPropertyName.FIREBALL_POWER_Y, 30);
        this.supportedEntityProperties.put(EntityMetaPropertyName.FIREBALL_POWER_Z, 31);
        // aux power
        // fish x
        // fish z
        // fish angle
        this.supportedEntityProperties.put(EntityMetaPropertyName.POTION_AUX_VALUE, 36);
        this.supportedEntityProperties.put(EntityMetaPropertyName.LEAD_HOLDER_EID, 37);
        this.supportedEntityProperties.put(EntityMetaPropertyName.SCALE, 38);
        this.supportedEntityProperties.put(EntityMetaPropertyName.INTERACTIVE_TAG, 39);
        this.supportedEntityProperties.put(EntityMetaPropertyName.NPC_SKIN_INDEX, 40);
        // url tag
        this.supportedEntityProperties.put(EntityMetaPropertyName.MAX_AIR_SUPPLY, 42);
        this.supportedEntityProperties.put(EntityMetaPropertyName.MARK_VARIANT, 43);
        this.supportedEntityProperties.put(EntityMetaPropertyName.CONTAINER_TYPE, 44);
        this.supportedEntityProperties.put(EntityMetaPropertyName.CONTAINER_BASE_SIZE, 45);
        this.supportedEntityProperties.put(EntityMetaPropertyName.CONTAINER_EXTRA_SLOTS_PER_STRENGTH, 46);
        this.supportedEntityProperties.put(EntityMetaPropertyName.BLOCK_TARGET, 47);
        this.supportedEntityProperties.put(EntityMetaPropertyName.WITHER_INVULNERABLE_TICKS, 48);
        this.supportedEntityProperties.put(EntityMetaPropertyName.WITHER_TARGET_1, 49);
        this.supportedEntityProperties.put(EntityMetaPropertyName.WITHER_TARGET_2, 50);
        this.supportedEntityProperties.put(EntityMetaPropertyName.WITHER_TARGET_3, 51);
        this.supportedEntityProperties.put(EntityMetaPropertyName.WITHER_AERIAL_ATTACK, 52);
        this.supportedEntityProperties.put(EntityMetaPropertyName.BOUNDING_BOX_WIDTH, 53);
        this.supportedEntityProperties.put(EntityMetaPropertyName.BOUNDING_BOX_HEIGHT, 54);
        this.supportedEntityProperties.put(EntityMetaPropertyName.FUSE_LENGTH, 55);
        this.supportedEntityProperties.put(EntityMetaPropertyName.RIDER_SEAT_POSITION, 56);
        this.supportedEntityProperties.put(EntityMetaPropertyName.RIDER_ROTATION_LOCKED, 57);
        this.supportedEntityProperties.put(EntityMetaPropertyName.RIDER_MAX_ROTATION, 58);
        this.supportedEntityProperties.put(EntityMetaPropertyName.RIDER_MIN_ROTATION, 59);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_RADIUS, 60);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_WAITING, 61);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_PARTICLE_ID, 62);
        this.supportedEntityProperties.put(EntityMetaPropertyName.SHULKER_ATTACH_FACE, 64);
        this.supportedEntityProperties.put(EntityMetaPropertyName.SHULKER_ATTACH_POSITION, 66);
        this.supportedEntityProperties.put(EntityMetaPropertyName.TRADING_TARGET_EID, 67);
        // trading career
        this.supportedEntityProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_ENABLED, 69);
        this.supportedEntityProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_COMMAND, 70);
        this.supportedEntityProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_LAST_OUTPUT, 71);
        this.supportedEntityProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_TRACK_OUTPUT, 72);
        this.supportedEntityProperties.put(EntityMetaPropertyName.CONTROLLING_RIDER_SEAT_NUMBER, 73);
        this.supportedEntityProperties.put(EntityMetaPropertyName.STRENGTH, 74);
        this.supportedEntityProperties.put(EntityMetaPropertyName.MAX_STRENGTH, 75);
        this.supportedEntityProperties.put(EntityMetaPropertyName.EVOKER_SPELL_COLOR, 76);
        this.supportedEntityProperties.put(EntityMetaPropertyName.LIMITED_LIFE, 77);
        this.supportedEntityProperties.put(EntityMetaPropertyName.ARMOR_STAND_POSE_INDEX, 78);
        this.supportedEntityProperties.put(EntityMetaPropertyName.ENDER_CRYSTAL_TIME_OFFSET, 79);
        this.supportedEntityProperties.put(EntityMetaPropertyName.ALWAYS_SHOW_NAMETAG, 80);
        this.supportedEntityProperties.put(EntityMetaPropertyName.COLOR_2, 81);
        // name author
        this.supportedEntityProperties.put(EntityMetaPropertyName.SCORE_TAG, 83);
        this.supportedEntityProperties.put(EntityMetaPropertyName.BALLOON_ATTACHED_ENTITY, 84);
        this.supportedEntityProperties.put(EntityMetaPropertyName.PUFFERFISH_SIZE, 85);
        this.supportedEntityProperties.put(EntityMetaPropertyName.BOAT_BUBBLE_TIME, 86);
        this.supportedEntityProperties.put(EntityMetaPropertyName.PLAYER_AGENT_EID, 87);
        // sitting amount
        // sitting amount previous
        this.supportedEntityProperties.put(EntityMetaPropertyName.EATING_COUNTER, 90);
        // flags extended (probably used for other flags?)
        // laying amount
        // laying amount previous
        this.supportedEntityProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_DURATION, 94);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_SPAWN_TIME, 95);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_CHANGE_RATE, 96);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AREA_EFFECT_CLOUD_CHANGE_ON_PICKUP, 97);
        // pickup count
        // interact text
        this.supportedEntityProperties.put(EntityMetaPropertyName.TRADE_TIER, 100);
        this.supportedEntityProperties.put(EntityMetaPropertyName.MAX_TRADE_TIER, 101);
        this.supportedEntityProperties.put(EntityMetaPropertyName.TRADE_XP, 102);
        this.supportedEntityProperties.put(EntityMetaPropertyName.SKIN_ID, 103);
        // spawning frames
        this.supportedEntityProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_TICK_DELAY, 105);
        this.supportedEntityProperties.put(EntityMetaPropertyName.COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK, 106);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AMBIENT_SOUND_INTERVAL, 107);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AMBIENT_SOUND_INTERVAL_RANGE, 108);
        this.supportedEntityProperties.put(EntityMetaPropertyName.AMBIENT_SOUND_EVENT_NAME, 109);
        this.supportedEntityProperties.put(EntityMetaPropertyName.FALL_DAMAGE_MULTIPLIER, 110);
        // name raw text
        this.supportedEntityProperties.put(EntityMetaPropertyName.CAN_RIDE_TARGET, 112);
        this.supportedEntityProperties.put(EntityMetaPropertyName.LOW_TIER_CURED_TRADE_DISCOUNT, 113);
        this.supportedEntityProperties.put(EntityMetaPropertyName.HIGH_TIER_CURED_TRADE_DISCOUNT, 114);
        this.supportedEntityProperties.put(EntityMetaPropertyName.NEARBY_CURED_TRADE_DISCOUNT, 115);
        this.supportedEntityProperties.put(EntityMetaPropertyName.DISCOUNT_TIME_STAMP, 116);
        this.supportedEntityProperties.put(EntityMetaPropertyName.HITBOX, 117);
        this.supportedEntityProperties.put(EntityMetaPropertyName.IS_BUOYANT, 118);
        this.supportedEntityProperties.put(EntityMetaPropertyName.BUOYANCY_DATA, 119);


        this.supportedEntityPropertyTypes.add(EntityMetaPropertyType.BYTE);
        this.supportedEntityPropertyTypes.add(EntityMetaPropertyType.SHORT);
        this.supportedEntityPropertyTypes.add(EntityMetaPropertyType.INTEGER);
        this.supportedEntityPropertyTypes.add(EntityMetaPropertyType.FLOAT);
        this.supportedEntityPropertyTypes.add(EntityMetaPropertyType.STRING);
        this.supportedEntityPropertyTypes.add(EntityMetaPropertyType.NBT);
        this.supportedEntityPropertyTypes.add(EntityMetaPropertyType.VECTOR3I);
        this.supportedEntityPropertyTypes.add(EntityMetaPropertyType.LONG);
        this.supportedEntityPropertyTypes.add(EntityMetaPropertyType.VECTOR3);

    }

    @Override
    public void writeItem(Item item, ByteBuf buffer) {

        // network id
        VarInts.writeInt(buffer, item.getId().ordinal());   // TODO: This probably isn't the proper item id. Find out how to get it

        // item damage + count
        int itemData = ((item.getDamage() << 8) | item.getCount());   // TODO: or maybe it's this id. The above id is just the network id. Does it affect anything?
        VarInts.writeInt(buffer, itemData);

        // Write NBT tag
        if (item.getTag() != null) {
            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
            try {
                NBTOutputStream stream = new NBTOutputStream(new VarIntDataOutputStream(resultStream));
                stream.writeCompound(item.getTag());
            } catch (IOException exception) {
                throw new RuntimeException("Unable to write NBT tag", exception);
            }

            buffer.writeShortLE(resultStream.toByteArray().length);
            buffer.writeBytes(resultStream.toByteArray());
        } else {
            buffer.writeShortLE(0);
        }

        // Blocks this item can be placed on
        if (item instanceof ItemBlock) {
            ItemBlock blockItem = (ItemBlock)item;
            VarInts.writeInt(buffer, blockItem.getBlocksCanBePlacedOn().size());
            for (ItemID itemId : blockItem.getBlocksCanBePlacedOn()) {
                this.writeString(itemId.getNameId(), buffer);
            }
        } else {
            VarInts.writeInt(buffer, 0);
        }

        // Blocks this item can break
        VarInts.writeInt(buffer, item.getBlocksCanBreak().size());
        for (ItemID itemId : item.getBlocksCanBreak()) {
            this.writeString(itemId.getNameId(), buffer);
        }

    }

    @Override
    public void writeSkin(ByteBuf buffer, Skin skin) {
        this.writeString(skin.getSkinId(), buffer);
        this.writeString(skin.getSkinResourcePatch(), buffer);
        buffer.writeIntLE(skin.getSkinWidth());
        buffer.writeIntLE(skin.getSkinHeight());
        this.writeByteArray(skin.getSkinData(), buffer);

        buffer.writeIntLE(skin.getAnimations().size());
        for (SkinAnimation animation : skin.getAnimations()) {
            buffer.writeIntLE(animation.getSkinWidth());
            buffer.writeIntLE(animation.getSkinHeight());
            this.writeByteArray(animation.getSkinData(), buffer);
            buffer.writeIntLE(animation.getType());
            buffer.writeFloatLE(animation.getFrame());
            buffer.writeIntLE(animation.getExpressionType());
        }

        buffer.writeIntLE(skin.getCapeWidth());
        buffer.writeIntLE(skin.getCapeHeight());
        this.writeByteArray(skin.getCapeData(), buffer);

        this.writeString(skin.getGeometryData(), buffer);
        this.writeString(skin.getAnimationData(), buffer);
        buffer.writeBoolean(skin.isPremium());
        buffer.writeBoolean(skin.isPersona());
        buffer.writeBoolean(skin.isCapeOnClassic());
        this.writeString(skin.getCapeId(), buffer);
        this.writeString(skin.getFullSkinId(), buffer);
        this.writeString(skin.getArmSize(), buffer);
        this.writeString(skin.getSkinColour(), buffer);

        buffer.writeIntLE(skin.getPieces().size());
        for (SkinPersonaPiece personaPiece : skin.getPieces()) {
            this.writeString(personaPiece.getId(), buffer);
            this.writeString(personaPiece.getType(), buffer);
            this.writeString(personaPiece.getPackId().toString(), buffer);
            buffer.writeBoolean(personaPiece.isDefault());
            this.writeString(personaPiece.getProductId() != null ? personaPiece.getProductId().toString() : "", buffer);
        }

        buffer.writeIntLE(skin.getTints().size());
        for (SkinPersonaPieceTint personaPieceTint : skin.getTints()) {
            this.writeString(personaPieceTint.getId(), buffer);

            buffer.writeIntLE(personaPieceTint.getColors().size());
            for (String colour : personaPieceTint.getColors()) {
                this.writeString(colour, buffer);
            }
        }
    }

    @Override
    public Skin readSkin(ByteBuf buffer) {
        Skin.Builder skinBuilder = new Skin.Builder()
                .setSkinId(this.readString(buffer))
                .setPlayFabId("")
                .setSkinResourcePatch(this.readString(buffer))
                .setSkinWidth(buffer.readIntLE())
                .setSkinHeight(buffer.readIntLE())
                .setSkinData(this.readByteArray(buffer));

        int animationsCount = buffer.readIntLE();
        List<SkinAnimation> animations = new ArrayList<>(animationsCount);
        for (int i = 0; i < animationsCount; i++) {
            SkinAnimation skinAnimation = new SkinAnimation.Builder()
                    .setSkinWidth(buffer.readIntLE())
                    .setSkinHeight(buffer.readIntLE())
                    .setSkinData(this.readByteArray(buffer))
                    .setType(buffer.readIntLE())
                    .setFrame((int)buffer.readFloatLE())
                    .setExpressionType(buffer.readIntLE())
                    .build();
            animations.add(skinAnimation);
        }
        skinBuilder.setAnimations(animations);

        skinBuilder.setCapeWidth(buffer.readIntLE())
                .setCapeHeight(buffer.readIntLE())
                .setCapeData(this.readByteArray(buffer))
                .setGeometryData(this.readString(buffer))
                .setAnimationData(this.readString(buffer))
                .setPremium(buffer.readBoolean())
                .setPersona(buffer.readBoolean())
                .setCapeOnClassic(buffer.readBoolean())
                .setCapeId(this.readString(buffer))
                .setFullSkinId(this.readString(buffer))
                .setArmSize(this.readString(buffer))
                .setSkinColour(this.readString(buffer));

        int piecesCount = buffer.readIntLE();
        List<SkinPersonaPiece> pieces = new ArrayList<>(piecesCount);
        for (int i = 0; i < piecesCount; i++) {
            SkinPersonaPiece.Builder pieceBuilder = new SkinPersonaPiece.Builder()
                    .setId(this.readString(buffer))
                    .setType(this.readString(buffer))
                    .setPackId(UUID.fromString(this.readString(buffer)))
                    .setDefault(buffer.readBoolean());
            String uuidStr = this.readString(buffer);
            pieceBuilder.setProductId(uuidStr.length() == 0 ? null : UUID.fromString(uuidStr));

            pieces.add(pieceBuilder.build());
        }
        skinBuilder.setPieces(pieces);

        int tintCount = buffer.readIntLE();
        List<SkinPersonaPieceTint> tints = new ArrayList<>(tintCount);
        for (int i = 0; i < tintCount; i++) {
            String id = this.readString(buffer);

            int colourCount = buffer.readIntLE();
            List<String> colours = new ArrayList<>(colourCount);
            for (int j = 0; j < colourCount; j++) {
                colours.add(this.readString(buffer));
            }

            SkinPersonaPieceTint tint = new SkinPersonaPieceTint(id, colours);
            tints.add(tint);
        }

        return skinBuilder
                .setTints(tints)
                .build();
    }

    @Override
    public void writeEntityMetadata(EntityMetaData metaData, ByteBuf buffer) {
        // Filter for the flags we support
        Map<EntityMetaFlagCategory, Set<EntityMetaFlag>> flags = metaData.getFlags();
        for (EntityMetaFlagCategory flagType : flags.keySet()) {
            Set<EntityMetaFlag> supportedFlags = flags.get(flagType).stream()
                    .filter(this.supportedEntityFlags::containsKey)
                    .collect(Collectors.toSet());

            flags.put(flagType, supportedFlags);
        }

        // Filter for the properties we support
        Map<EntityMetaPropertyName, EntityMetaProperty<?>> properties = new HashMap<>(metaData.getProperties());
        properties.keySet().removeIf(propertyName -> !this.supportedEntityProperties.containsKey(propertyName));

        // Serialize all entries
        int totalEntries = flags.keySet().size() + properties.size();
        VarInts.writeUnsignedInt(buffer, totalEntries);

        for (EntityMetaFlagCategory flagType : flags.keySet()) {
            VarInts.writeUnsignedInt(buffer, this.supportedEntityFlagTypes.get(flagType));
            if (flagType == EntityMetaFlagCategory.PLAYER_FLAG) {
                byte flagValue = 0;
                for (EntityMetaFlag flag : flags.get(flagType)) {
                    flagValue ^= 1 << this.supportedEntityFlags.get(flag);
                }
                VarInts.writeUnsignedInt(buffer, EntityMetaPropertyType.BYTE.ordinal());
                buffer.writeByte(flagValue);
            } else {
                long flagValue = 0;
                for (EntityMetaFlag flag : flags.get(flagType)) {
                    flagValue ^= 1L << this.supportedEntityFlags.get(flag);
                }
                VarInts.writeUnsignedInt(buffer, EntityMetaPropertyType.LONG.ordinal());
                VarInts.writeLong(buffer, flagValue);
            }
        }

        for (EntityMetaPropertyName propertyName : properties.keySet()) {
            io.github.willqi.pizzaserver.format.mcworld.utils.VarInts.writeUnsignedInt(buffer, this.supportedEntityProperties.get(propertyName));
            io.github.willqi.pizzaserver.format.mcworld.utils.VarInts.writeUnsignedInt(buffer, propertyName.getType().ordinal());
            switch (propertyName.getType()) {
                case BYTE:
                    buffer.writeByte((Byte)properties.get(propertyName).getValue());
                    break;
                case SHORT:
                    buffer.writeShortLE((Short)properties.get(propertyName).getValue());
                    break;
                case INTEGER:
                    VarInts.writeInt(buffer, (Integer)properties.get(propertyName).getValue());
                    break;
                case FLOAT:
                    buffer.writeFloatLE((Float)properties.get(propertyName).getValue());
                    break;
                case LONG:
                    VarInts.writeLong(buffer, (Long)properties.get(propertyName).getValue());
                    break;
                case STRING:
                    this.writeString((String)properties.get(propertyName).getValue(), buffer);
                    break;
                case NBT:
                    this.writeNBTCompound((NBTCompound)properties.get(propertyName).getValue(), buffer);
                    break;
                case VECTOR3I:
                    Vector3i vector3i = (Vector3i)properties.get(propertyName).getValue();
                    this.writeBlockVector(buffer, vector3i);
                    break;
                case VECTOR3:
                    Vector3 vector3 = (Vector3)properties.get(propertyName).getValue();
                    this.writeVector3(buffer, vector3);
                    break;
                default:
                    throw new UnsupportedOperationException("Missing implementation when encoding entity meta type " + propertyName.getType());
            }
        }
    }

    @Override
    public void writeEntityLink(EntityLink entityLink, ByteBuf buffer) {
        VarInts.writeLong(buffer, entityLink.getRiderUniqueEntityId());
        VarInts.writeLong(buffer, entityLink.getTransportationUniqueEntityId());
        if (!this.supportedEntityLinkTypes.contains(entityLink.getType())) {
            throw new UnsupportedOperationException("This version does not support entity link type: " + entityLink.getType());
        }
        buffer.writeByte(entityLink.getType().ordinal());
        buffer.writeBoolean(entityLink.isImmediate());
        buffer.writeBoolean(entityLink.isRiderInitiated());
    }

}
