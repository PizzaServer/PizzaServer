package io.github.pizzaserver.api.entity;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.keychain.EntityKeys;

public final class EntityHelper {

    public static Vector3f getBasicRotationFor(Entity entity) {
        return Vector3f.from(
                entity.get(EntityKeys.ROTATION_PITCH).orElse(0f),
                entity.get(EntityKeys.ROTATION_YAW).orElse(0f),
                entity.get(EntityKeys.ROTATION_HEAD_YAW).orElse(0f)
        );
    }

}
