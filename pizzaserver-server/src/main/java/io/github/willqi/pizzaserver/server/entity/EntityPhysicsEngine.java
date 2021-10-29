package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class EntityPhysicsEngine {

    protected final ImplEntity entity;
    protected boolean updatePosition = true;

    protected Vector3 velocity = new Vector3(0, 0, 0);
    protected Vector3 airAcceleration = new Vector3(0, -0.05f, 0);
    protected Vector3 groundAcceleration = new Vector3(-0.05f, 0, -0.01f);
    protected float terminalYVelocity = 0.4f;


    public EntityPhysicsEngine(ImplEntity entity) {
        this.entity = entity;
    }

    public Vector3 getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    /**
     * Retrieve whether ticking this physics engine will update the position of entities or if it will only change the calculated velocity.
     * @return if this engine will update the position of entities.
     */
    public boolean shouldUpdatePosition() {
        return this.updatePosition;
    }

    /**
     * Change whether ticking this physics engine will update the position of entities or if it will only change the calculated velocity.
     * @param enabled if this engine will update the position of entities.
     */
    public void setPositionUpdate(boolean enabled) {
        this.updatePosition = enabled;
    }

    public void tick() {
        if (this.getVelocity().getLength() > 0) {
            Vector3 newVelocity = this.getVelocity().add(this.airAcceleration);
            newVelocity.setY(Math.max(Math.min(newVelocity.getY(), this.terminalYVelocity), -this.terminalYVelocity));
            this.setVelocity(newVelocity);

            if (this.shouldUpdatePosition()) {
                Location location = this.entity.getLocation().add(this.getVelocity().getX(), this.getVelocity().getY(), this.getVelocity().getZ());
                this.entity.moveTo(location.getX(), location.getY(), location.getZ());
            }
        }
    }

}
