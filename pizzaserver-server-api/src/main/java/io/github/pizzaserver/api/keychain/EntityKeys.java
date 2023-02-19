package io.github.pizzaserver.api.keychain;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.commons.data.key.DataKey;

/**
 * A collection of built-in data keys, useful for accessing entity data.
 *
 */
public class EntityKeys {

    //TODO: EntityComponents -
    // split into two/three chunks: EntityCompDefinition + EntityCompImpl/EntityCompAPI
    // The definition should store the details of the component for the entity. This can be provided
    // as an instance to the EntityCompImpl.
    // Maybe EntityCompImpl<T extends EntityCompDefinition> ??

    //TODO: Add a way to map attributes & flags to properties. See EntityFlag -
    // Some properties would be derivable from existing data (Flag->ON_FIRE) from (EntityKeys->FIRE_TICKS_REMAINING)
    // Not every flag should be writable. Stuff like Flag->IS_IN_UI is read-only.
    // Some stuff can just be written an mirrored, however.
    //TODO: See EntityData as well for more values.

    // While the identifier names aren't critical, they tend to follow a convention that aligns them with
    // entity attributes/data --> minecraft:property_here
    // see PizzaServer-API :: AttributeType.java
    //
    // If a key maps to something that is really only handled by the server and won't be shared with the client,
    // the prefix of "internal" is adopted --> internal:property_here

    // -- Location
    public static final DataKey<Vector3f> POSITION = DataKey.of("minecraft:position", Vector3f.class);
    public static final DataKey<Float> ROTATION_PITCH = DataKey.of("minecraft:body_x_rotation", Float.TYPE);
    public static final DataKey<Float> ROTATION_YAW = DataKey.of("minecraft:body_y_rotation", Float.TYPE);
    public static final DataKey<Float> ROTATION_HEAD_PITCH = DataKey.of("minecraft:head_x_rotation", Float.TYPE);
    public static final DataKey<Float> ROTATION_HEAD_YAW = DataKey.of("minecraft:head_y_rotation", Float.TYPE);
    public static final DataKey<Float> ROTATION_HEAD_ROLL = DataKey.of("minecraft:head_roll_angle", Float.TYPE);
    public static final DataKey<World> WORLD = DataKey.of("internal:entity_world", World.class);


    // -- Time
    public static final DataKey<Integer> BREATHING_TICKS_REMAINING = DataKey.of("minecraft:breathing_ticks", Integer.TYPE); // has a flag?
    public static final DataKey<Integer> FIRE_TICKS_REMAINING = DataKey.of("minecraft:on_fire_time", Integer.TYPE);
    public static final DataKey<Integer> INVULNERABILITY_TICKS = DataKey.of("minecraft:invulnerable_ticks", Integer.TYPE);


    // -- Locomotion
    public static final DataKey<Float> MOVEMENT_SPEED = DataKey.of("minecraft:movement", Float.TYPE); // *Attrib


    // -- Living
    public static final DataKey<Float> HEALTH = DataKey.of("minecraft:health", Float.TYPE); // *Attrib
    public static final DataKey<Float> MAX_HEALTH = HEALTH.suffix("max");
    public static final DataKey<Float> KILL_THRESHOLD = HEALTH.suffix("min");

    public static final DataKey<Float> ABSORPTION = DataKey.of("minecraft:absorption", Float.TYPE); // *Attrib
    public static final DataKey<Float> MAX_ABSORPTION = HEALTH.suffix("min");

    public static final DataKey<Boolean> IS_VULNERABLE = DataKey.of("minecraft:is_vulnerable", Boolean.TYPE);


    // -- Humanoid
    public static final DataKey<Float> FOOD = DataKey.of("minecraft:player.hunger", Float.TYPE); // *Attrib
    public static final DataKey<Float> SATURATION = DataKey.of("minecraft:player.saturation", Float.TYPE); // *Attrib
    public static final DataKey<Float> PLAYER_XP = DataKey.of("minecraft:player.experience", Float.TYPE); // *Attrib
    public static final DataKey<Integer> PLAYER_XP_LEVELS = DataKey.of("minecraft:player.level", Integer.TYPE); // *Attrib


    // -- Misc
    public static final DataKey<BossBar> BOSS_BAR = DataKey.of("internal:boss_bar", BossBar.class);

}
