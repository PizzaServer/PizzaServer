package io.github.pizzaserver.api.scoreboard;

public interface ScoreboardLine {

    String getText();

    void setText(String content);

    int getScore();

    void setScore(int score);

}
