package io.github.pizzaserver.api.level.data.gamerules;

public class IntegerGameRule extends GameRule<Integer> {

    public IntegerGameRule(GameRuleID id, Integer value) {
        super(id, value);
    }

    @Override
    public GameRuleType getType() {
        return GameRuleType.INT;
    }

}
