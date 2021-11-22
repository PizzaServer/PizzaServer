package io.github.pizzaserver.api.entity.boss;

public interface BossBar {

    String getTitle();

    void setTitle(String title);

    float getPercentage();

    void setPercentage(float percentage);

    int getRenderRange();

    void setRenderRange(int range);

    boolean darkenSky();

    void setDarkenSky(boolean darkenSky);

}
