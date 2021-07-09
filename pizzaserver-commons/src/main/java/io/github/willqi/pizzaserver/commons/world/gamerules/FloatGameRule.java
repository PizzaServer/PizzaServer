package io.github.willqi.pizzaserver.commons.world.gamerules;

public class FloatGameRule extends GameRule<Float> {

    public FloatGameRule(GameRuleId id, Float value) {
        super(id, value);
    }

    @Override
    public GameRuleType getType() {
        return GameRuleType.FLOAT;
    }

}
