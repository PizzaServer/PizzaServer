package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.data.LevelSound;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LevelSoundEventPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;

public class V419LevelSoundEventPacketHandler extends BaseProtocolPacketHandler<LevelSoundEventPacket> {

    protected final BiMap<LevelSound, Integer> sounds = HashBiMap.create(new HashMap<LevelSound, Integer>() {
        {
            this.put(LevelSound.ITEM_USE_ON, 0);
            this.put(LevelSound.HIT, 1);
            this.put(LevelSound.STEP, 2);
            this.put(LevelSound.FLY, 3);
            this.put(LevelSound.JUMP, 4);
            this.put(LevelSound.BREAK, 5);
            this.put(LevelSound.PLACE, 6);
            this.put(LevelSound.HEAVY_STEP, 7);
            this.put(LevelSound.GALLOP, 8);
            this.put(LevelSound.FALL, 9);
            this.put(LevelSound.AMBIENT, 10);
            this.put(LevelSound.AMBIENT_BABY, 11);
            this.put(LevelSound.AMBIENT_IN_WATER, 12);
            this.put(LevelSound.BREATHE,  13);
            this.put(LevelSound.DEATH,  14);
            this.put(LevelSound.DEATH_IN_WATER,  15);
            this.put(LevelSound.DEATH_TO_ZOMBIE,  16);
            this.put(LevelSound.HURT,  17);
            this.put(LevelSound.HURT_IN_WATER,  18);
            this.put(LevelSound.MAD,  19);
            this.put(LevelSound.BOOST,  20);
            this.put(LevelSound.BOW,  21);
            this.put(LevelSound.SQUISH_BIG,  22);
            this.put(LevelSound.SQUISH_SMALL,  23);
            this.put(LevelSound.FALL_BIG,  24);
            this.put(LevelSound.FALL_SMALL,  25);
            this.put(LevelSound.SPLASH,  26);
            this.put(LevelSound.FIZZ,  27);
            this.put(LevelSound.FLAP,  28);
            this.put(LevelSound.SWIM,  29);
            this.put(LevelSound.DRINK,  30);
            this.put(LevelSound.EAT,  31);
            this.put(LevelSound.TAKEOFF,  32);
            this.put(LevelSound.SHAKE,  33);
            this.put(LevelSound.PLOP,  34);
            this.put(LevelSound.LAND,  35);
            this.put(LevelSound.SADDLE,  36);
            this.put(LevelSound.ARMOR,  37);
            this.put(LevelSound.MOB_ARMOR_STAND_PLACE,  38);
            this.put(LevelSound.ADD_CHEST,  39);
            this.put(LevelSound.THROW,  40);
            this.put(LevelSound.ATTACK,  41);
            this.put(LevelSound.ATTACK_NODAMAGE,  42);
            this.put(LevelSound.ATTACK_STRONG,  43);
            this.put(LevelSound.WARN,  44);
            this.put(LevelSound.SHEAR,  45);
            this.put(LevelSound.MILK,  46);
            this.put(LevelSound.THUNDER,  47);
            this.put(LevelSound.EXPLODE,  48);
            this.put(LevelSound.FIRE,  49);
            this.put(LevelSound.IGNITE,  50);
            this.put(LevelSound.FUSE,  51);
            this.put(LevelSound.STARE,  52);
            this.put(LevelSound.SPAWN,  53);
            this.put(LevelSound.SHOOT,  54);
            this.put(LevelSound.BREAK_BLOCK,  55);
            this.put(LevelSound.LAUNCH,  56);
            this.put(LevelSound.BLAST,  57);
            this.put(LevelSound.LARGE_BLAST,  58);
            this.put(LevelSound.TWINKLE,  59);
            this.put(LevelSound.REMEDY,  60);
            this.put(LevelSound.UNFECT,  61);
            this.put(LevelSound.LEVELUP,  62);
            this.put(LevelSound.BOW_HIT,  63);
            this.put(LevelSound.BULLET_HIT,  64);
            this.put(LevelSound.EXTINGUISH_FIRE,  65);
            this.put(LevelSound.ITEM_FIZZ,  66);
            this.put(LevelSound.CHEST_OPEN,  67);
            this.put(LevelSound.CHEST_CLOSED,  68);
            this.put(LevelSound.SHULKERBOX_OPEN,  69);
            this.put(LevelSound.SHULKERBOX_CLOSED,  70);
            this.put(LevelSound.ENDERCHEST_OPEN,  71);
            this.put(LevelSound.ENDERCHEST_CLOSED,  72);
            this.put(LevelSound.POWER_ON,  73);
            this.put(LevelSound.POWER_OFF,  74);
            this.put(LevelSound.ATTACH,  75);
            this.put(LevelSound.DETACH,  76);
            this.put(LevelSound.DENY,  77);
            this.put(LevelSound.TRIPOD,  78);
            this.put(LevelSound.POP,  79);
            this.put(LevelSound.DROP_SLOT,  80);
            this.put(LevelSound.NOTE,  81);
            this.put(LevelSound.THORNS,  82);
            this.put(LevelSound.PISTON_IN,  83);
            this.put(LevelSound.PISTON_OUT,  84);
            this.put(LevelSound.PORTAL,  85);
            this.put(LevelSound.WATER,  86);
            this.put(LevelSound.LAVA_POP,  87);
            this.put(LevelSound.LAVA,  88);
            this.put(LevelSound.BURP,  89);
            this.put(LevelSound.BUCKET_FILL_WATER,  90);
            this.put(LevelSound.BUCKET_FILL_LAVA,  91);
            this.put(LevelSound.BUCKET_EMPTY_WATER,  92);
            this.put(LevelSound.BUCKET_EMPTY_LAVA,  93);
            this.put(LevelSound.ARMOR_EQUIP_CHAIN,  94);
            this.put(LevelSound.ARMOR_EQUIP_DIAMOND,  95);
            this.put(LevelSound.ARMOR_EQUIP_GENERIC,  96);
            this.put(LevelSound.ARMOR_EQUIP_GOLD,  97);
            this.put(LevelSound.ARMOR_EQUIP_IRON,  98);
            this.put(LevelSound.ARMOR_EQUIP_LEATHER,  99);
            this.put(LevelSound.ARMOR_EQUIP_ELYTRA, 100);
            this.put(LevelSound.RECORD_13, 101);
            this.put(LevelSound.RECORD_CAT, 102);
            this.put(LevelSound.RECORD_BLOCKS, 103);
            this.put(LevelSound.RECORD_CHIRP, 104);
            this.put(LevelSound.RECORD_FAR, 105);
            this.put(LevelSound.RECORD_MALL, 106);
            this.put(LevelSound.RECORD_MELLOHI, 107);
            this.put(LevelSound.RECORD_STAL, 108);
            this.put(LevelSound.RECORD_STRAD, 109);
            this.put(LevelSound.RECORD_WARD, 110);
            this.put(LevelSound.RECORD_11, 111);
            this.put(LevelSound.RECORD_WAIT, 112);
            this.put(LevelSound.STOP_RECORD, 113);
            this.put(LevelSound.GUARDIAN_FLOP, 114);
            this.put(LevelSound.ELDERGUARDIAN_CURSE, 115);
            this.put(LevelSound.MOB_WARNING, 116);
            this.put(LevelSound.MOB_WARNING_BABY, 117);
            this.put(LevelSound.TELEPORT, 118);
            this.put(LevelSound.SHULKER_OPEN, 119);
            this.put(LevelSound.SHULKER_CLOSE, 120);
            this.put(LevelSound.HAGGLE, 121);
            this.put(LevelSound.HAGGLE_YES, 122);
            this.put(LevelSound.HAGGLE_NO, 123);
            this.put(LevelSound.HAGGLE_IDLE, 124);
            this.put(LevelSound.CHORUSGROW, 125);
            this.put(LevelSound.CHORUSDEATH, 126);
            this.put(LevelSound.GLASS, 127);
            this.put(LevelSound.POTION_BREWED, 128);
            this.put(LevelSound.CAST_SPELL, 129);
            this.put(LevelSound.PREPARE_ATTACK, 130);
            this.put(LevelSound.PREPARE_SUMMON, 131);
            this.put(LevelSound.PREPARE_WOLOLO, 132);
            this.put(LevelSound.FANG, 133);
            this.put(LevelSound.CHARGE, 134);
            this.put(LevelSound.CAMERA_TAKE_PICTURE, 135);
            this.put(LevelSound.LEASHKNOT_PLACE, 136);
            this.put(LevelSound.LEASHKNOT_BREAK, 137);
            this.put(LevelSound.GROWL, 138);
            this.put(LevelSound.WHINE, 139);
            this.put(LevelSound.PANT, 140);
            this.put(LevelSound.PURR, 141);
            this.put(LevelSound.PURREOW, 142);
            this.put(LevelSound.DEATH_MIN_VOLUME, 143);
            this.put(LevelSound.DEATH_MID_VOLUME, 144);
            this.put(LevelSound.IMITATE_BLAZE, 145);
            this.put(LevelSound.IMITATE_CAVE_SPIDER, 146);
            this.put(LevelSound.IMITATE_CREEPER, 147);
            this.put(LevelSound.IMITATE_ELDER_GUARDIAN, 148);
            this.put(LevelSound.IMITATE_ENDER_DRAGON, 149);
            this.put(LevelSound.IMITATE_ENDERMAN, 150);

            this.put(LevelSound.IMITATE_EVOCATION_ILLAGER, 152);
            this.put(LevelSound.IMITATE_GHAST, 153);
            this.put(LevelSound.IMITATE_HUSK, 154);
            this.put(LevelSound.IMITATE_ILLUSION_ILLAGER, 155);
            this.put(LevelSound.IMITATE_MAGMA_CUBE, 156);
            this.put(LevelSound.IMITATE_POLAR_BEAR, 157);
            this.put(LevelSound.IMITATE_SHULKER, 158);
            this.put(LevelSound.IMITATE_SILVERFISH, 159);
            this.put(LevelSound.IMITATE_SKELETON, 160);
            this.put(LevelSound.IMITATE_SLIME, 161);
            this.put(LevelSound.IMITATE_SPIDER, 162);
            this.put(LevelSound.IMITATE_STRAY, 163);
            this.put(LevelSound.IMITATE_VEX, 164);
            this.put(LevelSound.IMITATE_VINDICATION_ILLAGER, 165);
            this.put(LevelSound.IMITATE_WITCH, 166);
            this.put(LevelSound.IMITATE_WITHER, 167);
            this.put(LevelSound.IMITATE_WITHER_SKELETON, 168);
            this.put(LevelSound.IMITATE_WOLF, 169);
            this.put(LevelSound.IMITATE_ZOMBIE, 170);
            this.put(LevelSound.IMITATE_ZOMBIE_PIGMAN, 171);
            this.put(LevelSound.IMITATE_ZOMBIE_VILLAGER, 172);
            this.put(LevelSound.BLOCK_END_PORTAL_FRAME_FILL, 173);
            this.put(LevelSound.BLOCK_END_PORTAL_SPAWN, 174);
            this.put(LevelSound.RANDOM_ANVIL_USE, 175);
            this.put(LevelSound.BOTTLE_DRAGONBREATH, 176);
            this.put(LevelSound.PORTAL_TRAVEL, 177);
            this.put(LevelSound.ITEM_TRIDENT_HIT, 178);
            this.put(LevelSound.ITEM_TRIDENT_RETURN, 179);
            this.put(LevelSound.ITEM_TRIDENT_RIPTIDE_1, 180);
            this.put(LevelSound.ITEM_TRIDENT_RIPTIDE_2, 181);
            this.put(LevelSound.ITEM_TRIDENT_RIPTIDE_3, 182);
            this.put(LevelSound.ITEM_TRIDENT_THROW, 183);
            this.put(LevelSound.ITEM_TRIDENT_THUNDER, 184);
            this.put(LevelSound.ITEM_TRIDENT_HIT_GROUND, 185);
            this.put(LevelSound.DEFAULT, 186);

            this.put(LevelSound.ELEMCONSTRUCT_OPEN, 188);
            this.put(LevelSound.ICEBOMB_HIT, 189);
            this.put(LevelSound.BALLOONPOP, 190);
            this.put(LevelSound.LT_REACTION_ICEBOMB, 191);
            this.put(LevelSound.LT_REACTION_BLEACH, 192);
            this.put(LevelSound.LT_REACTION_EPASTE, 193);
            this.put(LevelSound.LT_REACTION_EPASTE2, 194);

            this.put(LevelSound.LT_REACTION_FERTILIZER, 199);
            this.put(LevelSound.LT_REACTION_FIREBALL, 200);
            this.put(LevelSound.LT_REACTION_MGSALT, 201);
            this.put(LevelSound.LT_REACTION_MISCFIRE, 202);
            this.put(LevelSound.LT_REACTION_FIRE, 203);
            this.put(LevelSound.LT_REACTION_MISCEXPLOSION, 204);
            this.put(LevelSound.LT_REACTION_MISCMYSTICAL, 205);
            this.put(LevelSound.LT_REACTION_MISCMYSTICAL2, 206);
            this.put(LevelSound.LT_REACTION_PRODUCT, 207);
            this.put(LevelSound.SPARKLER_USE, 208);
            this.put(LevelSound.GLOWSTICK_USE, 209);
            this.put(LevelSound.SPARKLER_ACTIVE, 210);
            this.put(LevelSound.CONVERT_TO_DROWNED, 211);
            this.put(LevelSound.BUCKET_FILL_FISH, 212);
            this.put(LevelSound.BUCKET_EMPTY_FISH, 213);
            this.put(LevelSound.BUBBLE_UP, 214);
            this.put(LevelSound.BUBBLE_DOWN, 215);
            this.put(LevelSound.BUBBLE_POP, 216);
            this.put(LevelSound.BUBBLE_UPINSIDE, 217);
            this.put(LevelSound.BUBBLE_DOWNINSIDE, 218);
            this.put(LevelSound.HURT_BABY, 219);
            this.put(LevelSound.DEATH_BABY, 220);
            this.put(LevelSound.STEP_BABY, 221);
            this.put(LevelSound.BORN, 223);
            this.put(LevelSound.BLOCK_TURTLE_EGG_BREAK, 224);
            this.put(LevelSound.BLOCK_TURTLE_EGG_CRACK, 225);
            this.put(LevelSound.BLOCK_TURTLE_EGG_HATCH, 226);
            this.put(LevelSound.BLOCK_TURTLE_EGG_ATTACK, 228);
            this.put(LevelSound.BEACON_ACTIVATE, 229);
            this.put(LevelSound.BEACON_AMBIENT, 230);
            this.put(LevelSound.BEACON_DEACTIVATE, 231);
            this.put(LevelSound.BEACON_POWER, 232);
            this.put(LevelSound.CONDUIT_ACTIVATE, 233);
            this.put(LevelSound.CONDUIT_AMBIENT, 234);
            this.put(LevelSound.CONDUIT_ATTACK, 235);
            this.put(LevelSound.CONDUIT_DEACTIVATE, 236);
            this.put(LevelSound.CONDUIT_SHORT, 237);
            this.put(LevelSound.SWOOP, 238);
            this.put(LevelSound.BLOCK_BAMBOO_SAPLING_PLACE, 239);
            this.put(LevelSound.PRESNEEZE, 240);
            this.put(LevelSound.SNEEZE, 241);
            this.put(LevelSound.AMBIENT_TAME, 242);
            this.put(LevelSound.SCARED, 243);
            this.put(LevelSound.BLOCK_SCAFFOLDING_CLIMB, 244);
            this.put(LevelSound.CROSSBOW_LOADING_START, 245);
            this.put(LevelSound.CROSSBOW_LOADING_MIDDLE, 246);
            this.put(LevelSound.CROSSBOW_LOADING_END, 247);
            this.put(LevelSound.CROSSBOW_SHOOT, 248);
            this.put(LevelSound.CROSSBOW_QUICK_CHARGE_START, 249);
            this.put(LevelSound.CROSSBOW_QUICK_CHARGE_MIDDLE, 250);
            this.put(LevelSound.CROSSBOW_QUICK_CHARGE_END, 251);
            this.put(LevelSound.AMBIENT_AGGRESSIVE, 252);
            this.put(LevelSound.AMBIENT_WORRIED, 253);
            this.put(LevelSound.CANT_BREED, 254);
            this.put(LevelSound.UNDEFINED, 255);
        }
    });

    @Override
    public LevelSoundEventPacket decode(ByteBuf buffer, BasePacketHelper helper) {
        LevelSoundEventPacket packet = new LevelSoundEventPacket();
        packet.setSound(sounds.inverse().get(VarInts.readUnsignedInt(buffer)));
        packet.setVector3(helper.readVector3(buffer));
        packet.setBlockID(VarInts.readInt(buffer));
        packet.setEntityType(helper.readString(buffer));
        packet.setBaby(buffer.readBoolean());
        packet.setGlobal(buffer.readBoolean());
        return packet;
    }

    @Override
    public void encode(LevelSoundEventPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeUnsignedInt(buffer, sounds.get(packet.getSound()));
        helper.writeVector3(buffer, packet.getVector3());
        VarInts.writeInt(buffer, packet.getBlockID());
        helper.writeString(packet.getEntityType(), buffer);
        buffer.writeBoolean(packet.isBaby());
        buffer.writeBoolean(packet.isGlobal());
    }
}
