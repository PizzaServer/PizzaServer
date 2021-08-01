package io.github.willqi.pizzaserver.commons.world.gamerules;

public class BooleanGameRule extends GameRule<Boolean> {

    public BooleanGameRule(GameRuleID id, Boolean value) {
        super(id, value);
    }

    @Override
    public GameRuleType getType() {
        return GameRuleType.BOOLEAN;
    }

}
