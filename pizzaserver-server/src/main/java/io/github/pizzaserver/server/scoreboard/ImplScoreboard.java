package io.github.pizzaserver.server.scoreboard;

import io.github.pizzaserver.api.scoreboard.DisplaySlot;
import io.github.pizzaserver.api.scoreboard.Scoreboard;
import io.github.pizzaserver.api.scoreboard.ScoreboardLine;
import io.github.pizzaserver.api.scoreboard.SortOrder;

import java.util.List;

public class ImplScoreboard implements Scoreboard {

    @Override
    public String getDisplayName() {

        return null;
    }

    @Override
    public void setDisplayName(String displayName) {

    }

    @Override
    public DisplaySlot getDisplaySlot() {

        return null;
    }

    @Override
    public void setDisplaySlot(DisplaySlot displaySlot) {

    }

    @Override
    public SortOrder getSortOrder() {

        return null;
    }

    @Override
    public void setSortOrder(SortOrder sortOrder) {

    }

    @Override
    public List<ScoreboardLine> getLines() {

        return null;
    }

    @Override
    public ScoreboardLine addLine(String line, int score) {

        return null;
    }

    @Override
    public void removeLine(ScoreboardLine display) {

    }

}
