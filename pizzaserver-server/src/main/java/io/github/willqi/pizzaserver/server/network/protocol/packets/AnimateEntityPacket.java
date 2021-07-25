package io.github.willqi.pizzaserver.server.network.protocol.packets;

import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;

public class AnimateEntityPacket extends ImplBedrockPacket {

    public static final int ID = 0x9e;

    private String animation;
    private String nextState = "default";
    private String stopExpression = "query.any_animation_finished";
    private String controller;
    private float blendOutTime = 0;
    private final UnsignedInteger entityArraySize = UnsignedInteger.valueOf(1);
    private UnsignedLong entityRuntimeID;

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

    public UnsignedInteger getEntityArraySize() {
        return entityArraySize;
    }

    public UnsignedLong getEntityRuntimeID() {
        return entityRuntimeID;
    }

    public void setEntityRuntimeID(long entityRuntimeID) {
        this.entityRuntimeID = UnsignedLong.valueOf(entityRuntimeID);
    }
}
