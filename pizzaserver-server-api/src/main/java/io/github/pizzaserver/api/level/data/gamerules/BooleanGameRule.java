package io.github.pizzaserver.api.level.data.gamerules;

public class BooleanGameRule extends GameRule<Boolean> {

    public BooleanGameRule(GameRuleID id, Boolean value) {
        super(id, value);
    }

    @Override
    public GameRuleType getType() {
        return GameRuleType.BOOLEAN;
    }
}
