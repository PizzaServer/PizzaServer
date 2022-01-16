package io.github.pizzaserver.server.entity.boss;

import com.nukkitx.protocol.bedrock.packet.BossEventPacket;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.player.Player;

import java.util.HashSet;
import java.util.Set;

public class ImplBossBar implements BossBar {

    protected long entityId = -1;
    protected boolean dummy = true;

    protected String title;
    protected float percentage;
    protected int renderRange;
    protected boolean darkenSky;
    protected Color color = Color.PURPLE;

    protected final Set<Player> viewers = new HashSet<>();


    public long getEntityId() {
        return this.entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public boolean isDummy() {
        return this.dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;

        BossEventPacket newBossTitlePacket = new BossEventPacket();
        newBossTitlePacket.setBossUniqueEntityId(this.getEntityId());
        newBossTitlePacket.setAction(BossEventPacket.Action.UPDATE_NAME);
        newBossTitlePacket.setTitle(this.getTitle());
        for (Player viewer : this.getViewers()) {
            viewer.sendPacket(newBossTitlePacket);
        }
    }

    @Override
    public float getPercentage() {
        return this.percentage;
    }

    @Override
    public void setPercentage(float percentage) {
        this.percentage = Math.max(Math.min(1, percentage), 0);

        BossEventPacket newPercentagePacket = new BossEventPacket();
        newPercentagePacket.setBossUniqueEntityId(this.getEntityId());
        newPercentagePacket.setAction(BossEventPacket.Action.UPDATE_PERCENTAGE);
        newPercentagePacket.setHealthPercentage(this.getPercentage());
        for (Player viewer : this.getViewers()) {
            viewer.sendPacket(newPercentagePacket);
        }
    }

    @Override
    public int getRenderRange() {
        return this.renderRange;
    }

    @Override
    public void setRenderRange(int range) {
        this.renderRange = range;
    }

    @Override
    public boolean darkenSky() {
        return this.darkenSky;
    }

    @Override
    public void setDarkenSky(boolean darkenSky) {
        this.darkenSky = darkenSky;

        BossEventPacket darkenSkyPacket = new BossEventPacket();
        darkenSkyPacket.setBossUniqueEntityId(this.getEntityId());
        darkenSkyPacket.setAction(BossEventPacket.Action.UPDATE_PROPERTIES);
        darkenSkyPacket.setColor(this.getColor().ordinal());
        darkenSkyPacket.setDarkenSky(this.darkenSky() ? 1 : 0);
        for (Player viewer : this.getViewers()) {
            viewer.sendPacket(darkenSkyPacket);
        }
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;

        BossEventPacket updateColorPacket = new BossEventPacket();
        updateColorPacket.setBossUniqueEntityId(this.getEntityId());
        updateColorPacket.setAction(BossEventPacket.Action.UPDATE_STYLE);
        updateColorPacket.setColor(this.getColor().ordinal());
        for (Player viewer : this.getViewers()) {
            viewer.sendPacket(updateColorPacket);
        }
    }

    @Override
    public Set<Player> getViewers() {
        return new HashSet<>(this.viewers);
    }

    public boolean spawnTo(Player player) {
        if (this.viewers.add(player)) {
            BossEventPacket createBossBarPacket = new BossEventPacket();
            createBossBarPacket.setBossUniqueEntityId(this.getEntityId());
            createBossBarPacket.setAction(BossEventPacket.Action.CREATE);
            createBossBarPacket.setTitle(this.getTitle());
            createBossBarPacket.setHealthPercentage(this.getPercentage());
            createBossBarPacket.setDarkenSky(this.darkenSky() ? 1 : 0);
            createBossBarPacket.setColor(this.getColor().ordinal());
            player.sendPacket(createBossBarPacket);
            return true;
        }
        return false;
    }

    public boolean despawnFrom(Player player) {
        if (this.viewers.remove(player)) {
            BossEventPacket removeBossBarPacket = new BossEventPacket();
            removeBossBarPacket.setBossUniqueEntityId(this.getEntityId());
            removeBossBarPacket.setAction(BossEventPacket.Action.REMOVE);
            player.sendPacket(removeBossBarPacket);
            return true;
        }
        return false;
    }
}
