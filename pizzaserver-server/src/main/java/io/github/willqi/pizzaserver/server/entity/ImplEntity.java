package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.LivingEntity;
import io.github.willqi.pizzaserver.api.entity.attributes.Attribute;
import io.github.willqi.pizzaserver.api.entity.attributes.AttributeType;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.network.protocol.packets.AddEntityPacket;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.commons.utils.NumberUtils;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.level.ImplLevel;
import io.github.willqi.pizzaserver.server.level.world.ImplWorld;
import io.github.willqi.pizzaserver.server.level.world.chunks.ImplChunk;
import io.github.willqi.pizzaserver.api.network.protocol.packets.RemoveEntityPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.SetEntityDataPacket;

import java.util.*;

public class ImplEntity implements Entity {

    public static long ID = 1;

    protected final long id;
    protected volatile float x;
    protected volatile float y;
    protected volatile float z;
    protected volatile World world;
    protected boolean moveUpdate;

    protected final EntityAttributes attributes = new EntityAttributes();

    protected float pitch;
    protected float yaw;
    protected float headYaw;

    protected boolean hasAI;
    protected final EntityDefinition entityDefinition;
    protected final LinkedList<EntityComponentGroup> componentGroups = new LinkedList<>();

    protected EntityMetaData metaData = new EntityMetaData();
    protected boolean metaDataUpdate;

    protected boolean spawned;
    protected final Set<Player> spawnedTo = new HashSet<>();
    protected final Set<Player> hiddenFrom = new HashSet<>();


    public ImplEntity(EntityDefinition entityDefinition) {
        this.id = ID++;
        this.entityDefinition = entityDefinition;
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
    public boolean removeComponentGroup(EntityComponentGroup group) {
        if (this.componentGroups.remove(group)) {
            // Unregister components of the group from this entity and then find and apply the new active components of the same types.
            for (EntityComponent removeComponent : group.getComponents()) {
                Class<? extends EntityComponent> removeComponentClazz = removeComponent.getClass();
                EntityComponentHandler handler = EntityRegistry.getComponentHandler(removeComponentClazz);

                handler.onUnregistered(this, group.getComponent(removeComponentClazz));
                handler.onRegistered(this, this.getComponent(removeComponentClazz));
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
    public Location getLocation() {
        return new Location(this.world, new Vector3(this.getX(), this.getY(), this.getZ()));
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    /**
     * Set the location of the entity.
     * Used internally to setup and to clean up the entity
     * @param location entity location
     */
    public void setLocation(Location location) {
        if (location != null) {
            this.x = location.getX();
            this.y = location.getY();
            this.z = location.getZ();
            this.world = location.getWorld();
        } else {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.world = null;
        }
    }

    @Override
    public void teleport(float x, float y, float z) {
        this.teleport(this.getWorld(), x, y, z);
    }

    @Override
    public void teleport(World world, float x, float y, float z) {
        this.moveUpdate = true;

        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @Override
    public float getEyeHeight() {
        return this.getHeight() / 2 + 0.1f;
    }

    @Override
    public String getDisplayName() {
        return this.getMetaData().getStringProperty(EntityMetaPropertyName.NAMETAG);
    }

    @Override
    public void setDisplayName(String name) {
        this.getMetaData().setStringProperty(EntityMetaPropertyName.NAMETAG, name);
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
        this.moveUpdate = true;
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
    public boolean hasAI() {
        return this.hasAI;
    }

    @Override
    public void setAI(boolean hasAI) {
        this.hasAI = hasAI;
    }

    @Override
    public float getScale() {
        return this.getMetaData().getFloatProperty(EntityMetaPropertyName.SCALE);
    }

    @Override
    public void setScale(float scale) {
        EntityMetaData data = this.getMetaData();
        data.setFloatProperty(EntityMetaPropertyName.SCALE, scale);
        this.setMetaData(data);
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public void moveTo(float x, float y, float z) {
        this.moveUpdate = true;

        ImplChunk currentChunk = this.getChunk();
        this.x = x;
        this.y = y;
        this.z = z;

        ImplChunk newChunk = this.getWorld().getChunk((int) Math.floor(this.x / 16), (int) Math.floor(this.z / 16));
        if (!currentChunk.equals(newChunk)) {   // spawn entity in new chunk and remove from old chunk
            currentChunk.removeEntity(this);
            newChunk.addEntity(this);
        }
    }

    @Override
    public void tick() {
        this.moveUpdate = false;

        if (this.metaDataUpdate) {
            this.metaDataUpdate = false;
            SetEntityDataPacket entityDataPacket = new SetEntityDataPacket();
            entityDataPacket.setRuntimeId(this.getId());
            entityDataPacket.setData(this.getMetaData());
            for (Player player : this.getViewers()) {
                player.sendPacket(entityDataPacket);
            }
        }
    }

    /**
     * Called when the entity is initially spawned into a world.
     * This is useful for entity initialization.
     */
    public void onSpawned() {
        this.spawned = true;
    }

    /**
     * Called when the entity is completely despawned.
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
                && (!(this instanceof LivingEntity) || !((LivingEntity) this).isHiddenFrom(player))
                && this.withinEntityRenderDistanceTo(player)
                && this.getChunk().canBeVisibleTo(player);
    }

    public boolean shouldBeDespawnedFrom(Player player) {
        return (this.getChunk() == null || !this.getChunk().canBeVisibleTo(player) || !this.withinEntityRenderDistanceTo(player))
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
            addEntityPacket.setEntityType(this.getEntityDefinition().getEntityId());
            addEntityPacket.setPosition(this.getLocation());
            addEntityPacket.setVelocity(new Vector3(0, 0, 0));
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
