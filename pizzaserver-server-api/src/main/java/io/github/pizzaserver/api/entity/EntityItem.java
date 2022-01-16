package io.github.pizzaserver.api.entity;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.item.Item;

import java.util.concurrent.ThreadLocalRandom;

public interface EntityItem extends Entity {

    Item getItem();

    void setItem(Item item);

    void setPickupDelay(int ticks);

    int getPickupDelay();

    /**
     * Retrieve a random motion as if an item was mined.
     *
     * @return random motion
     */
    static Vector3f getRandomMotion() {
        return Vector3f.from(ThreadLocalRandom.current().nextFloat() * 0.2f,
                             ThreadLocalRandom.current().nextFloat() * 0.2f,
                             ThreadLocalRandom.current().nextFloat() * 0.2f);
    }
}
