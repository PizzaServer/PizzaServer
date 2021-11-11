package io.github.willqi.pizzaserver.api.entity.boss;

public class BossBar implements Cloneable {

    protected String title;
    private float percentage;
    protected int renderRange;
    protected boolean darkenSky;


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPercentage() {
        return this.percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getRenderRange() {
        return this.renderRange;
    }

    public void setRenderRange(int range) {
        this.renderRange = range;
    }

    public boolean darkenSky() {
        return this.darkenSky;
    }

    public void setDarkenSky(boolean darkenSky) {
        this.darkenSky = darkenSky;
    }

    @Override
    public BossBar clone() {
        try {
            return (BossBar) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError("Cloning threw an exception.", exception);
        }
    }

}
