package io.github.pizzaserver.api.entity.data.attributes;

import io.github.pizzaserver.api.keychain.EntityKeys;

public class AttributeType {

    public static final AttributeTemplate HEALTH = AttributeTemplate.builder(EntityKeys.HEALTH)
            .min(0f)
            .max(EntityKeys.MAX_HEALTH)
            .defaults(EntityKeys.MAX_HEALTH)
            .build();

    public static final AttributeTemplate ABSORPTION = AttributeTemplate.builder(EntityKeys.ABSORPTION)
            .min(0f)
            .max(0f)
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

    public static final AttributeTemplate EXPERIENCE = AttributeTemplate.builder(EntityKeys.XP)
            .min(0f)
            .max(1f)
            .defaults(0f)
            .build();

    public static final AttributeTemplate EXPERIENCE_LEVEL = AttributeTemplate.builder(EntityKeys.XP_LEVELS)
            .min(0f)
            .max(24791f)
            .defaults(0f)
            .build();

    public static final AttributeTemplate MOVEMENT_SPEED = AttributeTemplate.builder(EntityKeys.MOVEMENT_SPEED)
            .min(0f)
            .max(Float.MAX_VALUE)
            .defaults(0.1f)
            .build();
}
