package io.github.willqi.pizzaserver.commons.world.gamerules;

public class BooleanGameRule extends GameRule<Boolean> {

    public BooleanGameRule(String name, Boolean value) {
        super(name, value);
    }

    @Override
    public GameRuleType getType() {
        return GameRuleType.BOOLEAN;
    }

}
