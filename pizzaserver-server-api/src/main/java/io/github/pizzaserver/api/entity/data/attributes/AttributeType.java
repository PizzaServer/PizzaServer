package io.github.pizzaserver.api.entity.data.attributes;

import io.github.pizzaserver.api.keychain.EntityKeys;
import io.github.pizzaserver.commons.data.DataKey;

import java.util.*;
import java.util.stream.Collectors;

public class AttributeType {

    public static final Set<DataKey<Float>> ALL_ATTRIBUTE_KEY_DEPENDENCIES;
    public static final AttributeTemplate[] ALL_ATTRIBUTES;
    public static final AttributeTemplate[] BASIC_REQUIRED_ATTRIBUTES;

    // When implementing attributes, make sure you add them to ALL_ATTRIBUTES.
    // ALL_ATTRIBUTE_KEY_DEPENDENCIES derives the necessary keys to create from them.

    public static final AttributeTemplate HEALTH = AttributeTemplate.builder(EntityKeys.HEALTH)
            .min(EntityKeys.KILL_THRESHOLD)
            .max(EntityKeys.MAX_HEALTH)
            .defaults(EntityKeys.MAX_HEALTH)
            .build();

    public static final AttributeTemplate ABSORPTION = AttributeTemplate.builder(EntityKeys.ABSORPTION)
            .min(0f)
            .max(EntityKeys.MAX_ABSORPTION)
            .defaults(0f)
            .build();

    public static final AttributeTemplate FOOD = AttributeTemplate.builder(EntityKeys.FOOD)
            .min(0f)
            .max(20f)
            .defaults(20f)
            .build();

    public static final AttributeTemplate SATURATION = AttributeTemplate.builder(EntityKeys.SATURATION)
            .min(0f)
            .max(Float.MAX_VALUE)
            .defaults(0f)
            .build();

    public static final AttributeTemplate PLAYER_EXPERIENCE = AttributeTemplate.builder(EntityKeys.PLAYER_XP)
            .min(0f)
            .max(1f)
            .defaults(0f)
            .build();

    public static final AttributeTemplate PLAYER_EXPERIENCE_LEVEL = AttributeTemplate.builder(EntityKeys.PLAYER_XP_LEVELS)
            .min(0f)
            .max(24791f)
            .defaults(0f)
            .build();

    public static final AttributeTemplate MOVEMENT_SPEED = AttributeTemplate.builder(EntityKeys.MOVEMENT_SPEED)
            .min(0f)
            .max(Float.MAX_VALUE)
            .defaults(EntityKeys.MOVEMENT_SPEED) //0.1f base default.
            .build();



    static {
        ALL_ATTRIBUTES = new AttributeTemplate[] {
                HEALTH, ABSORPTION, FOOD, SATURATION,
                PLAYER_EXPERIENCE, PLAYER_EXPERIENCE_LEVEL,
                MOVEMENT_SPEED
        };

        ALL_ATTRIBUTE_KEY_DEPENDENCIES = Arrays.stream(ALL_ATTRIBUTES)
                .flatMap(t -> t.getDependentKeys().stream())
                .collect(Collectors.toUnmodifiableSet());

        BASIC_REQUIRED_ATTRIBUTES = new AttributeTemplate[] {
                HEALTH, MOVEMENT_SPEED
        };
    }
}
