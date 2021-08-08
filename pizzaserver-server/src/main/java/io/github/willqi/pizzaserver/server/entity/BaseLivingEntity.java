package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.LivingEntity;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.server.world.chunks.ImplChunk;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseLivingEntity extends BaseEntity implements LivingEntity {

    protected float health;
    protected float maxHealth;

    protected float absorption;
    protected float maxAbsorption;

    protected float movementSpeed;

    protected float pitch;
    protected float yaw;
    protected float headYaw;

    private final Set<Player> hiddenFrom = new HashSet<>();


    @Override
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(float health) {
        this.health = Math.max(0, Math.min(health, this.getMaxHealth()));

        if (this.getHealth() <= 0) {
            // TODO: kill
        }
    }

    @Override
    public float getMaxHealth() {
        return this.maxHealth;
    }

    @Override
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = Math.max(0, maxHealth);
        this.setHealth(Math.min(this.getHealth(), this.getMaxHealth()));
    }

    @Override
    public float getAbsorption() {
        return this.absorption;
    }

    @Override
    public void setAbsorption(float absorption) {
        this.absorption = Math.max(0, Math.min(absorption, this.getMaxAbsorption()));
    }

    @Override
    public float getMaxAbsorption() {
        return this.maxAbsorption;
    }

    @Override
    public void setMaxAbsorption(float maxAbsorption) {
        this.maxAbsorption = Math.max(0, maxAbsorption);
        this.setAbsorption(Math.min(this.getAbsorption(), this.getMaxAbsorption()));
    }

    @Override
    public float getMovementSpeed() {
        return this.movementSpeed;
    }

    @Override
    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = Math.max(0, movementSpeed);
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public float getYaw() {
        return this.yaw;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public float getHeadYaw() {
        return this.headYaw;
    }

    @Override
    public void setHeadYaw(float headYaw) {
        this.headYaw = headYaw;
    }

    @Override
    public void moveTo(float x, float y, float z) {
        ImplChunk currentChunk = (ImplChunk)this.getChunk();
        ImplChunk newChunk = (ImplChunk)this.getWorld().getChunkManager().getChunk((int)Math.floor(this.x / 16), (int)Math.floor(this.z / 16));
        this.x = x;
        this.y = y;
        this.z = z;

        if (!currentChunk.equals(newChunk)) {   // spawn entity in new chunk and remove from old chunk
            currentChunk.removeEntity(this);
            newChunk.addEntity(this);

            // Remove entity from players who are in other chunks and can no longer see this entity
            for (Player player : this.getViewers()) {
                if (!this.getChunk().canBeVisibleTo(player)) {
                    this.despawnFrom(player);
                }
            }
        }
    }

    @Override
    public void teleport(float x, float y, float z) {
        this.teleport(this.getWorld(), x, y, z);
    }

    @Override
    public void teleport(World world, float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @Override
    public void showTo(Player player) {
        this.hiddenFrom.remove(player);
        if (this.getChunk().canBeVisibleTo(player) && !this.getViewers().contains(player)) {
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

}
