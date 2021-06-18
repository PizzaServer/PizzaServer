package io.github.willqi.pizzaserver.commons.world.gamerules;

public class FloatGameRule extends GameRule<Float> {

    public FloatGameRule(String name, Float value) {
        super(name, value);
    }

    @Override
    public GameRuleType getType() {
        return GameRuleType.FLOAT;
    }

}
