package io.github.pizzaserver.api.entity.data.attributes;

import io.github.pizzaserver.api.keychain.EntityKeys;
import io.github.pizzaserver.commons.data.key.DataKey;

import java.util.*;
import java.util.stream.Collectors;

public class AttributeType {

    public static final int PLAYER_XP_LEVEL_LIMIT = 24791;

    public static final Set<DataKey<? extends Number>> ALL_ATTRIBUTE_KEY_DEPENDENCIES;
    public static final Set<AttributeTemplate<? extends Number>> ALL_ATTRIBUTES;
    public static final Set<AttributeTemplate<? extends Number>> BASIC_REQUIRED_ATTRIBUTES;

    // When implementing attributes, make sure you add them to ALL_ATTRIBUTES.
    // ALL_ATTRIBUTE_KEY_DEPENDENCIES derives the necessary keys to create from them.

    public static final AttributeTemplate<Float> HEALTH = AttributeTemplate.builder(EntityKeys.HEALTH)
            .min(EntityKeys.KILL_THRESHOLD)
            .max(EntityKeys.MAX_HEALTH)
            .defaults(EntityKeys.MAX_HEALTH)
            .build();

    public static final AttributeTemplate<Float> ABSORPTION = AttributeTemplate.builder(EntityKeys.ABSORPTION)
            .min(0f)
            .max(EntityKeys.MAX_ABSORPTION)
            .defaults(0f)
            .build();

    public static final AttributeTemplate<Float> FOOD = AttributeTemplate.builder(EntityKeys.FOOD)
            .min(0f)
            .max(20f)
            .defaults(20f)
            .build();

    public static final AttributeTemplate<Float> SATURATION = AttributeTemplate.builder(EntityKeys.SATURATION)
            .min(0f)
            .max(Float.MAX_VALUE)
            .defaults(0f)
            .build();

    public static final AttributeTemplate<Float> PLAYER_EXPERIENCE = AttributeTemplate.builder(EntityKeys.PLAYER_XP)
            .min(0f)
            .max(1f)
            .defaults(0f)
            .build();

    public static final AttributeTemplate<Integer> PLAYER_EXPERIENCE_LEVEL = AttributeTemplate.builder(EntityKeys.PLAYER_XP_LEVELS)
            .min(0)
            .max(PLAYER_XP_LEVEL_LIMIT)
            .defaults(0)
            .build();

    public static final AttributeTemplate<Float> MOVEMENT_SPEED = AttributeTemplate.builder(EntityKeys.MOVEMENT_SPEED)
            .min(0f)
            .max(Float.MAX_VALUE)
            .defaults(EntityKeys.MOVEMENT_SPEED) //0.1f base default.
            .build();



    static {
        ALL_ATTRIBUTES = Set.of(
                HEALTH, ABSORPTION, FOOD, SATURATION,
                PLAYER_EXPERIENCE, PLAYER_EXPERIENCE_LEVEL,
                MOVEMENT_SPEED
        );

        ALL_ATTRIBUTE_KEY_DEPENDENCIES = ALL_ATTRIBUTES.stream()
                .flatMap(t -> t.getDependentKeys().stream())
                .collect(Collectors.toUnmodifiableSet());

        BASIC_REQUIRED_ATTRIBUTES = Set.of(
                HEALTH, MOVEMENT_SPEED
        );
    }
}
