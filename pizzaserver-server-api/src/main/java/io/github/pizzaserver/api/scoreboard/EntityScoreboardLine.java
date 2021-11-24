package io.github.pizzaserver.api.scoreboard;

import io.github.pizzaserver.api.entity.Entity;

public interface EntityScoreboardLine extends ScoreboardLine {

    Entity getEntity();

}
