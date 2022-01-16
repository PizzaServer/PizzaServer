package io.github.pizzaserver.server.scoreboard;

import com.nukkitx.protocol.bedrock.packet.RemoveObjectivePacket;
import com.nukkitx.protocol.bedrock.packet.SetDisplayObjectivePacket;
import com.nukkitx.protocol.bedrock.packet.SetScorePacket;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.scoreboard.*;

import java.util.*;
import java.util.stream.Collectors;

public class ImplScoreboard implements Scoreboard {

    protected long lineId;

    protected String objectiveId = UUID.randomUUID().toString();
    protected String displayName = "";
    protected SortOrder sortOrder = SortOrder.DESCENDING;

    protected Set<ScoreboardLine> lines = new HashSet<>();

    protected final Set<Player> viewers = new HashSet<>();
    protected final Map<UUID, DisplaySlot> viewerDisplaySlotTypes = new HashMap<>();


    public String getObjectiveId() {
        return this.objectiveId;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;

        for (Player player : this.getViewers()) {
            this.sendObjectivePacket(player);
        }
    }

    @Override
    public SortOrder getSortOrder() {
        return this.sortOrder;
    }

    @Override
    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;

        for (Player player : this.getViewers()) {
            this.sendObjectivePacket(player);
        }
    }

    @Override
    public Set<ScoreboardLine> getLines() {
        return new HashSet<>(this.lines);
    }

    public List<ScoreboardLine> getSortedLines() {
        if (this.getSortOrder() == SortOrder.DESCENDING) {
            return this.lines.stream()
                             .sorted(Comparator.comparingInt(ScoreboardLine::getScore).reversed())
                             .collect(Collectors.toList());
        }
        return this.lines.stream()
                         .sorted(Comparator.comparingInt(ScoreboardLine::getScore))
                         .collect(Collectors.toList());
    }

    @Override
    public ScoreboardLine addLine(String line, int score) {
        ScoreboardLine scoreboardLine = new ImplScoreboardLine(this, line, score);
        this.lines.add(scoreboardLine);

        return scoreboardLine;
    }

    @Override
    public EntityScoreboardLine addLine(Entity entity, int score) {
        EntityScoreboardLine scoreboardLine = new ImplEntityScoreboardLine(this, entity, score);
        this.lines.add(scoreboardLine);

        return scoreboardLine;
    }

    @Override
    public boolean removeLine(ScoreboardLine line) {
        if (this.lines.contains(line)) {
            ((ImplScoreboardLine) line).remove();
            this.lines.remove(line);
            return true;
        }
        return false;
    }

    public long getNextLineId() {
        return this.lineId++;
    }

    @Override
    public Set<Player> getViewers() {
        return new HashSet<>(this.viewers);
    }

    public boolean spawnTo(Player player, DisplaySlot slot) {
        if (this.viewers.add(player)) {
            this.viewerDisplaySlotTypes.put(player.getUUID(), slot);
            this.sendObjectivePacket(player);

            SetScorePacket setScorePacket = new SetScorePacket();
            setScorePacket.setAction(SetScorePacket.Action.SET);
            setScorePacket.setInfos(this.getLines()
                                        .stream()
                                        .map(line -> ((ImplScoreboardLine) line).getScoreInfo())
                                        .collect(Collectors.toList()));
            player.sendPacket(setScorePacket);
        }
        return true;
    }

    public boolean despawnFrom(Player player) {
        if (this.viewers.remove(player)) {
            this.viewerDisplaySlotTypes.remove(player.getUUID());

            RemoveObjectivePacket removeObjectivePacket = new RemoveObjectivePacket();
            removeObjectivePacket.setObjectiveId(this.getObjectiveId());
            player.sendPacket(removeObjectivePacket);
            return true;
        }
        return false;
    }

    private void sendObjectivePacket(Player player) {
        SetDisplayObjectivePacket objectivePacket = new SetDisplayObjectivePacket();
        objectivePacket.setDisplaySlot(this.viewerDisplaySlotTypes.get(player.getUUID()).getId());
        objectivePacket.setObjectiveId(this.getObjectiveId());
        objectivePacket.setDisplayName(this.getDisplayName());
        objectivePacket.setCriteria("dummy");
        objectivePacket.setSortOrder(this.getSortOrder().ordinal());
        player.sendPacket(objectivePacket);
    }
}
