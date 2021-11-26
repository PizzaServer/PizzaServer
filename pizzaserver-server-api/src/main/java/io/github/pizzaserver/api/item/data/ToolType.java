package io.github.pizzaserver.api.item.data;

import io.github.pizzaserver.api.item.ToolTypeRegistry;
import io.github.pizzaserver.api.block.Block;

public class ToolType {

    private final String predecessor;
    private final String successor;
    private final float strength;


    public ToolType(String successor, String predecessor, float strength) {
        this.successor = successor;
        this.predecessor = predecessor;
        this.strength = strength;
    }

    public ToolType getSuccessor() {
        return ToolTypeRegistry.getToolType(this.successor);
    }

    public ToolType getPredecessor() {
        return ToolTypeRegistry.getToolType(this.predecessor);
    }

    public float getStrength() {
        return this.strength;
    }

    public boolean isCorrectTool(Block block) {
        if (block.getBlockState().getCorrectTools().contains(this)) {
            return true;
        }

        if (this.predecessor == null) {
            return false;
        }
        return this.getPredecessor().isCorrectTool(block);
    }

    public boolean isBestTool(Block block) {
        if (block.getBlockState().getBestTools().contains(this)) {
            return true;
        }

        if (this.successor == null) {
            return false;
        }
        return this.getSuccessor().isBestTool(block);
    }

}
