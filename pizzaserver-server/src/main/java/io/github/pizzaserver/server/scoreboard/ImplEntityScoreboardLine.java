package io.github.pizzaserver.server.scoreboard;

import com.nukkitx.protocol.bedrock.data.ScoreInfo;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.scoreboard.EntityScoreboardLine;
import io.github.pizzaserver.api.scoreboard.Scoreboard;

public class ImplEntityScoreboardLine extends ImplScoreboardLine implements EntityScoreboardLine {

    protected final Entity entity;


    public ImplEntityScoreboardLine(Scoreboard scoreboard, Entity entity, int score) {
        super(scoreboard, null, score);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public String getText() {
        return this.entity.getName();
    }

    @Override
    public void setText(String content) {
        throw new IllegalStateException("Cannot set scoreboard text of entity scoreboard line");
    }

    @Override
    public ScoreInfo getScoreInfo() {
        ScoreInfo.ScorerType scorerType = ScoreInfo.ScorerType.ENTITY;
        if (this.getEntity() instanceof Player) {
            scorerType = ScoreInfo.ScorerType.PLAYER;
        }

        return new ScoreInfo(this.lineId,
                             this.scoreboard.getObjectiveId(),
                             this.getScore(),
                             scorerType,
                             this.getEntity().getId());
    }
}
