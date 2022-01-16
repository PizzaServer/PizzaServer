package io.github.pizzaserver.server.scoreboard;

import com.nukkitx.protocol.bedrock.data.ScoreInfo;
import com.nukkitx.protocol.bedrock.packet.SetScorePacket;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.scoreboard.Scoreboard;
import io.github.pizzaserver.api.scoreboard.ScoreboardLine;

import java.util.Collections;

public class ImplScoreboardLine implements ScoreboardLine {

    protected final ImplScoreboard scoreboard;
    protected final long lineId;

    protected String text;
    protected int score;

    public ImplScoreboardLine(Scoreboard scoreboard, String text, int score) {
        this.scoreboard = (ImplScoreboard) scoreboard;
        this.lineId = ((ImplScoreboard) scoreboard).getNextLineId();
        this.text = text;
        this.score = score;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String content) {
        this.text = content;

        for (Player viewer : this.scoreboard.getViewers()) {
            this.despawnFrom(viewer);
            this.spawnTo(viewer);
        }
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public void setScore(int score) {
        this.score = score;

        for (Player viewer : this.scoreboard.getViewers()) {
            this.spawnTo(viewer);
        }
    }

    public void remove() {
        for (Player viewer : this.scoreboard.getViewers()) {
            this.despawnFrom(viewer);
        }
    }

    protected void spawnTo(Player player) {
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.setAction(SetScorePacket.Action.SET);
        setScorePacket.setInfos(Collections.singletonList(this.getScoreInfo()));
        player.sendPacket(setScorePacket);
    }

    protected void despawnFrom(Player player) {
        SetScorePacket removeScorePacket = new SetScorePacket();
        removeScorePacket.setAction(SetScorePacket.Action.REMOVE);
        removeScorePacket.setInfos(Collections.singletonList(this.getScoreInfo()));
        player.sendPacket(removeScorePacket);
    }

    public ScoreInfo getScoreInfo() {
        return new ScoreInfo(this.lineId, this.scoreboard.getObjectiveId(), this.getScore(), this.getText());
    }
}
