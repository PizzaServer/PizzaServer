package io.github.willqi.pizzaserver.commons.world.gamerules;

public abstract class GameRule<T> {

    private final GameRuleId id;
    private final T value;


    public GameRule(GameRuleId id, T value) {
        this.id = id;
        this.value = value;
    }

    public GameRuleId getId() {
        return this.id;
    }

    public T getValue() {
        return this.value;
    }

    public abstract GameRuleType getType();

    public enum GameRuleType {
        BOOLEAN(1),
        INT(2),
        FLOAT(3);


        private final int id;

        GameRuleType(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

    }

}
