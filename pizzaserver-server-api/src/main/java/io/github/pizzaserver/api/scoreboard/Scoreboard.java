package io.github.pizzaserver.api.scoreboard;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.utils.Watchable;

import java.util.List;
import java.util.Set;

public interface Scoreboard extends Watchable {

    String getDisplayName();

    void setDisplayName(String displayName);

    SortOrder getSortOrder();

    void setSortOrder(SortOrder sortOrder);

    Set<ScoreboardLine> getLines();

    List<ScoreboardLine> getSortedLines();

    ScoreboardLine addLine(String line, int score);

    EntityScoreboardLine addLine(Entity entity, int score);

    boolean removeLine(ScoreboardLine display);
}
