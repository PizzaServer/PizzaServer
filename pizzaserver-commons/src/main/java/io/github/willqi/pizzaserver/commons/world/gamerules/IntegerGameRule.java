package io.github.willqi.pizzaserver.commons.world.gamerules;

public class IntegerGameRule extends GameRule<Integer> {

    public IntegerGameRule(String name, Integer value) {
        super(name, value);
    }

    @Override
    public GameRuleType getType() {
        return GameRuleType.INT;
    }

}
