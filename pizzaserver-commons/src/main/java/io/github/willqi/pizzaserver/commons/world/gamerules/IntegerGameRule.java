package io.github.willqi.pizzaserver.commons.world.gamerules;

public class IntegerGameRule extends GameRule<Integer> {

    public IntegerGameRule(GameRuleID id, Integer value) {
        super(id, value);
    }

    @Override
    public GameRuleType getType() {
        return GameRuleType.INT;
    }

}
