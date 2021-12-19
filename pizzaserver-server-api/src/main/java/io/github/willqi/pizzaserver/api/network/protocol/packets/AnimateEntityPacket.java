package io.github.willqi.pizzaserver.api.network.protocol.packets;

import java.util.*;

/**
 * Sent to the client to forcefully animate an entity.
 */
public class AnimateEntityPacket extends BaseBedrockPacket {

    public static final int ID = 0x9e;

    private String animation;
    private String nextState = "default";
    private String stopExpression = "query.any_animation_finished";
    private String controller;
    private float blendOutTime = 0;
    private Set<Long> entityRuntimeIDs = new HashSet<>();

    public AnimateEntityPacket() {
        super(ID);
    }

    public String getAnimation() {
        return this.animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public String getNextState() {
        return this.nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public String getStopExpression() {
        return this.stopExpression;
    }

    public void setStopExpression(String stopExpression) {
        this.stopExpression = stopExpression;
    }

    public String getController() {
        return this.controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public float getBlendOutTime() {
        return this.blendOutTime;
    }

    public void setBlendOutTime(float blendOutTime) {
        this.blendOutTime = blendOutTime;
    }

    public Set<Long> getEntityRuntimeIDs() {
        return this.entityRuntimeIDs;
    }

    public AnimateEntityPacket addEntityRuntimeID(long entityRuntimeID) {
        this.entityRuntimeIDs.add(entityRuntimeID);
        return this;
    }

    public void setEntityRuntimeIDs(Set<Long> entityRuntimeIDs) {
        this.entityRuntimeIDs = new HashSet<>(entityRuntimeIDs);
    }
}
