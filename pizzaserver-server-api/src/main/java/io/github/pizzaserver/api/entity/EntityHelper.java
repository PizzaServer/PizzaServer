package io.github.pizzaserver.api.entity;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.entity.data.attributes.AttributeTemplate;
import io.github.pizzaserver.api.entity.data.attributes.AttributeType;
import io.github.pizzaserver.api.entity.data.attributes.AttributeView;
import io.github.pizzaserver.api.keychain.EntityKeys;
import io.github.pizzaserver.commons.data.DataKey;

import java.util.*;
import java.util.stream.Collectors;

public final class EntityHelper {

    public static final AttributeTemplate[] PLAYER_ATTRIBUTES = new AttributeTemplate[] {
            AttributeType.HEALTH, AttributeType.ABSORPTION, AttributeType.FOOD, AttributeType.SATURATION,
            AttributeType.EXPERIENCE, AttributeType.EXPERIENCE_LEVEL, AttributeType.MOVEMENT_SPEED
    };


    public static Vector3f getBasicRotationFor(Entity entity) {
        return Vector3f.from(
                entity.get(EntityKeys.ROTATION_PITCH).orElse(0f),
                entity.get(EntityKeys.ROTATION_YAW).orElse(0f),
                entity.get(EntityKeys.ROTATION_HEAD_YAW).orElse(0f)
        );
    }

    public static Map<DataKey<Float>, AttributeView> generateAttributes(Entity entity, AttributeTemplate... templates) {
        return Arrays.stream(templates)
                .map(t -> t.using(entity))
                .collect(Collectors.toUnmodifiableMap(
                        AttributeView::getID,
                        template -> template
                ));
    }

}
