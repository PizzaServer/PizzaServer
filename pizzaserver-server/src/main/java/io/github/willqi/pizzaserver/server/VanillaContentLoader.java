package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.definition.components.handlers.*;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.*;
import io.github.willqi.pizzaserver.api.entity.definition.impl.CowEntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.impl.HumanEntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.impl.ItemEntityDefinition;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.server.level.world.blocks.types.*;

import java.util.ArrayList;
import java.util.Collections;

public class VanillaContentLoader {

    public static void load() {
        loadItems();
        loadBlocks();
        loadEntityComponents();
        loadEntities();
    }

    private static void loadItems() {

    }

    private static void loadBlocks() {
        BlockRegistry.register(new BlockTypeAir());
        BlockRegistry.register(new BlockTypeDirt());
        BlockRegistry.register(new BlockTypeGrass());
        BlockRegistry.register(new BlockTypeStone());
    }

    private static void loadEntityComponents() {
        EntityRegistry.registerComponent(EntityScaleComponent.class, new EntityScaleComponent(1), new EntityScaleComponentHandler());
        EntityRegistry.registerComponent(EntityCollisionBoxComponent.class, new EntityCollisionBoxComponent(0f, 0f), new EntityCollisionBoxComponentHandler());
        EntityRegistry.registerComponent(EntityHealthComponent.class, new EntityHealthComponent(0, 0, 0), new EntityHealthComponentHandler());
        EntityRegistry.registerComponent(EntityDamageSensorComponent.class, new EntityDamageSensorComponent(new EntityDamageSensorComponent.Sensor[0]), new EmptyEntityComponentHandler<>());
        EntityRegistry.registerComponent(EntityLootComponent.class, new EntityLootComponent(new ArrayList<>()), new EntityLootComponentHandler());
        EntityRegistry.registerComponent(EntityDeathMessageComponent.class, new EntityDeathMessageComponent(false), new EntityDeathMessageComponentHandler());
        EntityRegistry.registerComponent(EntityPhysicsComponent.class, new EntityPhysicsComponent(new EntityPhysicsComponent.Properties()
                .setCollision(true)
                .setGravity(true)
                .setPushable(true)
                .setPistonPushable(true)), new EntityPhysicsComponentHandler());
    }

    private static void loadEntities() {
        EntityRegistry.registerDefinition(new HumanEntityDefinition());
        EntityRegistry.registerDefinition(new ItemEntityDefinition());
        EntityRegistry.registerDefinition(new CowEntityDefinition());
    }

}
