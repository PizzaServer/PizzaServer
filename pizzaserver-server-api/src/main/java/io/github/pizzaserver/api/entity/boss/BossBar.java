package io.github.pizzaserver.api.entity.boss;

import io.github.pizzaserver.api.utils.Watchable;

public interface BossBar extends Watchable {

    String getTitle();

    void setTitle(String title);

    float getPercentage();

    void setPercentage(float percentage);

    int getRenderRange();

    void setRenderRange(int range);

    boolean darkenSky();

    void setDarkenSky(boolean darkenSky);

}
