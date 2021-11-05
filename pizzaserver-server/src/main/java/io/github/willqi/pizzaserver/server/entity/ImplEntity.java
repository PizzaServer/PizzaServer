package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.attributes.Attribute;
import io.github.willqi.pizzaserver.api.entity.attributes.AttributeType;
import io.github.willqi.pizzaserver.api.entity.data.DamageCause;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityDamageSensorComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.filter.EntityFilter;
import io.github.willqi.pizzaserver.api.entity.definition.components.filter.EntityFilterData;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityDeathMessageComponent;
import io.github.willqi.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.willqi.pizzaserver.api.event.type.entity.EntityDamageByEntityEvent;
import io.github.willqi.pizzaserver.api.event.type.entity.EntityDamageEvent;
import io.github.willqi.pizzaserver.api.event.type.entity.EntityDeathEvent;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.item.types.components.ArmorItemComponent;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.network.protocol.data.EntityEventType;
import io.github.willqi.pizzaserver.api.network.protocol.packets.*;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.BoundingBox;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.utils.TextMessage;
import io.github.willqi.pizzaserver.api.utils.TextType;
import io.github.willqi.pizzaserver.commons.utils.NumberUtils;
import io.github.willqi.pizzaserver.commons.utils.Vector2;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.entity.inventory.ImplEntityInventory;
import io.github.willqi.pizzaserver.server.level.ImplLevel;
import io.github.willqi.pizzaserver.server.level.world.ImplWorld;
import io.github.willqi.pizzaserver.server.level.world.chunks.ImplChunk;

import java.util.*;

public class ImplEntity implements Entity {

    public static long ID = 1;

    protected final long id;
    protected volatile float x;
    protected volatile float y;
    protected volatile float z;
    protected volatile World world;
    protected boolean moveUpdate;
    protected EntityPhysicsEngine physicsEngine = new EntityPhysicsEngine(this);

    protected boolean vulnerable = true;
    protected int deathAnimationTicks = -1;
    protected int noHitTicks;

    protected int fireTicks;

    protected final EntityAttributes attributes = new EntityAttributes();

    protected float pitch;
    protected float yaw;
    protected float headYaw;

    protected BoundingBox boundingBox = new BoundingBox();
    protected boolean pistonPushable;
    protected boolean pushable;

    protected final EntityDefinition entityDefinition;
    protected final LinkedList<EntityComponentGroup> componentGroups = new LinkedList<>();

    protected EntityInventory inventory = new ImplEntityInventory(this, Collections.singleton(InventorySlotType.INVENTORY), 0);
    protected List<ItemStack> loot = new ArrayList<>();
    protected EntityMetaData metaData = new EntityMetaData();
    protected boolean metaDataUpdate;

    protected EntityDamageEvent lastDamageEvent = null;
    protected boolean showDeathMessages;

    protected boolean spawned;
    protected final Set<Player> spawnedTo = new HashSet<>();
    protected final Set<Player> hiddenFrom = new HashSet<>();


    @SuppressWarnings({"rawtypes", "unchecked"})
    public ImplEntity(EntityDefinition entityDefinition) {
        this.id = ID++;
        this.entityDefinition = entityDefinition;

        // Apply default components to the entity
        EntityRegistry.getComponentClasses().forEach(clazz -> {
            EntityComponentHandler handler = EntityRegistry.getComponentHandler(clazz);
            handler.onRegistered(this, EntityRegistry.getDefaultComponent(clazz));
        });

        EntityMetaData metaData = this.getMetaData();
        metaData.setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_MOVING, true);
        this.setMetaData(metaData);
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public EntityDefinition getEntityDefinition() {
        return this.entityDefinition;
    }

    @Override
    public boolean addComponentGroup(String groupId) {
        return this.addComponentGroup(this.getEntityDefinition().getComponentGroup(groupId));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean addComponentGroup(EntityComponentGroup group) {
        if (this.componentGroups.contains(group)) {
            return false;
        }

        // Unregister previous active components and register new active components
        for (EntityComponent newComponent : group.getComponents()) {
            Class<? extends EntityComponent> newComponentClazz = newComponent.getClass();
            EntityComponentHandler handler = EntityRegistry.getComponentHandler(newComponentClazz);

            for (EntityComponentGroup existingComponentGroup : this.componentGroups) {
                if (existingComponentGroup.hasComponent(newComponentClazz)) {
                    handler.onUnregistered(this, existingComponentGroup.getComponent(newComponentClazz));
                    break;
                }
            }

            handler.onRegistered(this, group.getComponent(newComponentClazz));
        }

        this.componentGroups.addFirst(group);
        return true;
    }

    @Override
    public boolean removeComponentGroup(String groupId) {
        return this.removeComponentGroup(this.getEntityDefinition().getComponentGroup(groupId));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean removeComponentGroup(EntityComponentGroup removingComponentGroup) {
        if (this.componentGroups.contains(removingComponentGroup)) {
            // Find components that are in groups higher than the removed group so that we don't treat all the components
            // in the removed group as the current active ones.
            Set<Class<? extends EntityComponent>> activeHigherComponents = new HashSet<>();

            Iterator<EntityComponentGroup> groupIterator = this.componentGroups.iterator();
            while (groupIterator.hasNext()) {
                EntityComponentGroup iteratedGroup = groupIterator.next();
                if (iteratedGroup.getGroupId().equals(removingComponentGroup.getGroupId())) {
                    groupIterator.remove();
                    break;
                } else {
                    activeHigherComponents.addAll(iteratedGroup.getComponentClasses());
                }
            }

            // Unregister components of the group from this entity and then find and apply the new active components of the same types.
            for (EntityComponent removeComponent : removingComponentGroup.getComponents()) {
                Class<? extends EntityComponent> removeComponentClazz = removeComponent.getClass();
                EntityComponentHandler handler = EntityRegistry.getComponentHandler(removeComponentClazz);

                if (!activeHigherComponents.contains(removeComponentClazz)) {
                    handler.onUnregistered(this, removingComponentGroup.getComponent(removeComponentClazz));
                    handler.onRegistered(this, this.getComponent(removeComponentClazz));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public <T extends EntityComponent> T getComponent(Class<T> componentClazz) {
        for (EntityComponentGroup group : this.componentGroups) {
            if (group.hasComponent(componentClazz)) {
                return group.getComponent(componentClazz);
            }
        }

        return EntityRegistry.getDefaultComponent(componentClazz);
    }

    @Override
    public boolean hasComponent(Class<? extends EntityComponent> componentClazz) {
        for (EntityComponentGroup group : this.componentGroups) {
            if (group.hasComponent(componentClazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public float getZ() {
        return this.z;
    }

    @Override
    public int getFloorX() {
        return (int) Math.floor(this.x);
    }

    @Override
    public int getFloorY() {
        return (int) Math.floor(this.y);
    }

    @Override
    public int getFloorZ() {
        return (int) Math.floor(this.z);
    }

    @Override
    public boolean isOnGround() {
        int minBlockXCheck = (int) Math.floor(this.getX() - this.getBoundingBox().getWidth());
        int minBlockZCheck = (int) Math.floor(this.getZ() - this.getBoundingBox().getWidth());
        int maxBlockXCheck = (int) Math.ceil(this.getX() + this.getBoundingBox().getWidth());
        int maxBlockZCheck = (int) Math.ceil(this.getZ() + this.getBoundingBox().getWidth());

        BoundingBox intersectingBoundingBox = this.getBoundingBox().clone();
        intersectingBoundingBox.setPosition(intersectingBoundingBox.getPosition().subtract(0, 0.0002f, 0));

        for (int x = minBlockXCheck; x <= maxBlockXCheck; x++) {
            for (int z = minBlockZCheck; z <= maxBlockZCheck; z++) {
                Block blockBelow = this.getWorld().getBlock(x, this.getFloorY() - 1, z);
                if (blockBelow.isSolid() && blockBelow.getBoundingBox().collidesWith(intersectingBoundingBox)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Location getLocation() {
        return new Location(this.world, new Vector3(this.getX(), this.getY(), this.getZ()));
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public float getEyeHeight() {
        return this.getHeight() / 2 + 0.1f;
    }

    @Override
    public String getName() {
        return this.getDisplayName().orElse(this.getEntityDefinition().getName());
    }

    @Override
    public float getHeight() {
        return this.getBoundingBox().getHeight() / this.getScale();
    }

    @Override
    public void setHeight(float height) {
        this.getBoundingBox().setHeight(height * this.getScale());
        EntityMetaData metaData = this.getMetaData();
        metaData.setFloatProperty(EntityMetaPropertyName.BOUNDING_BOX_HEIGHT, height);
        this.setMetaData(metaData);
    }

    @Override
    public float getWidth() {
        return this.getBoundingBox().getWidth() / this.getScale();
    }

    @Override
    public void setWidth(float width) {
        this.getBoundingBox().setWidth(width * this.getScale());
        EntityMetaData metaData = this.getMetaData();
        metaData.setFloatProperty(EntityMetaPropertyName.BOUNDING_BOX_WIDTH, width);
        this.setMetaData(metaData);
    }

    /**
     * Set the internal position of the entity.
     * Used internally to setup and to clean up the entity
     * @param location entity location
     */
    public void setPosition(Location location) {
        if (location != null) {
            this.setPosition(location.getWorld(), location.getX(), location.getY(), location.getZ());
        } else {
            this.setPosition(null, 0, 0, 0);
        }
    }

    public void setPosition(World world, float x, float y, float z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.getBoundingBox().setPosition(x, y, z);
    }

    @Override
    public void teleport(float x, float y, float z) {
        this.teleport(this.getWorld(), x, y, z);
    }

    @Override
    public void teleport(Location location) {
        this.teleport(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void teleport(World world, float x, float y, float z) {
        World oldWorld = this.getWorld();
        this.moveUpdate = true;

        if (!world.equals(oldWorld)) {
            oldWorld.removeEntity(this);
            world.addEntity(this, new Vector3(x, y, z));
        } else {
            this.setPosition(new Location(this.world, this.x, this.y, this.z));
        }
    }

    @Override
    public Vector3 getVelocity() {
        return this.physicsEngine.getVelocity();
    }

    @Override
    public void setVelocity(Vector3 velocity) {
        this.physicsEngine.setVelocity(velocity);

        SetEntityVelocityPacket velocityPacket = new SetEntityVelocityPacket();
        velocityPacket.setVelocity(this.getVelocity());
        velocityPacket.setEntityRuntimeId(this.getId());
        for (Player player : this.getViewers()) {
            player.sendPacket(velocityPacket);
        }
    }

    @Override
    public Optional<String> getDisplayName() {
        return Optional.ofNullable(this.getMetaData().getStringProperty(EntityMetaPropertyName.NAMETAG));
    }

    @Override
    public void setDisplayName(String name) {
        this.getMetaData().setStringProperty(EntityMetaPropertyName.NAMETAG, name);
    }

    @Override
    public boolean isVulnerable() {
        return this.vulnerable;
    }

    @Override
    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    @Override
    public Set<Attribute> getAttributes() {
        return this.attributes.getAttributes();
    }

    @Override
    public Attribute getAttribute(AttributeType type) {
        return this.attributes.getAttribute(type);
    }

    @Override
    public float getMovementSpeed() {
        return this.getAttribute(AttributeType.MOVEMENT_SPEED).getCurrentValue();
    }

    @Override
    public void setMovementSpeed(float movementSpeed) {
        Attribute attribute = this.getAttribute(AttributeType.MOVEMENT_SPEED);
        attribute.setCurrentValue(Math.max(attribute.getMinimumValue(), movementSpeed));
        attribute.setDefaultValue(Math.max(attribute.getMinimumValue(), movementSpeed));
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }

    @Override
    public void setPitch(float pitch) {
        this.moveUpdate = true;
        this.pitch = pitch;
    }

    @Override
    public float getYaw() {
        return this.yaw;
    }

    @Override
    public void setYaw(float yaw) {
        this.moveUpdate = true;
        this.yaw = yaw;
    }

    @Override
    public float getHeadYaw() {
        return this.headYaw;
    }

    @Override
    public void setHeadYaw(float headYaw) {
        this.moveUpdate = true;
        this.headYaw = headYaw;
    }

    @Override
    public boolean isAlive() {
        return this.getHealth() >= this.getAttribute(AttributeType.HEALTH).getMinimumValue();
    }

    @Override
    public float getHealth() {
        return this.getAttribute(AttributeType.HEALTH).getCurrentValue();
    }

    @Override
    public void setHealth(float health) {
        Attribute attribute = this.getAttribute(AttributeType.HEALTH);
        attribute.setMaximumValue(Math.max(attribute.getMaximumValue(), health));
        attribute.setCurrentValue(Math.max(0, health));
    }

    @Override
    public float getMaxHealth() {
        return this.getAttribute(AttributeType.HEALTH).getMaximumValue();
    }

    @Override
    public void setMaxHealth(float maxHealth) {
        Attribute attribute = this.getAttribute(AttributeType.HEALTH);

        float newMaxHealth = Math.max(attribute.getMinimumValue(), maxHealth);
        attribute.setMaximumValue(newMaxHealth);
        attribute.setCurrentValue(Math.min(this.getHealth(), this.getMaxHealth()));
    }

    @Override
    public float getAbsorption() {
        return this.getAttribute(AttributeType.ABSORPTION).getCurrentValue();
    }

    @Override
    public void setAbsorption(float absorption) {
        Attribute attribute = this.getAttribute(AttributeType.ABSORPTION);

        float newAbsorption = Math.max(attribute.getMinimumValue(), Math.min(absorption, this.getMaxAbsorption()));
        attribute.setCurrentValue(newAbsorption);
    }

    @Override
    public float getMaxAbsorption() {
        return this.getAttribute(AttributeType.ABSORPTION).getMaximumValue();
    }

    @Override
    public void setMaxAbsorption(float maxAbsorption) {
        Attribute attribute = this.getAttribute(AttributeType.ABSORPTION);

        float newMaxAbsorption = Math.max(attribute.getMinimumValue(), maxAbsorption);
        attribute.setMaximumValue(newMaxAbsorption);
        attribute.setCurrentValue(Math.min(this.getAbsorption(), this.getMaxAbsorption()));
    }

    @Override
    public Vector3 getDirectionVector() {
        double cosPitch = Math.cos(Math.toRadians(this.getPitch()));
        double x = Math.sin(Math.toRadians(this.getYaw())) * -cosPitch;
        double y = -Math.sin(Math.toRadians(this.getPitch()));
        double z = Math.cos(Math.toRadians(this.getYaw())) * cosPitch;

        return new Vector3((float) x, (float) y, (float) z).normalize();
    }

    @Override
    public ImplChunk getChunk() {
        return (ImplChunk) this.getLocation().getChunk();
    }

    @Override
    public ImplWorld getWorld() {
        return (ImplWorld) this.getLocation().getWorld();
    }

    @Override
    public ImplLevel getLevel() {
        return (ImplLevel) this.getLocation().getLevel();
    }

    @Override
    public ImplServer getServer() {
        return (ImplServer) ImplServer.getInstance();
    }

    @Override
    public EntityMetaData getMetaData() {
        return this.metaData;
    }

    @Override
    public void setMetaData(EntityMetaData metaData) {
        this.metaData = metaData;
        this.metaDataUpdate = true;
    }

    @Override
    public boolean hasGravity() {
        return this.getMetaData().hasFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_GRAVITY);
    }

    @Override
    public void setGravity(boolean enabled) {
        EntityMetaData metaData = this.getMetaData();
        metaData.setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_GRAVITY, enabled);
        this.setMetaData(metaData);
    }

    @Override
    public boolean hasCollision() {
        return this.getMetaData().hasFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_COLLISION);
    }

    @Override
    public void setCollision(boolean canCollide) {
        EntityMetaData metaData = this.getMetaData();
        metaData.setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_COLLISION, canCollide);
        this.setMetaData(metaData);
    }

    @Override
    public boolean isPushable() {
        return this.pushable;
    }

    @Override
    public void setPushable(boolean enabled) {
        this.pushable = enabled;
    }

    @Override
    public boolean isPistonPushable() {
        return this.pistonPushable;
    }

    @Override
    public void setPistonPushable(boolean enabled) {
        this.pistonPushable = enabled;
    }

    @Override
    public boolean hasAI() {
        return !this.getMetaData().hasFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_NO_AI);
    }

    @Override
    public void setAI(boolean hasAI) {
        EntityMetaData metaData = this.getMetaData();
        metaData.setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_NO_AI, !hasAI);
        this.setMetaData(metaData);
    }

    @Override
    public float getScale() {
        return this.getMetaData().getFloatProperty(EntityMetaPropertyName.SCALE);
    }

    @Override
    public void setScale(float scale) {
        this.getBoundingBox().setWidth(this.getWidth() * scale);
        this.getBoundingBox().setHeight(this.getHeight() * scale);

        EntityMetaData data = this.getMetaData();
        data.setFloatProperty(EntityMetaPropertyName.SCALE, scale);
        this.setMetaData(data);
    }

    @Override
    public int getFireTicks() {
        return this.fireTicks;
    }

    @Override
    public void setFireTicks(int ticks) {
        boolean onFirePreviously = this.getFireTicks() > 0;
        this.fireTicks = ticks;

        EntityMetaData metaData = this.getMetaData();
        if (this.fireTicks > 0 && !onFirePreviously) {
            metaData.setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_ON_FIRE, true);
            this.setMetaData(metaData);
        } else if (this.fireTicks <= 0 && onFirePreviously) {
            metaData.setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_ON_FIRE, false);
            this.setMetaData(metaData);
        }
    }

    @Override
    public List<ItemStack> getLoot() {
        return this.loot;
    }

    @Override
    public void setLoot(List<ItemStack> loot) {
        this.loot = loot;
    }

    @Override
    public EntityInventory getInventory() {
        return this.inventory;
    }

    @Override
    public int getArmourPoints() {
        int armourPoints = 0;

        if (this.getInventory().getHelmet().getItemType() instanceof ArmorItemComponent) {
            armourPoints += ((ArmorItemComponent) this.getInventory().getHelmet().getItemType()).getProtection();
        }
        if (this.getInventory().getChestplate().getItemType() instanceof ArmorItemComponent) {
            armourPoints += ((ArmorItemComponent) this.getInventory().getChestplate().getItemType()).getProtection();
        }
        if (this.getInventory().getLeggings().getItemType() instanceof ArmorItemComponent) {
            armourPoints += ((ArmorItemComponent) this.getInventory().getLeggings().getItemType()).getProtection();
        }
        if (this.getInventory().getBoots().getItemType() instanceof ArmorItemComponent) {
            armourPoints += ((ArmorItemComponent) this.getInventory().getBoots().getItemType()).getProtection();
        }

        return armourPoints;
    }

    @Override
    public int getNoHitTicks() {
        return this.noHitTicks;
    }

    @Override
    public void setNoHitTicks(int ticks) {
        this.noHitTicks = Math.max(0, ticks);
    }

    @Override
    public void hurt(float damage) {
        this.setHealth(this.getHealth() - damage);
        if (damage > 0 && this.isAlive()) {
            EntityEventPacket entityEventPacket = new EntityEventPacket();
            entityEventPacket.setRuntimeEntityId(this.getId());
            entityEventPacket.setType(EntityEventType.HURT);

            for (Player player : this.getViewers()) {
                player.sendPacket(entityEventPacket);
            }
            if (this instanceof Player) {
                ((Player) this).sendPacket(entityEventPacket);
            }
        }
    }

    @Override
    public void kill() {
        if (this.hasSpawned() && this.deathAnimationTicks == -1) {
            this.setHealth(0);
            this.setAI(false);

            this.deathAnimationTicks = 20;
            this.startDeathAnimation();

            TextMessage deathMessage = null;
            if (this.getComponent(EntityDeathMessageComponent.class).showDeathMessages()) {
                deathMessage = this.getDeathMessage().orElse(null);
            }

            EntityDeathEvent deathEvent = new EntityDeathEvent(this, this.getLoot(), deathMessage, this.getServer().getPlayers());
            this.getServer().getEventManager().call(deathEvent);

            for (ItemStack itemStack : deathEvent.getDrops()) {
                this.getWorld().addItemEntity(itemStack, this.getLocation());
            }

            if (deathEvent.getDeathMessage().isPresent()) {
                for (Player player : deathEvent.getRecipients()) {
                    player.sendMessage(deathEvent.getDeathMessage().get());
                }
            }
        }
    }

    @Override
    public void setShowDeathMessages(boolean enabled) {
        this.showDeathMessages = enabled;
    }

    @Override
    public boolean showDeathMessages() {
        return this.showDeathMessages;
    }

    protected Optional<TextMessage> getDeathMessage() {
        if (this.lastDamageEvent == null) {
            return Optional.empty();
        }

        TextMessage.Builder textBuilder = new TextMessage.Builder()
                .setType(TextType.TRANSLATION)
                .setTranslationRequired(true)
                .addParameter(this.getName());

        if (this.lastDamageEvent instanceof EntityDamageByEntityEvent) {
            textBuilder.addParameter(((EntityDamageByEntityEvent) this.lastDamageEvent).getAttacker().getName());
        }

        switch (this.lastDamageEvent.getCause()) {
            case ANVIL:
                textBuilder.setMessage("death.attack.anvil");
                break;
            case ATTACK:
                textBuilder.setMessage("death.attack.player");
                break;
            case BLOCK_EXPLOSION:
                textBuilder.setMessage("death.attack.explosion");
                break;
            case CONTACT:
                // TODO: verify this is a cactus attack
                textBuilder.setMessage("death.attack.cactus");
                break;
            case DROWNING:
                textBuilder.setMessage("death.attack.drown");
                break;
            case ENTITY_EXPLOSION:
                textBuilder.setMessage("death.attack.explosion.player");
                break;
            case FALL:
                textBuilder.setMessage("death.attack.fall");
                break;
            case FALLING_BLOCK:
                textBuilder.setMessage("death.attack.fallingBlock");
                break;
            case FIRE:
                textBuilder.setMessage("death.attack.inFire");
                break;
            case FIRE_TICK:
                textBuilder.setMessage("death.attack.onFire");
                break;
            case FLY_INTO_WALL:
                textBuilder.setMessage("death.attack.flyIntoWall");
                break;
            case LAVA:
                textBuilder.setMessage("death.attack.lava");
                break;
            case MAGIC:
                textBuilder.setMessage("death.attack.magic");
                break;
            case PROJECTILE:
                textBuilder.setMessage("death.attack.arrow");
                break;
            case STALACTITE:
                textBuilder.setMessage("death.attack.stalactite");
                break;
            case STALAGMITE:
                textBuilder.setMessage("death.attack.stalagmite");
                break;
            case STARVE:
                textBuilder.setMessage("death.attack.starve");
                break;
            case SUFFOCATION:
                textBuilder.setMessage("death.attack.inWall");
                break;
            case THORNS:
                textBuilder.setMessage("death.attack.thorns");
                break;
            case VOID:
                textBuilder.setMessage("death.attack.outOfWorld");
                break;
            case WITHER:
                textBuilder.setMessage("death.attack.wither");
                break;
            default:
                textBuilder.setMessage("death.attack.generic");
                break;
        }

        return Optional.of(textBuilder.build());
    }

    protected void startDeathAnimation() {
        EntityEventPacket deathEventPacket = new EntityEventPacket();
        deathEventPacket.setRuntimeEntityId(this.getId());
        deathEventPacket.setType(EntityEventType.DEATH);

        for (Player player : this.getViewers()) {
            player.sendPacket(deathEventPacket);
        }
    }

    protected void endDeathAnimation() {
        EntityEventPacket smokeDeathPacket = new EntityEventPacket();
        smokeDeathPacket.setRuntimeEntityId(this.getId());
        smokeDeathPacket.setType(EntityEventType.DEATH_SMOKE_CLOUD);

        for (Player player : this.getViewers()) {
            player.sendPacket(smokeDeathPacket);
        }
    }

    public void moveTo(float x, float y, float z) {
        this.moveUpdate = true;

        ImplChunk currentChunk = this.getChunk();
        this.setPosition(this.getWorld(), x, y, z);

        ImplChunk newChunk = this.getWorld().getChunk((int) Math.floor(this.x / 16), (int) Math.floor(this.z / 16));
        if (!currentChunk.equals(newChunk)) {   // spawn entity in new chunk and remove from old chunk
            currentChunk.removeEntity(this);
            newChunk.addEntity(this);
        }
    }

    @Override
    public void tick() {
        this.physicsEngine.tick();

        if (this.moveUpdate) {
            this.moveUpdate = false;
            this.sendMovementPacket();
        }

        if (this.metaDataUpdate) {
            this.metaDataUpdate = false;
            SetEntityDataPacket entityDataPacket = new SetEntityDataPacket();
            entityDataPacket.setRuntimeId(this.getId());
            entityDataPacket.setData(this.getMetaData());
            for (Player player : this.getViewers()) {
                player.sendPacket(entityDataPacket);
            }
        }

        if (this.getNoHitTicks() > 0) {
            this.setNoHitTicks(this.getNoHitTicks() - 1);
        } else if (this.getFireTicks() > 0) {
            this.setFireTicks(this.getFireTicks() - 1);
            // TODO: Do fire damage
        }

        if (this.getHealth() <= this.getAttribute(AttributeType.HEALTH).getMinimumValue() && this.isVulnerable()) {
            this.kill();
        }
        if (this.deathAnimationTicks != -1 && --this.deathAnimationTicks <= 0) {
            this.endDeathAnimation();
            this.despawn();
        }
    }

    protected void sendMovementPacket() {
        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();
        moveEntityPacket.setEntityRuntimeId(this.getId());
        moveEntityPacket.setPosition(this.getLocation());
        moveEntityPacket.setPitch(this.getPitch());
        moveEntityPacket.setYaw(this.getYaw());
        moveEntityPacket.setHeadYaw(this.getHeadYaw());
        moveEntityPacket.addFlag(MoveEntityAbsolutePacket.Flag.TELEPORT);
        for (Player player : this.getViewers()) {
            player.sendPacket(moveEntityPacket);
        }
    }

    /**
     * Damage this entity.
     * The event should be nothing more than the raw damage caused by the damage type.
     * It should not take into consideration armour points/effects as they are handled in this method.
     * @param event damage event
     * @return if the damage went through
     */
    public boolean damage(EntityDamageEvent event) {
        if (!this.isVulnerable()
                || this.getHealth() < this.getAttribute(AttributeType.HEALTH).getMinimumValue()
                || (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getAttacker().equals(this))) {
            return false;
        }

        float finalDamage = event.getDamage();
        String attackSound = null;  // TODO: attack sound

        if (this.getNoHitTicks() > 0) {
            return false;
        }

        // Apply armour defense points
        switch (event.getCause()) {
            case ANVIL:
            case ATTACK:
            case BLOCK_EXPLOSION:
            case CONTACT:
            case ENTITY_EXPLOSION:
            case FALL:
            case PROJECTILE:
            case STALACTITE:
            case STALAGMITE:
                int armourPoints = this.getArmourPoints();
                finalDamage = finalDamage * Math.max(0, 1 - armourPoints * 0.04f);
                break;
        }

        EntityFilterData damageFilterData;
        if (event instanceof EntityDamageByEntityEvent) {
            damageFilterData = new EntityFilterData.Builder()
                    .setSelf(this)
                    .setOther(((EntityDamageByEntityEvent) event).getAttacker())
                    .build();
        } else {
            damageFilterData = new EntityFilterData.Builder()
                    .setSelf(this)
                    .build();
        }

        EntityDamageSensorComponent damageSensorComponent = this.getComponent(EntityDamageSensorComponent.class);
        if (damageSensorComponent.getSensors().length > 0) {
            // Check entity's damage sensors to ensure we can attack the entity
            for (EntityDamageSensorComponent.Sensor sensor : damageSensorComponent.getSensors()) {
                boolean isAttackSensor = sensor.getCause().filter(cause -> cause.equals(event.getCause())).isPresent() || !sensor.getCause().isPresent();

                if (isAttackSensor) {
                    boolean passedFilters = false;
                    for (EntityFilter filter : sensor.getFilters()) {
                        if (filter.test(damageFilterData)) {
                            if (!sensor.dealsDamage()) {
                                return false;
                            }

                            finalDamage *= sensor.getDamageMultiplier();
                            finalDamage += sensor.getDamageModifier();
                            attackSound = sensor.getSound().orElse(null);

                            event.setDamage(finalDamage);

                            this.getServer().getEventManager().call(event);
                            if (event.isCancelled()) {
                                return false;
                            }
                            this.lastDamageEvent = event;

                            if (filter.getTriggerEventId().isPresent()) {
                                this.getEntityDefinition().getEvent(filter.getTriggerEventId().get()).trigger(this);
                            }

                            this.setNoHitTicks(event.getNoHitTicks());
                            this.hurt(event.getDamage());
                            passedFilters = true;
                            break;
                        }
                    }
                    if (!passedFilters) {
                        return false;
                    }
                }
            }
        } else {
            // No damage sensors preventing the entity from attacking the entity.
            this.getServer().getEventManager().call(event);
            if (event.isCancelled()) {
                return false;
            }
            this.lastDamageEvent = event;

            this.setNoHitTicks(event.getNoHitTicks());
            this.hurt(event.getDamage());
        }

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
            Entity attacker = damageByEntityEvent.getAttacker();

            Vector2 directionVector = new Vector2(this.getX() - attacker.getX(), this.getZ() - attacker.getZ()).normalize();
            Vector3 knockback = damageByEntityEvent.getKnockback().multiply(directionVector.getX(), 1, directionVector.getY());
            this.setVelocity(knockback);
        }

        return true;
    }

    /**
     * Called when the entity is initially spawned into a world.
     * This is useful for entity initialization.
     */
    public void onSpawned() {
        this.spawned = true;
    }

    /**
     * Called when the entity is about to be despawned.
     */
    public void onDespawned() {
        this.spawned = false;
    }

    public boolean withinEntityRenderDistanceTo(Player player) {
        int chunkDistanceToViewer = (int) Math.round(Math.sqrt(Math.pow(player.getChunk().getX() - this.getChunk().getX(), 2) + Math.pow(player.getChunk().getZ() - this.getChunk().getZ(), 2)));
        return chunkDistanceToViewer < this.getWorld().getServer().getConfig().getEntityChunkRenderDistance();
    }

    public boolean canBeSpawnedTo(Player player) {
        return !this.equals(player)
                && !this.hasSpawnedTo(player)
                && !this.isHiddenFrom(player)
                && this.withinEntityRenderDistanceTo(player)
                && this.getChunk().canBeVisibleTo(player);
    }

    public boolean shouldBeDespawnedFrom(Player player) {
        return (!this.hasSpawned() || !this.getChunk().canBeVisibleTo(player) || !this.withinEntityRenderDistanceTo(player))
                && this.hasSpawnedTo(player);
    }

    @Override
    public boolean hasSpawned() {
        return this.spawned;
    }

    @Override
    public boolean hasSpawnedTo(Player player) {
        return this.spawnedTo.contains(player);
    }

    @Override
    public boolean spawnTo(Player player) {
        if (this.canBeSpawnedTo(player)) {
            this.spawnedTo.add(player);

            AddEntityPacket addEntityPacket = new AddEntityPacket();
            addEntityPacket.setEntityUniqueId(this.getId());
            addEntityPacket.setEntityRuntimeId(this.getId());
            addEntityPacket.setEntityType(this.getEntityDefinition().getId());
            addEntityPacket.setPosition(this.getLocation());
            addEntityPacket.setVelocity(this.getVelocity());
            addEntityPacket.setPitch(this.getPitch());
            addEntityPacket.setYaw(this.getYaw());
            addEntityPacket.setHeadYaw(this.getHeadYaw());
            addEntityPacket.setMetaData(this.getMetaData());
            player.sendPacket(addEntityPacket);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean despawnFrom(Player player) {
        if (this.spawnedTo.remove(player)) {
            RemoveEntityPacket entityPacket = new RemoveEntityPacket();
            entityPacket.setUniqueEntityId(this.getId());
            player.sendPacket(entityPacket);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void despawn() {
        this.getWorld().removeEntity(this);
    }

    @Override
    public void showTo(Player player) {
        this.hiddenFrom.remove(player);
        if (this.getChunk().canBeVisibleTo(player) && this.withinEntityRenderDistanceTo(player) && !this.hasSpawnedTo(player)) {
            this.spawnTo(player);
        }
    }

    @Override
    public void hideFrom(Player player) {
        this.hiddenFrom.add(player);
        if (this.getViewers().contains(player)) {
            this.despawnFrom(player);
        }
    }

    @Override
    public boolean isHiddenFrom(Player player) {
        return this.hiddenFrom.contains(player);
    }

    @Override
    public Set<Player> getViewers() {
        return new HashSet<>(this.spawnedTo);
    }

    @Override
    public int hashCode() {
        return 43 * (int) this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImplEntity) {
            return NumberUtils.isNearlyEqual(((ImplEntity) obj).getId(), this.getId());
        }
        return false;
    }
}
