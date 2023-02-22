package io.github.pizzaserver.server.entity;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityEventType;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityHelper;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.entity.data.DamageCause;
import io.github.pizzaserver.api.entity.data.attributes.AttributeView;
import io.github.pizzaserver.api.entity.data.attributes.AttributeType;
import io.github.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.filter.EntityFilter;
import io.github.pizzaserver.api.entity.definition.components.filter.EntityFilterData;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityBreathableComponent;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityBurnsInDaylightComponent;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityDamageSensorComponent;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityDeathMessageComponent;
import io.github.pizzaserver.api.inventory.EntityInventory;
import io.github.pizzaserver.api.entity.meta.EntityMetadata;
import io.github.pizzaserver.api.event.type.entity.EntityDamageByEntityEvent;
import io.github.pizzaserver.api.event.type.entity.EntityDamageEvent;
import io.github.pizzaserver.api.event.type.entity.EntityDeathEvent;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.descriptors.ArmorItem;
import io.github.pizzaserver.api.keychain.EntityKeys;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.*;
import io.github.pizzaserver.commons.data.DataAction;
import io.github.pizzaserver.commons.data.key.DataKey;
import io.github.pizzaserver.commons.data.store.DataStore;
import io.github.pizzaserver.commons.data.store.SingleDataStore;
import io.github.pizzaserver.commons.data.value.Preprocessors;
import io.github.pizzaserver.commons.utils.NumberUtils;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.entity.boss.ImplBossBar;
import io.github.pizzaserver.server.inventory.ImplEntityInventory;
import io.github.pizzaserver.server.item.ItemUtils;
import io.github.pizzaserver.server.level.ImplLevel;
import io.github.pizzaserver.server.level.world.chunks.ImplChunk;

import java.util.*;

public class ImplEntity extends SingleDataStore implements Entity {

    public static long ID = 1;
    public static final int NO_HIT_TICKS = 10;


    protected final long id;

    protected boolean moveUpdate;
    protected EntityPhysicsEngine physicsEngine = new EntityPhysicsEngine(this);
    protected long ticks;

    protected BlockLocation home = null;

    protected int deathAnimationTicks = -1;
    protected int noHitTicks;

    protected int fireTicks;

    protected ImplBossBar bossBar = null;


    protected BoundingBox boundingBox = new BoundingBox(Vector3f.ZERO, Vector3f.ZERO);
    protected float eyeHeight;
    protected float baseOffset;
    protected boolean pistonPushable;
    protected boolean pushable;

    protected final EntityDefinition entityDefinition;
    protected final LinkedList<EntityComponentGroup> componentGroups = new LinkedList<>();

    protected EntityInventory inventory = new ImplEntityInventory(this, ContainerType.INVENTORY, 0);
    protected List<Item> loot = new ArrayList<>();
    protected EntityMetadata metaData = new ImplEntityMetadata(this);
    protected boolean metaDataUpdate;

    protected EntityDamageEvent lastDamageEvent = null;
    protected boolean showDeathMessages;

    protected boolean spawned;
    protected final Set<Player> spawnedTo = new HashSet<>();
    protected final Set<Player> hiddenFrom = new HashSet<>();


    // Attributes are hidden from the API - all of them should be passed through the entity data store.
    // AttributeViews are only used to package attribs to and from packets.
    protected Map<DataKey<? extends Number>, AttributeView<? extends Number>> bakedAttributeViews = Collections.unmodifiableMap(new HashMap<>());


    @SuppressWarnings({"rawtypes", "unchecked"})
    public ImplEntity(EntityDefinition entityDefinition) {
        this.id = ID++;
        this.entityDefinition = entityDefinition;

        // Anything that previously triggered a movement update should trigger this.
        Runnable movementUpdateTrigger = () -> this.moveUpdate = true;

        this.getOrCreateContainerFor(EntityKeys.POSITION, Vector3f.ZERO).setPreprocessor(Preprocessors.nonNull("Entity Position")).listenFor(DataAction.VALUE_PRE_SET, movementUpdateTrigger);
        this.getOrCreateContainerFor(EntityKeys.ROTATION_PITCH, 0f).setPreprocessor(Preprocessors.TRANSFORM_NULL_TO_FLOAT_ZERO).listenFor(DataAction.VALUE_PRE_SET, movementUpdateTrigger);
        this.getOrCreateContainerFor(EntityKeys.ROTATION_YAW, 0f).setPreprocessor(Preprocessors.TRANSFORM_NULL_TO_FLOAT_ZERO).listenFor(DataAction.VALUE_PRE_SET, movementUpdateTrigger);
        this.getOrCreateContainerFor(EntityKeys.ROTATION_HEAD_PITCH, 0f).setPreprocessor(Preprocessors.TRANSFORM_NULL_TO_FLOAT_ZERO).listenFor(DataAction.VALUE_PRE_SET, movementUpdateTrigger);
        this.getOrCreateContainerFor(EntityKeys.ROTATION_HEAD_YAW, 0f).setPreprocessor(Preprocessors.TRANSFORM_NULL_TO_FLOAT_ZERO).listenFor(DataAction.VALUE_PRE_SET, movementUpdateTrigger);
        this.getOrCreateContainerFor(EntityKeys.ROTATION_HEAD_ROLL, 0f).setPreprocessor(Preprocessors.TRANSFORM_NULL_TO_FLOAT_ZERO).listenFor(DataAction.VALUE_PRE_SET, movementUpdateTrigger);

        this.getOrCreateContainerFor(EntityKeys.IS_VULNERABLE, true).setPreprocessor(Preprocessors.ifNullThenConstant(true));

        // Apply default components to the entity
        Server.getInstance().getEntityRegistry().getComponentClasses().forEach(clazz -> {
            EntityComponentHandler handler = Server.getInstance().getEntityRegistry().getComponentHandler(clazz);
            handler.onRegistered(this, Server.getInstance().getEntityRegistry().getDefaultComponent(clazz));
        });

        this.getOrCreateContainerFor(EntityKeys.KILL_THRESHOLD, 0f)
                .setPreprocessor(Preprocessors.inOrder(
                        Preprocessors.nonNull("Kill threshold must not be null and must be above or equal to zero."),
                        Preprocessors.FLOAT_EQUAL_OR_ABOVE_ZERO
                )); // No need for a listenFor as kill conditions are checked during tick()

        this.getOrCreateContainerFor(EntityKeys.MAX_HEALTH, 1f)
                .setPreprocessor(Preprocessors.inOrder(
                        Preprocessors.nonNull("Max Health must not be null and must be above or equal to zero."),
                        Preprocessors.ensureAboveDefined(this.expectProxy(EntityKeys.KILL_THRESHOLD))
                ))
                .listenFor(DataAction.VALUE_SET, newMaxHealth -> {
                    float health = this.expect(EntityKeys.HEALTH);
                    float maxHealth = (float) newMaxHealth;
                    if (health > maxHealth)
                        this.set(EntityKeys.HEALTH, maxHealth);
                });

        this.getOrCreateContainerFor(EntityKeys.HEALTH, this.expect(EntityKeys.MAX_HEALTH))
                .setPreprocessor(Preprocessors.inOrder(
                        Preprocessors.ifNullThenValue(() -> this.expect(EntityKeys.MAX_HEALTH)),
                        Preprocessors.ensureBelowDefined(this.expectProxy(EntityKeys.MAX_HEALTH)),
                        Preprocessors.FLOAT_EQUAL_OR_ABOVE_ZERO
                ))
                .listenFor(DataAction.VALUE_SET, () -> {
                    if (this.getBossBar().isPresent()) {
                        BossBar bossBar = this.getBossBar().get();
                        bossBar.setPercentage(this.expect(EntityKeys.HEALTH) / this.expect(EntityKeys.MAX_HEALTH));
                        this.setBossBar(bossBar);
                    }
                });

        this.getOrCreateContainerFor(EntityKeys.MAX_ABSORPTION, 0f)
                .setPreprocessor(Preprocessors.inOrder(
                        Preprocessors.nonNull("Max Absorption must not be null and must be above or equal to zero."),
                        Preprocessors.FLOAT_EQUAL_OR_ABOVE_ZERO
                ))
                .listenFor(DataAction.VALUE_SET, newMaxAbsorption -> {
                    float absorption = this.expect(EntityKeys.ABSORPTION);
                    float maxAbsorption = (float) newMaxAbsorption;
                    if (absorption > maxAbsorption)
                        this.set(EntityKeys.ABSORPTION, maxAbsorption);
                });

        this.getOrCreateContainerFor(EntityKeys.ABSORPTION, this.expect(EntityKeys.MAX_ABSORPTION))
                .setPreprocessor(Preprocessors.inOrder(
                        Preprocessors.ifNullThenValue(() -> this.expect(EntityKeys.MAX_ABSORPTION)),
                        Preprocessors.ensureBelowDefined(this.expectProxy(EntityKeys.MAX_ABSORPTION)),
                        Preprocessors.FLOAT_EQUAL_OR_ABOVE_ZERO
                ));

        this.getOrCreateContainerFor(EntityKeys.MOVEMENT_SPEED, 0.1f)
                .setPreprocessor(Preprocessors.inOrder(
                        Preprocessors.TRANSFORM_NULL_TO_FLOAT_ZERO,
                        Preprocessors.FLOAT_EQUAL_OR_ABOVE_ZERO
                ));


        this.listenFor(DataStore.ACTION_CREATE_CONTAINER, key -> {
                if(AttributeType.ALL_ATTRIBUTE_KEY_DEPENDENCIES.contains(key))
                    this.generateAttributeReferences();
        });
    }


    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public EntityDefinition getEntityDefinition() {
        return this.entityDefinition;
    }

    public final Map<DataKey<? extends Number>, AttributeView<? extends Number>> generateAttributeReferences() {
        if(this.bakedAttributeViews == null)
            this.bakedAttributeViews = EntityHelper.generateAttributes(this, AttributeType.ALL_ATTRIBUTES);

        return this.bakedAttributeViews;
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
            EntityComponentHandler handler = this.getServer().getEntityRegistry().getComponentHandler(newComponentClazz);

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
                EntityComponentHandler handler = this.getServer().getEntityRegistry().getComponentHandler(removeComponentClazz);

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

        return this.getServer().getEntityRegistry().getDefaultComponent(componentClazz);
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
        return this.expect(EntityKeys.POSITION).getX();
    }

    @Override
    public float getY() {
        return this.expect(EntityKeys.POSITION).getY();
    }

    @Override
    public float getZ() {
        return this.expect(EntityKeys.POSITION).getZ();
    }

    @Override
    public int getFloorX() {
        return (int) Math.floor(this.getX());
    }

    @Override
    public int getFloorY() {
        return (int) Math.floor(this.getY());
    }

    @Override
    public int getFloorZ() {
        return (int) Math.floor(this.getZ());
    }

    @Override
    public boolean isOnGround() {
        BoundingBox boundingBox = this.getBoundingBox().grow(0.5f);
        int minBlockXCheck = (int) boundingBox.getMinX();
        int minBlockZCheck = (int) boundingBox.getMinZ();
        int maxBlockXCheck = (int) boundingBox.getMaxX();
        int maxBlockZCheck = (int) boundingBox.getMaxZ();

        BoundingBox intersectingBoundingBox = this.getBoundingBox().translate(0, -0.0002f, 0);
        World world = this.expect(EntityKeys.WORLD);

        for (int x = minBlockXCheck; x <= maxBlockXCheck; x++) {
            for (int z = minBlockZCheck; z <= maxBlockZCheck; z++) {
                Block blockBelow = world.getBlock(x, this.getFloorY() - 1, z);
                if (blockBelow.hasCollision() && blockBelow.getBoundingBox().collidesWith(intersectingBoundingBox)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Set<Block> getCollisionBlocks() {
        BoundingBox entityBoundingBox = this.getBoundingBox().grow(0.5f);
        int minBlockXCheck = (int) entityBoundingBox.getMinX();
        int minBlockYCheck = (int) entityBoundingBox.getMinY();
        int minBlockZCheck = (int) entityBoundingBox.getMinZ();
        int maxBlockXCheck = (int) entityBoundingBox.getMaxX();
        int maxBlockYCheck = (int) entityBoundingBox.getMaxY();
        int maxBlockZCheck = (int) entityBoundingBox.getMaxZ();

        Set<Block> collidingBlocks = new HashSet<>();
        World world = this.expect(EntityKeys.WORLD);

        for (int x = minBlockXCheck; x <= maxBlockXCheck; x++) {
            for (int y = minBlockYCheck; y <= maxBlockYCheck; y++) {
                for (int z = minBlockZCheck; z <= maxBlockZCheck; z++) {
                    Block block = world.getBlock(x, y, z);
                    if (block.getBoundingBox().collidesWith(entityBoundingBox)) {
                        collidingBlocks.add(block);
                    }
                }
            }
        }

        return collidingBlocks;
    }

    @Override
    public Block getHeadBlock() {
        World world = this.expect(EntityKeys.WORLD);
        Vector3i blockLocation = this.isSwimming()
                ? this.getLocation().toVector3i()
                : this.getLocation().toVector3f().add(0, this.getEyeHeight(), 0).floor().toInt();

        return world.getBlock(blockLocation);
    }

    @Override
    public void setHome(BlockLocation home) {
        this.home = home;
    }

    @Override
    public Optional<BlockLocation> getHome() {
        return Optional.ofNullable(this.home);
    }

    @Override
    public Location getLocation() {
        return new Location(
                this.get(EntityKeys.WORLD).orElse(null),
                this.expect(EntityKeys.POSITION),
                EntityHelper.getBasicRotationFor(this)
        );
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    protected void recalculateBoundingBox() {
        float minX = this.getX() - ((this.getWidth() / 2) * this.getScale());
        float maxX = this.getX() + ((this.getWidth() / 2) * this.getScale());
        float minY = this.getY();
        float maxY = this.getY() + (this.getHeight() * this.getScale());
        float minZ = this.getZ() - ((this.getWidth() / 2) * this.getScale());
        float maxZ = this.getZ() + ((this.getWidth() / 2) * this.getScale());
        this.boundingBox = new BoundingBox(Vector3f.from(minX, minY, minZ), Vector3f.from(maxX, maxY, maxZ));
    }

    @Override
    public float getEyeHeight() {
        return this.eyeHeight;
    }

    @Override
    public void setEyeHeight(float eyeHeight) {
        this.eyeHeight = eyeHeight;
    }

    @Override
    public float getBaseOffset() {
        return this.baseOffset;
    }

    @Override
    public void setBaseOffset(float offset) {
        this.baseOffset = offset;
    }

    @Override
    public String getName() {
        return this.getDisplayName().orElse(this.getEntityDefinition().getName());
    }

    @Override
    public float getHeight() {
        return this.getMetaData().getFloat(EntityData.BOUNDING_BOX_HEIGHT);
    }

    @Override
    public void setHeight(float height) {
        this.getMetaData().putFloat(EntityData.BOUNDING_BOX_HEIGHT, height);
        this.recalculateBoundingBox();
    }

    @Override
    public float getWidth() {
        return this.getMetaData().getFloat(EntityData.BOUNDING_BOX_WIDTH);
    }

    @Override
    public void setWidth(float width) {
        this.getMetaData().putFloat(EntityData.BOUNDING_BOX_WIDTH, width);
        this.recalculateBoundingBox();
    }

    /**
     * Set the internal position of the entity.
     * Used internally to set up and to clean up the entity
     * @param location entity location
     */
    public void setPosition(Location location) {
        if (location != null) {
            this.setPosition(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), location.getHeadYaw());
        } else {
            this.setPosition(null, 0, 0, 0, 0, 0, 0);
        }
    }

    public void setPosition(World world, float x, float y, float z, float pitch, float yaw, float headYaw) {
        this.set(EntityKeys.WORLD, world);
        this.set(EntityKeys.POSITION, Vector3f.from(x, y, z));
        this.set(EntityKeys.ROTATION_PITCH, pitch);
        this.set(EntityKeys.ROTATION_YAW, yaw);
        this.set(EntityKeys.ROTATION_HEAD_YAW, headYaw);
        this.recalculateBoundingBox();
    }

    @Override
    public void teleport(World world, float x, float y, float z, float pitch, float yaw, float headYaw) {
        World oldWorld = this.expect(EntityKeys.WORLD);
        this.moveUpdate = true;

        if (!world.equals(oldWorld)) {
            this.set(EntityKeys.ROTATION_PITCH, pitch);
            this.set(EntityKeys.ROTATION_YAW, yaw);
            this.set(EntityKeys.ROTATION_HEAD_YAW, headYaw);

            oldWorld.removeEntity(this);
            world.addEntity(this, Vector3f.from(x, y, z));
        } else {
            this.setPosition(new Location(world, x, y, z, pitch, yaw, headYaw));
        }
    }

    @Override
    public Vector3f getMotion() {
        return this.physicsEngine.getMotion();
    }

    @Override
    public void setMotion(Vector3f motion) {
        this.physicsEngine.setMotion(motion);

        SetEntityMotionPacket motionPacket = new SetEntityMotionPacket();
        motionPacket.setMotion(this.getMotion());
        motionPacket.setRuntimeEntityId(this.getId());
        for (Player player : this.getViewers()) {
            player.sendPacket(motionPacket);
        }
    }

    @Override
    public Optional<String> getDisplayName() {
        return Optional.ofNullable(this.getMetaData().getString(EntityData.NAMETAG));
    }

    @Override
    public void setDisplayName(String name) {
        this.getMetaData().putString(EntityData.NAMETAG, name);
    }

    @Override
    public HorizontalDirection getHorizontalDirection() {
        return HorizontalDirection.fromYaw(this.get(EntityKeys.ROTATION_YAW).orElse(0f));
    }

    @Override
    public boolean isAlive() {
        return this.expect(EntityKeys.HEALTH) > this.get(EntityKeys.KILL_THRESHOLD).orElse(0f);
    }

    @Override
    public Vector3f getDirectionVector() {
        float pitch = this.get(EntityKeys.ROTATION_PITCH).orElse(0f);
        float yaw = this.get(EntityKeys.ROTATION_PITCH).orElse(0f);

        double cosPitch = Math.cos(Math.toRadians(pitch));
        double x = Math.sin(Math.toRadians(yaw)) * -cosPitch;
        double y = -Math.sin(Math.toRadians(pitch));
        double z = Math.cos(Math.toRadians(yaw)) * cosPitch;

        return Vector3f.from((float) x, (float) y, (float) z).normalize();
    }

    @Override
    public ImplChunk getChunk() {
        return (ImplChunk) this.getLocation().getChunk();
    }

    @Override
    public ImplLevel getLevel() {
        return (ImplLevel) this.getLocation().getLevel();
    }

    @Override
    public ImplServer getServer() {
        return (ImplServer) Server.getInstance();
    }

    @Override
    public ImplEntityMetadata getMetaData() {
        return (ImplEntityMetadata) this.metaData;
    }

    @Override
    public boolean hasGravity() {
        return this.getMetaData().hasFlag(EntityFlag.HAS_GRAVITY);
    }

    @Override
    public void setGravity(boolean enabled) {
        this.getMetaData().putFlag(EntityFlag.HAS_GRAVITY, enabled);
    }

    @Override
    public boolean hasCollision() {
        return this.getMetaData().hasFlag(EntityFlag.HAS_COLLISION);
    }

    @Override
    public void setCollision(boolean canCollide) {
        this.getMetaData().putFlag(EntityFlag.HAS_COLLISION, canCollide);
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
        return !this.getMetaData().hasFlag(EntityFlag.NO_AI);
    }

    @Override
    public void setAI(boolean hasAI) {
        this.getMetaData().putFlag(EntityFlag.NO_AI, !hasAI);
    }

    @Override
    public float getScale() {
        return this.getMetaData().getFloat(EntityData.SCALE);
    }

    @Override
    public void setScale(float scale) {
        this.getMetaData().putFloat(EntityData.SCALE, scale);
        this.recalculateBoundingBox();
    }

    @Override
    public boolean isSneaking() {
        return this.getMetaData().hasFlag(EntityFlag.SNEAKING);
    }

    @Override
    public void setSneaking(boolean sneaking) {
        this.getMetaData().putFlag(EntityFlag.SNEAKING, sneaking);
    }

    @Override
    public boolean isSwimming() {
        return this.getMetaData().hasFlag(EntityFlag.SWIMMING);
    }

    @Override
    public void setSwimming(boolean swimming) {
        this.getMetaData().putFlag(EntityFlag.SWIMMING, swimming);
    }

    @Override
    public boolean isSprinting() {
        return this.getMetaData().hasFlag(EntityFlag.SPRINTING);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        this.getMetaData().putFlag(EntityFlag.SPRINTING, sprinting);
    }

    @Override
    public int getFireTicks() {
        return this.fireTicks;
    }

    @Override
    public void setFireTicks(int ticks) {
        boolean onFirePreviously = this.getFireTicks() > 0;
        this.fireTicks = ticks;

        EntityMetadata metaData = this.getMetaData();
        if (this.fireTicks > 0 && !onFirePreviously) {
            metaData.putFlag(EntityFlag.ON_FIRE, true);
        } else if (this.fireTicks <= 0 && onFirePreviously) {
            metaData.putFlag(EntityFlag.ON_FIRE, false);
        }
    }

    @Override
    public int getAirSupplyTicks() {
        return this.getMetaData().getShort(EntityData.AIR_SUPPLY);
    }

    @Override
    public void setAirSupplyTicks(int ticks) {
        int newAirSupplyTicks = Math.max(0, Math.min(ticks, this.getMaxAirSupplyTicks()));
        if (newAirSupplyTicks != this.getAirSupplyTicks()) {
            this.getMetaData().putShort(EntityData.AIR_SUPPLY, (short) newAirSupplyTicks);
        }
    }

    @Override
    public int getMaxAirSupplyTicks() {
        return this.getMetaData().getShort(EntityData.MAX_AIR_SUPPLY);
    }

    @Override
    public void setMaxAirSupplyTicks(int ticks) {
        int newMaxAirSupplyTicks = Math.max(0, ticks);
        if (newMaxAirSupplyTicks != this.getMaxAirSupplyTicks()) {
            this.getMetaData().putShort(EntityData.MAX_AIR_SUPPLY, (short) newMaxAirSupplyTicks);

            this.setAirSupplyTicks(Math.min(this.getAirSupplyTicks(), this.getMaxAirSupplyTicks()));
        }
    }

    @Override
    public List<Item> getLoot() {
        return this.loot;
    }

    @Override
    public void setLoot(List<Item> loot) {
        this.loot = loot;
    }

    @Override
    public EntityInventory getInventory() {
        return this.inventory;
    }

    @Override
    public Optional<BossBar> getBossBar() {
        return Optional.ofNullable(this.bossBar);
    }

    @Override
    public void setBossBar(BossBar bossBar) {
        if (bossBar != null) {
            if (((ImplBossBar) bossBar).getEntityId() != -1) {
                throw new IllegalArgumentException("The boss bar is already assigned to an entity.");
            }
            ((ImplBossBar) bossBar).setEntityId(this.getId());
            ((ImplBossBar) bossBar).setDummy(false);
        }

        if (this.getBossBar().isPresent()) {
            // Removing boss bar
            for (Player viewer : this.bossBar.getViewers()) {
                this.bossBar.despawnFrom(viewer);
            }
        }
        this.bossBar = (ImplBossBar) bossBar;
    }

    protected void updateBossBarVisibility() {
        if (this.getBossBar().isPresent()) {
            for (Player viewer : this.getViewers()) {
                if (viewer.getLocation().toVector3f().distance(this.getLocation().toVector3f()) <= this.getBossBar().get().getRenderRange()) {
                    // In range
                    this.bossBar.spawnTo(viewer);
                } else {
                    // Out of range
                    this.bossBar.despawnFrom(viewer);
                }
            }
        }
    }

    @Override
    public int getArmourPoints() {
        int armourPoints = 0;

        if (this.getInventory().getHelmet() instanceof ArmorItem) {
            armourPoints += ((ArmorItem) this.getInventory().getHelmet()).getProtection();
        }
        if (this.getInventory().getChestplate() instanceof ArmorItem) {
            armourPoints += ((ArmorItem) this.getInventory().getChestplate()).getProtection();
        }
        if (this.getInventory().getLeggings() instanceof ArmorItem) {
            armourPoints += ((ArmorItem) this.getInventory().getLeggings()).getProtection();
        }
        if (this.getInventory().getBoots() instanceof ArmorItem) {
            armourPoints += ((ArmorItem) this.getInventory().getBoots()).getProtection();
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
        this.set(EntityKeys.HEALTH, this.expect(EntityKeys.HEALTH) - damage);
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
            this.set(EntityKeys.HEALTH, 0f);
            this.setAI(false);

            this.deathAnimationTicks = 20;
            this.startDeathAnimation();

            TextMessage deathMessage = null;
            if (this.getComponent(EntityDeathMessageComponent.class).showDeathMessages()) {
                deathMessage = this.getDeathMessage().orElse(null);
            }

            EntityDeathEvent deathEvent = new EntityDeathEvent(this, this.getLoot(), deathMessage, this.getServer().getPlayers());
            this.getServer().getEventManager().call(deathEvent);

            for (Item itemStack : deathEvent.getDrops()) {
                this.get(EntityKeys.WORLD).ifPresentOrElse(
                        world -> world.addItemEntity(itemStack, this.getLocation().toVector3f()),
                        () -> Server.getInstance()
                                .getLogger()
                                .warn(String.format("Attempted to add an item entity on behalf of an entity with a null world (ID: %s)", this.getId()))
                );
            }

            if (deathEvent.getDeathMessage().isPresent()) {
                for (Player player : deathEvent.getRecipients()) {
                    player.sendMessage(deathEvent.getDeathMessage().get());
                }
            }

            // Removes all data listeners, effectively marking this entity
            // as unused.
            this.stale();
        }
    }

    @Override
    public void setDeathMessageEnabled(boolean enabled) {
        this.showDeathMessages = enabled;
    }

    @Override
    public boolean isDeathMessageEnabled() {
        return this.showDeathMessages;
    }

    protected Optional<TextMessage> getDeathMessage() {
        if (this.lastDamageEvent == null) {
            return Optional.empty();
        }

        TextMessage.Builder textBuilder = new TextMessage.Builder()
                .setType(TextPacket.Type.TRANSLATION)
                .setTranslationRequired(true)
                .addParameter(this.getName());

        if (this.lastDamageEvent instanceof EntityDamageByEntityEvent) {
            textBuilder.addParameter(((EntityDamageByEntityEvent) this.lastDamageEvent).getAttacker().getName());
        }

        switch (this.lastDamageEvent.getCause()) {
            case ANVIL -> textBuilder.setMessage("death.attack.anvil");
            case ATTACK -> textBuilder.setMessage("death.attack.player");
            case BLOCK_EXPLOSION -> textBuilder.setMessage("death.attack.explosion");
            case CONTACT -> textBuilder.setMessage("death.attack.cactus");
            case DROWNING -> textBuilder.setMessage("death.attack.drown");
            case ENTITY_EXPLOSION -> textBuilder.setMessage("death.attack.explosion.player");
            case FALL -> textBuilder.setMessage("death.attack.fall");
            case FALLING_BLOCK -> textBuilder.setMessage("death.attack.fallingBlock");
            case FIRE -> textBuilder.setMessage("death.attack.inFire");
            case FIRE_TICK -> textBuilder.setMessage("death.attack.onFire");
            case FLY_INTO_WALL -> textBuilder.setMessage("death.attack.flyIntoWall");
            case LAVA -> textBuilder.setMessage("death.attack.lava");
            case MAGIC -> textBuilder.setMessage("death.attack.magic");
            case PROJECTILE -> textBuilder.setMessage("death.attack.arrow");
            case STALACTITE -> textBuilder.setMessage("death.attack.stalactite");
            case STALAGMITE -> textBuilder.setMessage("death.attack.stalagmite");
            case STARVE -> textBuilder.setMessage("death.attack.starve");
            case SUFFOCATION -> textBuilder.setMessage("death.attack.inWall");
            case THORNS -> textBuilder.setMessage("death.attack.thorns");
            case VOID -> textBuilder.setMessage("death.attack.outOfWorld");
            case WITHER -> textBuilder.setMessage("death.attack.wither");
            default -> textBuilder.setMessage("death.attack.generic");
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
        this.moveTo(
                x, y, z,
                this.get(EntityKeys.ROTATION_PITCH).orElse(0f),
                this.get(EntityKeys.ROTATION_YAW).orElse(0f),
                this.get(EntityKeys.ROTATION_HEAD_YAW).orElse(0f)
        );
    }

    public void moveTo(float x, float y, float z, float pitch, float yaw, float headYaw) {
        World world = this.expect(EntityKeys.WORLD);
        this.moveUpdate = true;

        Block blockBelow = world.getBlock(this.getFloorX(), this.getFloorY() - 1, this.getFloorZ());

        ImplChunk currentChunk = this.getChunk();
        this.setPosition(world, x, y, z, pitch, yaw, headYaw);

        Chunk newChunk = world.getChunk((int) Math.floor(this.getX() / 16), (int) Math.floor(this.getZ() / 16));
        if (!currentChunk.equals(newChunk)) {   // spawn entity in new chunk and remove from old chunk
            currentChunk.removeEntity(this);
            newChunk.addEntity(this);
        }

        Block newBlockBelow = world.getBlock(this.getFloorX(), this.getFloorY() - 1, this.getFloorZ());
        if (!blockBelow.getLocation().equals(newBlockBelow.getLocation())) {
            newBlockBelow.getBehavior().onWalkedOn(this, newBlockBelow);
            blockBelow.getBehavior().onWalkedOff(this, blockBelow);
        }
    }

    @Override
    public void tick() {
        this.physicsEngine.tick();

        if (this.moveUpdate) {
            this.moveUpdate = false;
            this.sendMovementPacket();
        }

        this.getMetaData().tryUpdate();

        World world = this.expect(EntityKeys.WORLD);

        if (this.hasComponent(EntityBurnsInDaylightComponent.class) && world.isDay()) {
            Block highestBlockAboveEntity = world.getHighestBlockAt(this.getFloorX(), this.getFloorZ());
            if (highestBlockAboveEntity.getY() <= this.getY()) {
                this.setFireTicks(20);
            }
        }

        if (this.getNoHitTicks() > 0) {
            this.setNoHitTicks(this.getNoHitTicks() - 1);
        } else {
            if (this.getFireTicks() > 0) {
                if (this.getFireTicks() % 20 == 0) {
                    EntityDamageEvent fireTickDamageEvent = new EntityDamageEvent(this, DamageCause.FIRE_TICK, 1f, NO_HIT_TICKS);
                    this.damage(fireTickDamageEvent);
                }
                this.setFireTicks(this.getFireTicks() - 1);
            }

            Block headBlock = this.getHeadBlock();
            EntityBreathableComponent breathableComponent = this.getComponent(EntityBreathableComponent.class);
            boolean isSuffocating = headBlock.hasCollision()
                    && headBlock.getBoundingBox().collidesWith(this.getBoundingBox())
                    && (!(breathableComponent.canBreathSolids() || breathableComponent.getBreathableBlocks().contains(headBlock))
                        || breathableComponent.getNonBreathableBlocks().contains(headBlock));
            if (isSuffocating) {
                if (this.ticks % breathableComponent.getSuffocationInterval() == 0) {
                    EntityDamageEvent suffocationEvent = new EntityDamageEvent(this, DamageCause.SUFFOCATION, 1f, 0);
                    this.damage(suffocationEvent);
                }
            }

            boolean losingOxygen = !headBlock.hasCollision()
                    && ((breathableComponent.getNonBreathableBlocks().contains(headBlock)
                                && !breathableComponent.getBreathableBlocks().contains(headBlock))
                        || !(headBlock.hasOxygen() || breathableComponent.getBreathableBlocks().contains(headBlock)));

            if (losingOxygen && !(this instanceof Player player && player.isCreativeMode())) {
                if (this.getAirSupplyTicks() <= 0 && this.getServer().getTick() % 20 == 0) {
                    EntityDamageEvent drowningEvent = new EntityDamageEvent(this, DamageCause.DROWNING, 1f, 0);
                    this.damage(drowningEvent);
                } else {
                    this.setAirSupplyTicks(this.getAirSupplyTicks() - 1);
                }
            } else if (this.getAirSupplyTicks() < this.getMaxAirSupplyTicks()) {
                int increment;
                if (this.getComponent(EntityBreathableComponent.class).getInhaleTime() <= 0) {
                    increment = this.getMaxAirSupplyTicks();
                } else {
                    increment = (int) Math.ceil(this.getMaxAirSupplyTicks() / this.getComponent(EntityBreathableComponent.class).getInhaleTime() / 20);
                }
                this.setAirSupplyTicks(this.getAirSupplyTicks() + increment);
            }

        }

        if (this.expect(EntityKeys.IS_VULNERABLE) && !this.isAlive()) {
            this.kill();
        }

        if (this.deathAnimationTicks != -1 && --this.deathAnimationTicks <= 0) {
            this.endDeathAnimation();
            this.despawn();
        }

        for (Block block : this.getCollisionBlocks()) {
            if (block.getBoundingBox().collidesWith(this.getBoundingBox())) {
                block.getBehavior().onCollision(this, block);
            }
        }

        Block blockBelow = world.getBlock(this.getFloorX(), this.getFloorY() - 1, this.getFloorZ());
        blockBelow.getBehavior().onStandingOn(this, blockBelow);

        this.updateBossBarVisibility();
        this.ticks++;
    }

    protected void sendMovementPacket() {
        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();
        moveEntityPacket.setRuntimeEntityId(this.getId());
        moveEntityPacket.setPosition(this.getLocation().toVector3f().add(0, this.getBaseOffset(), 0));
        moveEntityPacket.setRotation(EntityHelper.getBasicRotationFor(this));
        moveEntityPacket.setTeleported(true);

        SetEntityMotionPacket setEntityVelocityPacket = new SetEntityMotionPacket();
        setEntityVelocityPacket.setRuntimeEntityId(this.getId());
        setEntityVelocityPacket.setMotion(this.getMotion());

        for (Player player : this.getViewers()) {
            player.sendPacket(moveEntityPacket);
            player.sendPacket(setEntityVelocityPacket);
        }
    }

    private float getKnockbackResistance() {
        float totalResistance = 0;
        if (this.getInventory().getHelmet() instanceof ArmorItem) {
            totalResistance += ((ArmorItem) this.getInventory().getHelmet()).getKnockbackResistance();
        }
        if (this.getInventory().getChestplate() instanceof ArmorItem) {
            totalResistance += ((ArmorItem) this.getInventory().getChestplate()).getKnockbackResistance();
        }
        if (this.getInventory().getLeggings() instanceof ArmorItem) {
            totalResistance += ((ArmorItem) this.getInventory().getLeggings()).getKnockbackResistance();
        }
        if (this.getInventory().getBoots() instanceof ArmorItem) {
            totalResistance += ((ArmorItem) this.getInventory().getBoots()).getKnockbackResistance();
        }

        return Math.max(0, Math.min(1, totalResistance));
    }

    /**
     * Damage this entity.
     * The event should be nothing more than the raw damage caused by the damage type.
     * It should not take into consideration armour points/effects as they are handled in this method.
     * @param event damage event
     * @return if the damage went through
     */
    public boolean damage(EntityDamageEvent event) {
        if (!this.expect(EntityKeys.IS_VULNERABLE)
                || !this.isAlive()
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

        // Apply KB resistance
        if (event instanceof EntityDamageByEntityEvent) {
            ((EntityDamageByEntityEvent) event).setKnockback(((EntityDamageByEntityEvent) event).getKnockback().mul(1 - this.getKnockbackResistance()));
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
                boolean isAttackSensor = sensor.getCause().filter(cause -> cause.equals(event.getCause())).isPresent() || sensor.getCause().isEmpty();

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

            Vector2f directionVector = Vector2f.ZERO;
            if (!(NumberUtils.isNearlyEqual(this.getX(), attacker.getX()) && NumberUtils.isNearlyEqual(this.getZ(), attacker.getZ()))) {
                directionVector = Vector2f.from(this.getX() - attacker.getX(), this.getZ() - attacker.getZ()).normalize();
            }
            Vector3f knockback = damageByEntityEvent.getKnockback().mul(directionVector.getX(), 1, directionVector.getY());
            this.setMotion(knockback);
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
        int chunkDistanceToViewer = (int) Math.round(
                Math.sqrt(
                        Math.pow(player.getChunk().getX() - this.getChunk().getX(), 2) +
                        Math.pow(player.getChunk().getZ() - this.getChunk().getZ(), 2)
                )
        );

        return chunkDistanceToViewer < this.expect(EntityKeys.WORLD).getServer().getConfig().getEntityChunkRenderDistance();
    }

    @Override
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
            addEntityPacket.setUniqueEntityId(this.getId());
            addEntityPacket.setRuntimeEntityId(this.getId());
            addEntityPacket.setIdentifier(this.getEntityDefinition().getEntityId());
            addEntityPacket.setPosition(this.getLocation().toVector3f().add(0, this.getBaseOffset(), 0));
            addEntityPacket.setMotion(this.getMotion());
            addEntityPacket.setRotation(EntityHelper.getBasicRotationFor(this));
            addEntityPacket.getMetadata().putAll(this.getMetaData().serialize());
            player.sendPacket(addEntityPacket);
            this.sendEquipmentPacket(player);

            return true;
        } else {
            return false;
        }
    }

    protected void sendEquipmentPacket(Player player) {
        Item helmet = this.getInventory().getHelmet();
        Item chestplate = this.getInventory().getChestplate();
        Item leggings = this.getInventory().getLeggings();
        Item boots = this.getInventory().getBoots();
        boolean wearingAmour = !(helmet.isEmpty() && chestplate.isEmpty() && leggings.isEmpty() && boots.isEmpty());

        if (wearingAmour) {
            MobArmorEquipmentPacket mobArmourEquipmentPacket = new MobArmorEquipmentPacket();
            mobArmourEquipmentPacket.setRuntimeEntityId(this.getId());
            mobArmourEquipmentPacket.setHelmet(ItemUtils.serializeForNetwork(helmet, player.getVersion()));
            mobArmourEquipmentPacket.setChestplate(ItemUtils.serializeForNetwork(chestplate, player.getVersion()));
            mobArmourEquipmentPacket.setLeggings(ItemUtils.serializeForNetwork(leggings, player.getVersion()));
            mobArmourEquipmentPacket.setBoots(ItemUtils.serializeForNetwork(boots, player.getVersion()));
            player.sendPacket(mobArmourEquipmentPacket);
        }
    }

    @Override
    public boolean despawnFrom(Player player) {
        if (this.spawnedTo.remove(player)) {
            RemoveEntityPacket entityPacket = new RemoveEntityPacket();
            entityPacket.setUniqueEntityId(this.getId());
            player.sendPacket(entityPacket);

            this.getBossBar().ifPresent(bossBar -> ((ImplBossBar) bossBar).despawnFrom(player));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void despawn() {
        this.get(EntityKeys.WORLD).ifPresentOrElse(
                world -> world.removeEntity(this),
                () -> Server.getInstance()
                        .getLogger()
                        .warn(String.format("Attempted to despawn entity without a null world (ID: %s)", this.getId()))
        );
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
