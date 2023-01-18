package io.github.pizzaserver.api.keychain;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.commons.data.DataKey;

/**
 * A collection of built-in data keys, useful for accessing entity data.
 *
 * <p>
 * Action Key IDs tend to follow the names of the official query functions. If a value does not have a query function,
 * it should be replaced with <i>pizza.[appropriate name].</i> The official query functions
 * <a href="https://learn.microsoft.com/en-us/minecraft/creator/reference/content/molangreference/examples/molangconcepts/queryfunctions">
 * can be found here!
 * </a>
 * </p>
 */
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
    public static final DataKey<Vector3f> POSITION = DataKey.of("query.position", Vector3f.class);
    public static final DataKey<Float> ROTATION_PITCH = DataKey.of("query.body_x_rotation", Float.TYPE);
    public static final DataKey<Float> ROTATION_YAW = DataKey.of("query.body_y_rotation", Float.TYPE);
    public static final DataKey<Float> ROTATION_HEAD_PITCH = DataKey.of("query.head_x_rotation", Float.TYPE);
    public static final DataKey<Float> ROTATION_HEAD_YAW = DataKey.of("query.head_y_rotation", Float.TYPE);
    public static final DataKey<Float> ROTATION_HEAD_ROLL = DataKey.of("query.head_roll_angle", Float.TYPE);
    public static final DataKey<World> WORLD = DataKey.of("pizza.entity_world", World.class);


    // -- Time
    public static final DataKey<Integer> BREATHING_TICKS_REMAINING = DataKey.of("entity_breathing_ticks", Integer.TYPE); // has a flag?
    public static final DataKey<Integer> FIRE_TICKS_REMAINING = DataKey.of("query.on_fire_time", Integer.TYPE);
    public static final DataKey<Integer> INVULNERABILITY_TICKS = DataKey.of("query.invulnerable_ticks", Integer.TYPE);


    // -- Locomotion
    public static final DataKey<Float> MOVEMENT_SPEED = DataKey.of("pizza.base_move_speed", Float.TYPE);


    // -- Living
    public static final DataKey<Float> HEALTH = DataKey.of("query.health", Float.TYPE);
    public static final DataKey<Float> ABSORPTION = DataKey.of("pizza.absorption", Float.TYPE);
    public static final DataKey<Boolean> IS_VULNERABLE = DataKey.of("pizza.is_vulnerable", Boolean.TYPE);


    // -- Humanoid
    public static final DataKey<Float> FOOD = DataKey.of("pizza.food", Float.TYPE);
    public static final DataKey<Float> SATURATION = DataKey.of("pizza.saturation", Float.TYPE);
    public static final DataKey<Float> XP = DataKey.of("pizza.entity_xp", Float.TYPE);
    public static final DataKey<Float> XP_LEVELS = DataKey.of("pizza.entity_xp_levels", Float.TYPE);


    // -- Misc
    public static final DataKey<BossBar> BOSS_BAR = DataKey.of("pizza.boss_bar", BossBar.class);

}
