package io.github.willqi.pizzaserver.api.level.data.gamerules;

public class FloatGameRule extends GameRule<Float> {

    public FloatGameRule(GameRuleID id, Float value) {
        super(id, value);
    }

    @Override
    public GameRuleType getType() {
        return GameRuleType.FLOAT;
    }

}
