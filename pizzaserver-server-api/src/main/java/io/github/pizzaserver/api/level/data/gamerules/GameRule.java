package io.github.pizzaserver.api.level.data.gamerules;

public abstract class GameRule<T> {

    private final GameRuleID id;
    private final T value;


    public GameRule(GameRuleID id, T value) {
        this.id = id;
        this.value = value;
    }

    public GameRuleID getId() {
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
