package io.github.pizzaserver.api.scoreboard;

import java.util.List;

public interface Scoreboard {

    String getDisplayName();

    void setDisplayName(String displayName);

    DisplaySlot getDisplaySlot();

    void setDisplaySlot(DisplaySlot displaySlot);

    SortOrder getSortOrder();

    void setSortOrder(SortOrder sortOrder);

    List<ScoreboardLine> getLines();

    ScoreboardLine addLine(String line, int score);

    void removeLine(ScoreboardLine display);

}
