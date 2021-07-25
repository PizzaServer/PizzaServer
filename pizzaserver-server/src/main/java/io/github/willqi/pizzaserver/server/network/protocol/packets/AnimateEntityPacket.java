package io.github.willqi.pizzaserver.server.network.protocol.packets;

import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import com.google.common.primitives.UnsignedLongs;

import java.util.ArrayList;
import java.util.Arrays;

public class AnimateEntityPacket extends ImplBedrockPacket {

    public static final int ID = 0x9e;

    private String animation;
    private String nextState = "default";
    private String stopExpression = "query.any_animation_finished";
    private String controller;
    private float blendOutTime = 0;
    private ArrayList<UnsignedLong> entityRuntimeIDs = new ArrayList<>();

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

    public ArrayList<UnsignedLong> getEntityRuntimeIDs() {
        return entityRuntimeIDs;
    }

    public void addEntityRuntimeID(long entityRuntimeID) {
        entityRuntimeIDs.add(UnsignedLong.valueOf(entityRuntimeID));
    }

    public void addEntityRuntimeID(UnsignedLong entityRuntimeID) {
        entityRuntimeIDs.add(entityRuntimeID);
    }

    public void setEntityRuntimeIDs(ArrayList<UnsignedLong> entityRuntimeIDs) {
        this.entityRuntimeIDs = entityRuntimeIDs;
    }
}
