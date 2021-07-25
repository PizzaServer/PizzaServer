package io.github.willqi.pizzaserver.server.network.protocol.packets;

import java.util.*;

public class AnimateEntityPacket extends ImplBedrockPacket {

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
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public String getStopExpression() {
        return stopExpression;
    }

    public void setStopExpression(String stopExpression) {
        this.stopExpression = stopExpression;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public float getBlendOutTime() {
        return blendOutTime;
    }

    public void setBlendOutTime(float blendOutTime) {
        this.blendOutTime = blendOutTime;
    }

    public Set<Long> getEntityRuntimeIDs() {
        return entityRuntimeIDs;
    }

    public AnimateEntityPacket addEntityRuntimeID(long entityRuntimeID) {
        this.entityRuntimeIDs.add(entityRuntimeID);
        return this;
    }

    public void setEntityRuntimeIDs(Set<Long> entityRuntimeIDs) {
        this.entityRuntimeIDs = new HashSet<>(entityRuntimeIDs);
    }
}
