package io.github.willqi.pizzaserver.server.world.gamerules;

public abstract class GameRule<T> {

    private final String name;
    private final T value;


    public GameRule(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
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
