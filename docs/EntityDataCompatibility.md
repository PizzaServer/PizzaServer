# Entities & Data 2.0

A lot of properties either require implementation or a rework from their previous
state for PR #75 - This document states which properties/flags have been 
implemented and what their equivalents in the current Cloudburst Protocol are.

If PizzaServer adopts its own protocol project, these mappings should ideally
be dropped in favour of a simple registration system for keys and data. Otherwise,
this implementation only applies a slight bit of overhead in return for some
development simplicity.

## Entity Data

See `com.nukkitx.protocol.bedrock.data.entity.EntityData` @ NukkitX/Cloudburst Protocol

| Cloudburst Entry                      | EntityKeys Impl           | Comments                                                          |
|---------------------------------------|---------------------------|-------------------------------------------------------------------|
| FLAGS                                 | N/A (ignore)              | Special Case Handler                                              |
| FLAGS2                                | N/A (ignore)              | Special Case Handler                                              |
| HEALTH                                | N/A (not paired)          | Testing suggests the value is only ever equal to 1 - LEGACY?      |
| VARIANT                               | VARIANT                   |                                                                   |
| COLOR                                 |                           |                                                                   |
| NAMETAG                               | DISPLAY_NAME              |                                                                   |
| OWNER_EID                             |                           |                                                                   |
| TARGET_EID                            |                           |                                                                   |
| AIR_SUPPLY                            | BREATHING_TICKS_REMAINING |                                                                   |
| EFFECT_COLOR                          |                           |                                                                   |
| EFFECT_AMBIENT                        |                           |                                                                   |
| JUMP_DURATION                         |                           |                                                                   |
| HURT_TIME                             |                           |                                                                   |
| HURT_DIRECTION                        |                           |                                                                   |
| ROW_TIME_LEFT                         |                           |                                                                   |
| ROW_TIME_RIGHT                        |                           |                                                                   |
| EXPERIENCE_VALUE                      |                           |                                                                   |
| DISPLAY_ITEM                          |                           |                                                                   |
| DISPLAY_OFFSET                        |                           |                                                                   |
| CUSTOM_DISPLAY                        |                           |                                                                   |
| SWELL                                 |                           |                                                                   |
| OLD_SWELL                             |                           |                                                                   |
| SWELL_DIRECTION                       |                           |                                                                   |
| CHARGE_AMOUNT                         |                           |                                                                   |
| CARRIED_BLOCK                         |                           |                                                                   |
| CLIENT_EVENT                          |                           |                                                                   |
| USING_ITEM                            |                           |                                                                   |
| PLAYER_FLAGS                          |                           |                                                                   |
| PLAYER_INDEX                          |                           |                                                                   |
| BED_POSITION                          |                           |                                                                   |
| X_POWER                               |                           |                                                                   |
| Y_POWER                               |                           |                                                                   |
| Z_POWER                               |                           |                                                                   |
| AUX_POWER                             |                           |                                                                   |
| FISH_X                                |                           |                                                                   |
| FISH_Z                                |                           |                                                                   |
| FISH_ANGLE                            |                           |                                                                   |
| POTION_AUX_VALUE                      |                           |                                                                   |
| LEASH_HOLDER_EID                      |                           |                                                                   |
| SCALE                                 | SCALE                     |                                                                   |
| HAS_NPC_COMPONENT                     |                           |                                                                   |
| SKIN_ID                               |                           |                                                                   |
| NPC_SKIN_ID                           |                           |                                                                   |
| NPC_DATA                              |                           |                                                                   |
| URL_TAG                               |                           |                                                                   |
| MAX_AIR_SUPPLY                        | MAX_BREATHING_TICKS       |                                                                   |
| MARK_VARIANT                          |                           |                                                                   |
| CONTAINER_TYPE                        |                           |                                                                   |
| CONTAINER_BASE_SIZE                   |                           |                                                                   |
| CONTAINER_STRENGTH_MODIFIER           |                           |                                                                   |
| BLOCK_TARGET                          |                           |                                                                   |
| WITHER_INVULNERABLE_TICKS             |                           |                                                                   |
| WITHER_TARGET_1                       |                           |                                                                   |
| WITHER_TARGET_2                       |                           |                                                                   |
| WITHER_TARGET_3                       |                           |                                                                   |
| WITHER_AERIAL_ATTACK                  |                           |                                                                   |
| BOUNDING_BOX_WIDTH                    | BOUNDING_BOX_WIDTH        |                                                                   |
| BOUNDING_BOX_HEIGHT                   | BOUNDING_BOX_HEIGHT       |                                                                   |
| FUSE_LENGTH                           |                           |                                                                   |
| RIDER_SEAT_POSITION                   |                           |                                                                   |
| RIDER_ROTATION_LOCKED                 |                           |                                                                   |
| RIDER_MAX_ROTATION                    |                           |                                                                   |
| RIDER_MIN_ROTATION                    |                           |                                                                   |
| RIDER_ROTATION_OFFSET                 |                           |                                                                   |
| AREA_EFFECT_CLOUD_RADIUS              |                           |                                                                   |
| AREA_EFFECT_CLOUD_WAITING             |                           |                                                                   |
| AREA_EFFECT_CLOUD_PARTICLE_ID         |                           |                                                                   |
| SHULKER_PEAK_HEIGHT                   |                           |                                                                   |
| SHULKER_PEEK_ID                       |                           |                                                                   |
| SHULKER_ATTACH_FACE                   |                           |                                                                   |
| SHULKER_ATTACH_POS                    |                           |                                                                   |
| TRADE_TARGET_EID                      |                           |                                                                   |
| COMMAND_BLOCK_ENABLED                 |                           |                                                                   |
| COMMAND_BLOCK_COMMAND                 |                           |                                                                   |
| COMMAND_BLOCK_LAST_OUTPUT             |                           |                                                                   |
| COMMAND_BLOCK_TRACK_OUTPUT            |                           |                                                                   |
| CONTROLLING_RIDER_SEAT_INDEX          |                           |                                                                   |
| STRENGTH                              |                           |                                                                   |
| MAX_STRENGTH                          |                           |                                                                   |
| EVOKER_SPELL_COLOR                    |                           |                                                                   |
| LIMITED_LIFE                          |                           |                                                                   |
| ARMOR_STAND_POSE_INDEX                |                           |                                                                   |
| ENDER_CRYSTAL_TIME_OFFSET             |                           |                                                                   |
| NAMETAG_ALWAYS_SHOW                   |                           |                                                                   |
| COLOR_2                               |                           |                                                                   |
| SCORE_TAG                             |                           |                                                                   |
| BALLOON_ATTACHED_ENTITY               |                           |                                                                   |
| PUFFERFISH_SIZE                       |                           |                                                                   |
| BOAT_BUBBLE_TIME                      |                           |                                                                   |
| AGENT_ID                              |                           |                                                                   |
| SITTING_AMOUNT                        |                           |                                                                   |
| SITTING_AMOUNT_PREVIOUS               |                           |                                                                   |
| EATING_COUNTER                        |                           |                                                                   |
| LAYING_AMOUNT                         |                           |                                                                   |
| LAYING_AMOUNT_PREVIOUS                |                           |                                                                   |
| AREA_EFFECT_CLOUD_DURATION            |                           |                                                                   |
| AREA_EFFECT_CLOUD_SPAWN_TIME          |                           |                                                                   |
| AREA_EFFECT_CLOUD_CHANGE_RATE         |                           |                                                                   |
| AREA_EFFECT_CLOUD_CHANGE_ON_PICKUP    |                           |                                                                   |
| AREA_EFFECT_CLOUD_COUNT               |                           |                                                                   |
| INTERACTIVE_TAG                       |                           |                                                                   |
| TRADE_TIER                            |                           |                                                                   |
| MAX_TRADE_TIER                        |                           |                                                                   |
| TRADE_XP                              |                           |                                                                   |
| SPAWNING_FRAMES                       |                           |                                                                   |
| COMMAND_BLOCK_TICK_DELAY              |                           |                                                                   |
| COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK   |                           |                                                                   |
| AMBIENT_SOUND_INTERVAL                |                           |                                                                   |
| AMBIENT_SOUND_INTERVAL_RANGE          |                           |                                                                   |
| AMBIENT_SOUND_EVENT_NAME              |                           |                                                                   |
| FALL_DAMAGE_MULTIPLIER                |                           |                                                                   |
| NAME_RAW_TEXT                         |                           |                                                                   |
| CAN_RIDE_TARGET                       |                           |                                                                   |
| LOW_TIER_CURED_TRADE_DISCOUNT         |                           |                                                                   |
| HIGH_TIER_CURED_TRADE_DISCOUNT        |                           |                                                                   |
| NEARBY_CURED_TRADE_DISCOUNT           |                           |                                                                   |
| NEARBY_CURED_DISCOUNT_TIME_STAMP      |                           |                                                                   |
| HITBOX                                |                           | Possibly linked to Bounding Box properties?                       |
| IS_BUOYANT                            |                           |                                                                   |
| BUOYANCY_DATA                         |                           |                                                                   |
| FREEZING_EFFECT_STRENGTH              |                           |                                                                   |
| GOAT_HORN_COUNT                       |                           |                                                                   |
| BASE_RUNTIME_ID                       |                           |                                                                   |
| DEFINE_PROPERTIES                     |                           |                                                                   |
| UPDATE_PROPERTIES                     |                           |                                                                   |
| MOVEMENT_SOUND_DISTANCE_OFFSET        |                           |                                                                   |
| HEARTBEAT_INTERVAL_TICKS              |                           |                                                                   |
| HEARTBEAT_SOUND_EVENT                 |                           |                                                                   |
| PLAYER_LAST_DEATH_POS                 |                           |                                                                   |
| PLAYER_LAST_DEATH_DIMENSION           |                           |                                                                   |
| PLAYER_HAS_DIED                       |                           |                                                                   |
| ------------------------------------- | ------------------------- | ----------------------------------------------------------------- |


## Entity Flags

See `com.nukkitx.protocol.bedrock.data.entity.EntityFlag` @ NukkitX/Cloudburst Protocol


| EntityFlag Entry              | EntityKeys Impl   | Comments                                                           |
|-------------------------------|-------------------|--------------------------------------------------------------------|
| ON_FIRE                       | BURNING           |                                                                    |
| SNEAKING                      | SNEAKING          |                                                                    |
| RIDING                        |                   |                                                                    |
| SPRINTING                     | SPRINTING         |                                                                    |
| USING_ITEM                    |                   |                                                                    |
| INVISIBLE                     |                   |                                                                    |
| TEMPTED                       |                   |                                                                    |
| IN_LOVE                       |                   |                                                                    |
| SADDLED                       |                   |                                                                    |
| POWERED                       |                   |                                                                    |
| IGNITED                       |                   |                                                                    |
| BABY                          |                   |                                                                    |
| CONVERTING                    |                   |                                                                    |
| CRITICAL                      |                   |                                                                    |
| CAN_SHOW_NAME                 |                   |                                                                    |
| ALWAYS_SHOW_NAME              |                   |                                                                    |
| NO_AI                         | AI_ENABLED        | Flipped.                                                           |
| SILENT                        |                   |                                                                    |
| WALL_CLIMBING                 |                   |                                                                    |
| CAN_CLIMB                     | CLIMBING_ENABLED  |                                                                    |
| CAN_SWIM                      |                   |                                                                    |
| CAN_FLY                       |                   |                                                                    |
| CAN_WALK                      |                   |                                                                    |
| RESTING                       |                   |                                                                    |
| SITTING                       |                   |                                                                    |
| ANGRY                         |                   |                                                                    |
| INTERESTED                    |                   |                                                                    |
| CHARGED                       |                   |                                                                    |
| TAMED                         |                   |                                                                    |
| ORPHANED                      |                   |                                                                    |
| LEASHED                       |                   |                                                                    |
| SHEARED                       |                   |                                                                    |
| GLIDING                       |                   |                                                                    |
| ELDER                         |                   |                                                                    |
| MOVING                        |                   |                                                                    |
| BREATHING                     | BREATHING         |                                                                    |
| CHESTED                       |                   |                                                                    |
| STACKABLE                     |                   |                                                                    |
| SHOW_BOTTOM                   |                   |                                                                    |
| STANDING                      |                   |                                                                    |
| SHAKING                       |                   |                                                                    |
| IDLING                        |                   |                                                                    |
| CASTING                       |                   |                                                                    |
| CHARGING                      |                   |                                                                    |
| WASD_CONTROLLED               |                   |                                                                    |
| CAN_POWER_JUMP                |                   |                                                                    |
| LINGERING                     |                   |                                                                    |
| HAS_COLLISION                 | COLLISION_ENABLED |                                                                    |
| HAS_GRAVITY                   | GRAVITY_ENABLED   |                                                                    |
| FIRE_IMMUNE                   |                   |                                                                    |
| DANCING                       |                   |                                                                    |
| ENCHANTED                     |                   |                                                                    |
| RETURN_TRIDENT                |                   |                                                                    |
| CONTAINER_IS_PRIVATE          |                   |                                                                    |
| IS_TRANSFORMING               |                   |                                                                    |
| DAMAGE_NEARBY_MOBS            |                   |                                                                    |
| SWIMMING                      | SWIMMING          |                                                                    |
| BRIBED                        |                   |                                                                    |
| IS_PREGNANT                   |                   |                                                                    |
| LAYING_EGG                    |                   |                                                                    |
| RIDER_CAN_PICK                |                   |                                                                    |
| TRANSITION_SITTING            |                   |                                                                    |
| EATING                        |                   |                                                                    |
| LAYING_DOWN                   |                   |                                                                    |
| SNEEZING                      |                   |                                                                    |
| TRUSTING                      |                   |                                                                    |
| ROLLING                       |                   |                                                                    |
| SCARED                        |                   |                                                                    |
| IN_SCAFFOLDING                |                   |                                                                    |
| OVER_SCAFFOLDING              |                   |                                                                    |
| FALL_THROUGH_SCAFFOLDING      |                   |                                                                    |
| BLOCKING                      |                   |                                                                    |
| TRANSITION_BLOCKING           |                   |                                                                    |
| BLOCKED_USING_SHIELD          |                   |                                                                    |
| BLOCKED_USING_DAMAGED_SHIELD  |                   |                                                                    |
| SLEEPING                      |                   |                                                                    |
| WANTS_TO_WAKE                 |                   |                                                                    |
| TRADE_INTEREST                |                   |                                                                    |
| DOOR_BREAKER                  |                   |                                                                    |
| BREAKING_OBSTRUCTION          |                   |                                                                    |
| DOOR_OPENER                   |                   |                                                                    |
| IS_ILLAGER_CAPTAIN            |                   |                                                                    |
| STUNNED                       |                   |                                                                    |
| ROARING                       |                   |                                                                    |
| DELAYED_ATTACK                |                   |                                                                    |
| IS_AVOIDING_MOBS              |                   |                                                                    |
| IS_AVOIDING_BLOCK             |                   |                                                                    |
| FACING_TARGET_TO_RANGE_ATTACK |                   |                                                                    |
| HIDDEN_WHEN_INVISIBLE         |                   |                                                                    |
| IS_IN_UI                      |                   |                                                                    |
| STALKING                      |                   |                                                                    |
| EMOTING                       |                   |                                                                    |
| CELEBRATING                   |                   |                                                                    |
| ADMIRING                      |                   |                                                                    |
| CELEBRATING_SPECIAL           |                   |                                                                    |
| OUT_OF_CONTROL                |                   |                                                                    |
| RAM_ATTACK                    |                   |                                                                    |
| PLAYING_DEAD                  |                   |                                                                    |
| IN_ASCENDABLE_BLOCK           |                   |                                                                    |
| OVER_DESCENDABLE_BLOCK        |                   |                                                                    |
| CROAKING                      |                   |                                                                    |
| EAT_MOB                       |                   |                                                                    |
| JUMP_GOAL_JUMP                |                   |                                                                    |
| EMERGING                      |                   |                                                                    |
| SNIFFING                      |                   |                                                                    |
| DIGGING                       |                   |                                                                    |
| SONIC_BOOM                    |                   |                                                                    |