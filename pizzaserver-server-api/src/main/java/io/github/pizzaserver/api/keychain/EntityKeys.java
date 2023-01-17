package io.github.pizzaserver.api.keychain;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.commons.data.DataKey;

/** A collection of built-in data keys, useful for accessing entity data. */
public class EntityKeys {

    //TODO: Add a method of filtering inputs for set (override set in entity impl of data store? -
    // will need to hide the getContainer() stuff)

    //TODO: EntityComponents -
    // split into two/three chunks: EntityCompDefinition + EntityCompImpl/EntityCompAPI
    // The definition should store the details of the component for the entity. This can be provided
    // as an instance to the EntityCompImpl.
    // Maybe EntityCompImpl<T extends EntityCompDefinition> ??

    //TODO: Data integrity
    // Potentially solved the risk of data being updated at unexpected points by hiding the getContainer
    // for stores by default. Add the listeners/callback handlers back to ImplEntity.

    // -- Location
    public static final DataKey<Vector3f> POSITION = DataKey.of("entity_position", Vector3f.class);
    public static final DataKey<Float> ROTATION_PITCH = DataKey.of("entity_rotation_pitch", Float.TYPE);
    public static final DataKey<Float> ROTATION_YAW = DataKey.of("entity_rotation_yaw", Float.TYPE);
    public static final DataKey<Float> ROTATION_HEAD_YAW = DataKey.of("entity_rotation_yaw_head", Float.TYPE);
    public static final DataKey<World> WORLD = DataKey.of("entity_world", World.class);


    // -- Time
    public static final DataKey<Integer> BREATHING_TICKS_REMAINING = DataKey.of("entity_breathing_ticks", Integer.TYPE); // has a flag?
    public static final DataKey<Integer> FIRE_TICKS_REMAINING = DataKey.of("entity_fire_ticks", Integer.TYPE);
    public static final DataKey<Integer> INVULNERABILITY_TICKS = DataKey.of("entity_damage_ticks", Integer.TYPE);


    // -- Locomotion
    public static final DataKey<Float> MOVEMENT_SPEED = DataKey.of("entity_move_speed", Float.TYPE);


    // -- Living
    public static final DataKey<Float> HEALTH = DataKey.of("entity_health", Float.TYPE);
    public static final DataKey<Float> ABSORPTION = DataKey.of("entity_absorption", Float.TYPE);
    public static final DataKey<Boolean> IS_VULNERABLE = DataKey.of("entity_vulnerable", Boolean.TYPE);


    // -- Humanoid
    public static final DataKey<Float> FOOD = DataKey.of("entity_food", Float.TYPE);
    public static final DataKey<Float> SATURATION = DataKey.of("entity_saturation", Float.TYPE);
    public static final DataKey<Float> XP = DataKey.of("entity_xp", Float.TYPE);
    public static final DataKey<Float> XP_LEVELS = DataKey.of("entity_xp_levels", Float.TYPE);


    // -- Misc
    public static final DataKey<BossBar> BOSS_BAR = DataKey.of("boss_bar", BossBar.class);

}
